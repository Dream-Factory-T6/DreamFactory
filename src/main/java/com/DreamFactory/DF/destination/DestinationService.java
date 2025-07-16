package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.cloudinary.CloudinaryService;
import com.DreamFactory.DF.destination.dto.DestinationFilterRequest;
import com.DreamFactory.DF.destination.dto.DestinationMapper;
import com.DreamFactory.DF.destination.dto.DestinationRequest;
import com.DreamFactory.DF.destination.dto.DestinationResponse;
import com.DreamFactory.DF.destination.exceptions.DestinationNotFoundException;
import com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException;
import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.email.DestinationEmailTemplates;
import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.exceptions.EmailSendException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public Page<DestinationResponse> getAllDestinations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Destination> destinations = destinationRepository.findAll(pageable);
        return destinations.map(DestinationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public DestinationResponse getDestinationById(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException(id));
        return DestinationMapper.toResponse(destination);
    }

    @Transactional(readOnly = true)
    public Page<DestinationResponse> getDestinationsWithFilters(DestinationFilterRequest filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Destination> destinations;

        if (filter.location() != null && !filter.location().trim().isEmpty() &&
                filter.title() != null && !filter.title().trim().isEmpty()) {
            destinations = destinationRepository.findByLocationAndTitleContainingIgnoreCase(
                    filter.location().trim(), filter.title().trim(), pageable);
        } else if (filter.location() != null && !filter.location().trim().isEmpty()) {
            destinations = destinationRepository.findByLocationContainingIgnoreCase(
                    filter.location().trim(), pageable);
        } else if (filter.title() != null && !filter.title().trim().isEmpty()) {
            destinations = destinationRepository.findByTitleContainingIgnoreCase(
                    filter.title().trim(), pageable);
        } else {
            destinations = destinationRepository.findAll(pageable);
        }
        return destinations.map(DestinationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DestinationResponse> getUserDestinations(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Destination> destinations = destinationRepository.findByUser(user, pageable);
        return destinations.map(DestinationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DestinationResponse> getUserDestinations(User user, int page, int size, String sort) {
        Pageable pageable;
        if ("asc".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by("createdAt").ascending());
        } else if ("desc".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by("createdAt").descending());
        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<Destination> destinations = destinationRepository.findByUser(user, pageable);
        return destinations.map(DestinationMapper::toResponse);
    }

    public DestinationResponse createDestination(User user, DestinationRequest request) throws IOException {
        Map uploadResult = cloudinaryService.uploadFile(request.image());
        String imageUrl = (String) uploadResult.get("secure_url");
        Destination destination = DestinationMapper.toEntity(request, imageUrl);
        destination.setUser(user);
        Destination savedDestination = destinationRepository.save(destination);
        DestinationResponse response = DestinationMapper.toResponse(savedDestination);

        try {
            String subject = DestinationEmailTemplates.getDestinationCreatedSubject();
            String plainText = DestinationEmailTemplates.getDestinationCreatedPlainText(user, response);
            String htmlContent = DestinationEmailTemplates.getDestinationCreatedHtml(user, response);

            emailService.sendDestinationCreatedEmail(user.getEmail(), subject, plainText, htmlContent);
        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send confirmation email: " + e.getMessage(), e);
        }
        return response;
    }

    public DestinationResponse updateDestination(Long id, User user, DestinationRequest request) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException(id));

        if (!isAuthorizedToModify(destination, user)) {
            throw new UnauthorizedAccessException(id);
        }
        destination.setTitle(request.title());
        destination.setLocation(request.location());
        destination.setDescription(request.description());
        postImageCloudinary(request, destination);

        return DestinationMapper.toResponse(destination);

    }

    public void deleteDestination(Long id, User user) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException(id));
        if (!isAuthorizedToModify(destination, user)) {
            throw new UnauthorizedAccessException(id);
        }

        String imageUrl = destination.getImageUrl();

        String withoutPrefix = imageUrl.substring(imageUrl.indexOf("/upload/") + 8);
        if (withoutPrefix.matches("v\\d+/.+")) {
            withoutPrefix = withoutPrefix.substring(withoutPrefix.indexOf('/') + 1);
        }
        int dotIndex = withoutPrefix.lastIndexOf('.');
        String publicId = (dotIndex != -1) ? withoutPrefix.substring(0, dotIndex) : withoutPrefix;

        deleteImageCloudinary(publicId);
        destinationRepository.delete(destination);
    }

    private boolean isAuthorizedToModify(Destination destination, User user) {
        return destination.getUser().getId().equals(user.getId());
    }

    private void postImageCloudinary(DestinationRequest request, Destination destination) {
        try {
            Map uploadResult = cloudinaryService.uploadFile(request.image());
            String imageUrl = (String) uploadResult.get("secure_url");
            destination.setImageUrl(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }
    }

    private void deleteImageCloudinary(String publicId) {
        try {
            cloudinaryService.deleteFile(publicId);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting image from Cloudinary: " + e.getMessage());
        }
    }
}

package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.destination.dto.DestinationFilterRequest;
import com.DreamFactory.DF.destination.dto.DestinationMapper;
import com.DreamFactory.DF.destination.dto.DestinationRequest;
import com.DreamFactory.DF.destination.dto.DestinationResponse;
import com.DreamFactory.DF.destination.exceptions.DestinationNotFoundException;
import com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException;
import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.email.EmailTemplates;
import com.DreamFactory.DF.user.model.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final EmailService emailService;

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

    public DestinationResponse createDestination(User user, DestinationRequest request) {
        Destination destination = DestinationMapper.toEntity(request);
        destination.setUser(user);
        Destination savedDestination = destinationRepository.save(destination);
        DestinationResponse response = DestinationMapper.toResponse(savedDestination);

        try {
            String subject = EmailTemplates.getDestinationCreatedSubject();
            String plainText = EmailTemplates.getDestinationCreatedPlainText(user, response);
            String htmlContent = EmailTemplates.getDestinationCreatedHtml(user, response);

            emailService.sendDestinationCreatedEmail(user.getEmail(), subject, plainText, htmlContent);
        } catch (MessagingException e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
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
        destination.setImageUrl(request.imageUrl());

        return DestinationMapper.toResponse(destination);

    }

    public void deleteDestination(Long id, User user) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException(id));
        if (!isAuthorizedToModify(destination, user)) {
            throw new UnauthorizedAccessException(id);
        }
        destinationRepository.delete(destination);
    }

    private boolean isAuthorizedToModify(Destination destination, User user) {
        return destination.getUser().getId().equals(user.getId());
    }
}

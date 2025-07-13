package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.destination.dto.DestinationFilterRequest;
import com.DreamFactory.DF.destination.dto.DestinationMapper;
import com.DreamFactory.DF.destination.dto.DestinationRequest;
import com.DreamFactory.DF.destination.dto.DestinationResponse;
import com.DreamFactory.DF.destination.exceptions.DestinationNotFoundException;
import com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException;
import com.DreamFactory.DF.user.model.Role;
import com.DreamFactory.DF.user.model.User;
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

    public Page<DestinationResponse> getAllDestinations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Destination> destinations = destinationRepository.findAll(pageable);
        return destinations.map(DestinationMapper::toResponse);
    }

    public DestinationResponse getDestinationById(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new DestinationNotFoundException(id));
        return DestinationMapper.toResponse(destination);
    }

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

    public Page<DestinationResponse> getUserDestinations(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Destination> destinations = destinationRepository.findByUser(user, pageable);
        return destinations.map(DestinationMapper::toResponse);
    }

    public DestinationResponse createDestination(User user, DestinationRequest request) {
        Destination destination = DestinationMapper.toEntity(request);
        destination.setUser(user);
        Destination savedDestination = destinationRepository.save(destination);
        return DestinationMapper.toResponse(savedDestination);
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

        Destination updatedDestination = destinationRepository.save(destination);
        return DestinationMapper.toResponse(updatedDestination);

    }

    private boolean isAuthorizedToModify(Destination destination, User user) {
        return destination.getUser().getId().equals(user.getId());
    }
}

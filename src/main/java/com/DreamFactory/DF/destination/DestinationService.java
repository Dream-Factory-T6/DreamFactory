package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.destination.dto.DestinationMapper;
import com.DreamFactory.DF.destination.dto.DestinationResponse;
import com.DreamFactory.DF.destination.exceptions.DestinationNotFoundException;
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
}

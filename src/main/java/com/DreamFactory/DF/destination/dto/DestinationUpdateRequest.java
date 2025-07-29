package com.DreamFactory.DF.destination.dto;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record DestinationUpdateRequest(
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters") String title,

        @Size(min = 3, max = 100, message = "Location must be between 3 and 100 characters") String location,

        @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters") String description,

        MultipartFile image) {
}
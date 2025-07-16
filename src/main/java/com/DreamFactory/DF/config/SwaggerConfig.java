package com.DreamFactory.DF.config;

import com.DreamFactory.DF.swagger.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Map;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("My API")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))

                        .addSchemas("ErrorResponse", new Schema<ErrorResponse>()
                                .$ref("ErrorResponse"))

                        .addResponses("BadRequest", apiResponse(400, "Invalid input or malformed request"))
                        .addResponses("Unauthorized", apiResponse(401, "Authentication required"))
                        .addResponses("Forbidden", apiResponse(403, "You do not have permission to access this resource"))
                        .addResponses("NotFound", apiResponse(404, "Requested resource not found"))
                        .addResponses("DestinationNotFound", apiResponse(404, "Destination not found."))
                        .addResponses("ReviewNotFound", apiResponse(404, "Review not found."))
                        .addResponses("UserNotFound", apiResponse(404, "User not found."))
                        .addResponses("InternalServerError", apiResponse(500, "Internal server error"))
                        .addResponses("NoContent", new ApiResponse()
                                .description("Successfully processed request with no content")));
    }

    private ApiResponse apiResponse(int status, String message) {
        return new ApiResponse()
                .description(message)
                .content(jsonError(status, message));
    }

    private Content jsonError(int status, String message) {
        return new Content().addMediaType("application/json",
                new MediaType()
                        .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                        .example(Map.of("status", status, "message", message)));
    }
}

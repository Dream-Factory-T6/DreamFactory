package com.DreamFactory.DF.config;

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
        final String securitySchemeName = "basicAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("My API")
                        .version("1.0.0")
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                        )
                        .addResponses("BadRequest", new ApiResponse()
                                .description("Invalid input or malformed request")
                                .content(jsonError(400, "Invalid input or malformed request")))

                        .addResponses("Unauthorized", new ApiResponse()
                                .description("Authentication required")
                                .content(jsonError(401, "Authentication required")))

                        .addResponses("Forbidden", new ApiResponse()
                                .description("You do not have permission to access this resource")
                                .content(jsonError(403, "You do not have permission to access this resource")))

                        .addResponses("NotFound", new ApiResponse()
                                .description("Requested resource not found")
                                .content(jsonError(404,"Requested resource not found")))

                        .addResponses("CartNotFound", new ApiResponse()
                                .description("Cart for user not found.")
                                .content(jsonError(404,"Cart for user not found.")))

                        .addResponses("ProductNotFound", new ApiResponse()
                                .description("Product not found.")
                                .content(jsonError(404, "Product not found.")))

                        .addResponses("CategoryNotFound", new ApiResponse()
                                .description("Category not found.")
                                .content(jsonError(404, "Category not found.")))

                        .addResponses("UserNotFound", new ApiResponse()
                                .description("User not found.")
                                .content(jsonError(404, "User not found.")))

                        .addResponses("InternalServerError", new ApiResponse()
                                .description("Internal server error")
                                .content(jsonError(500, "Internal server error")))

                        .addResponses("NoContent", new ApiResponse()
                                .description("Successfully processed request with no content"))
                );
    }

    private Content jsonError(int status, String message) {
        return new Content().addMediaType("application/json",
                new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                        .example(Map.of(
                                "status", status,
                                "message", message
                        )));
    }
}

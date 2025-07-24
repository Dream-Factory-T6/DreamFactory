
package com.DreamFactory.DF.user;

import com.DreamFactory.DF.auth.AuthServiceHelper;
import com.DreamFactory.DF.user.dto.adminRole.UserRequestUpdateAdmin;
import com.DreamFactory.DF.user.dto.userRole.UserRequest;
import com.DreamFactory.DF.user.dto.adminRole.UserRequestAdmin;
import com.DreamFactory.DF.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "User", description = "Operations related to user")
public class UserController {

    private UserService userService;
    private AuthServiceHelper authServiceHelper;


    @GetMapping("/api/users")
    @Operation(summary = "Get all users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users returned successfully"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }



    @GetMapping("/api/users/{id}")
    @Operation(summary = "Get user by Id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User returned successfully"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/UserNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<UserResponse> getUserById(@Parameter @PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/auth/refresh")
    @Operation(summary = "Refresh access token using refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
                    @ApiResponse(responseCode = "400", description = "Missing token"),
                    @ApiResponse(responseCode = "401", description = "Invalid refresh token")
            })
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> body) {
        return authServiceHelper.handleRefreshToken(body.get("refreshToken"));
    }


    @PostMapping("/register")
    @Operation(summary = "Create a new user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "409", description = "Conflict with username or email"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse registeredUser = userService.registerUser(request);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/register/admin")
    @Operation(summary = "Create new user by user with role ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "409", description = "Conflict with username or email"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<UserResponse> registerUserAdmin(@Valid @RequestBody UserRequestAdmin request) {
        try {
            UserResponse registeredUser = userService.registerUserByAdmin(request);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/api/users/{id}")
    @Operation(summary = "Update user by user with role ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<UserResponse> updateUserRoleRole(@Parameter @PathVariable Long id, @Valid @RequestBody UserRequestUpdateAdmin request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/api/users/{id}")
    @Operation(summary = "Get all reviews by user.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted and reviews and destinations published by the user successfully deleted"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/UserNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<String> deleteUserById(@Parameter(description = "User ID you want to delete") @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User with id " + id + " has been deleted", HttpStatus.NO_CONTENT);
    }
}


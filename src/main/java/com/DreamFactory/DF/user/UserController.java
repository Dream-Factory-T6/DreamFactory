package com.DreamFactory.DF.user;

import com.DreamFactory.DF.user.dto.UserRequest;
import com.DreamFactory.DF.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse registeredUser = userService.registerUser(request);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> updateUser(@Parameter @PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<String> deleteUserById(@Parameter(description = "User ID you want to delete") @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User with id " + id + " has been deleted", HttpStatus.NO_CONTENT);
    }
}

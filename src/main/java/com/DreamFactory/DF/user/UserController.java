package com.DreamFactory.DF.user;

import com.DreamFactory.DF.user.dto.UserRequest;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.user.model.Role;
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


    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@Parameter @PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
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

    @PutMapping("/api/users/role/string/{id}")
    public ResponseEntity<UserResponse> updateUserStringRole(@Parameter @PathVariable Long id, @Valid @RequestBody UserRequest request, @RequestBody String role) {
        return ResponseEntity.ok(userService.updateUserString(id, request, role));
    }

    @PutMapping("/api/users/role/role/{id}")
    public ResponseEntity<UserResponse> updateUserRoleRole(@Parameter @PathVariable Long id, @Valid @RequestBody UserRequest request, @RequestBody Role role) {
        return ResponseEntity.ok(userService.updateUserRole(id, request, role));
    }

    @PutMapping("/api/users/role/{id}")
    public ResponseEntity<UserResponse> updateUserRoleByRole(@Parameter @PathVariable Long id, @Valid @RequestBody UserRequest request, @RequestBody Role role) {
        return ResponseEntity.ok(userService.updateUserRoleByRole(id, role));
    }

    @PutMapping("/api/users/string/{id}")
    public ResponseEntity<UserResponse> updateUserRoleByString(@Parameter @PathVariable Long id, @Valid @RequestBody UserRequest request, @RequestBody String role) {
        return ResponseEntity.ok(userService.updateUserRoleByString(id, role));
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<String> deleteUserById(@Parameter(description = "User ID you want to delete") @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User with id " + id + " has been deleted", HttpStatus.NO_CONTENT);
    }
}

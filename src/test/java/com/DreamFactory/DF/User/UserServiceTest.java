package com.DreamFactory.DF.User;

import com.DreamFactory.DF.user.UserRepository;
import com.DreamFactory.DF.user.UserService;
import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.UserRequest;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void should_loginExistingUser_fromRequest(){
        UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");
        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        when(userRepository.findByUsername("userTest")).thenReturn(Optional.of(userSaved));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        UserDetails userLogExpected = new org.springframework.security.core.userdetails.User(
                "userTest",
                "usertest@test.com",
                true,
                true,
                true,
                true,
                authorities);

        UserDetails userLogResponse = userService.loadUserByUsername("userTest");

        assertEquals(userLogExpected, userLogResponse);

    }

    @Test
    void should_loginExistingUser_throw_exception(){

        when(userRepository.findByUsername("userTest")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("userTest") );
    }

    @Test
    void should_registerNewUser_fromRequest(){
        UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

        when(userRepository.findByUsername("userTest")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("usertest@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2");

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        when(userRepository.save(any(User.class))).thenReturn(userSaved);

        UserResponse userResponse = userService.registerUser(userRequest);

        assertEquals("userTest", userResponse.username());
        assertEquals("usertest@test.com", userResponse.email());

    }


    @Test
    void should_registerNewUser_throw_exceptionUsername(){

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

        when(userRepository.findByUsername("userTest")).thenReturn(Optional.of(userSaved));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(userRequest));
        assertEquals("Username already exist", exception.getMessage());

    }

    @Test
    void should_registerNewUser_throw_exceptionEmail(){

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

        when(userRepository.findByEmail("usertest@test.com")).thenReturn(Optional.of(userSaved));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(userRequest));
        assertEquals("Email already exist", exception.getMessage());

    }

    @Test
    void should_getAllUsers() {
        User userSaved1 = new User();
        userSaved1.setId(1L);
        userSaved1.setUsername("adminTest");
        userSaved1.setEmail("adminTest@test.com");
        userSaved1.setPassword("password123");
        userSaved1.setRoles(Set.of(Role.ADMIN));

        User userSaved2 = new User();
        userSaved2.setId(2L);
        userSaved2.setUsername("userTest");
        userSaved2.setEmail("usertest@test.com");
        userSaved2.setPassword("password123");
        userSaved2.setRoles(Set.of(Role.USER));

        when(userRepository.findAll()).thenReturn(List.of(userSaved1,userSaved2));

        List<UserResponse> expectedList = List.of(UserMapper.fromEntity(userSaved1), UserMapper.fromEntity(userSaved2));

        List<UserResponse> responseList = userService.getAllUsers();

        assertEquals(expectedList, responseList);
    }

    @Test
    void should_updateUserById_fromRequest(){
        User userSaved1 = new User();
        userSaved1.setId(1L);
        userSaved1.setUsername("userTest");
        userSaved1.setEmail("usertest@test.com");
        userSaved1.setPassword("password123");
        userSaved1.setRoles(Set.of(Role.USER));

        User userSaved2 = new User();
        userSaved2.setId(1L);
        userSaved2.setUsername("userTest2");
        userSaved2.setEmail("usertest2@test.com");
        userSaved2.setPassword("password123");
        userSaved2.setRoles(Set.of(Role.USER));

        UserResponse userResponseExpected = UserMapper.fromEntity(userSaved2);

        UserRequest userRequest = new UserRequest("userTest2", "usertest2@test.com", "password123");



        when(userRepository.findById(1L)).thenReturn(Optional.of(userSaved1));
        when(userRepository.findByUsername("userTest2")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("usertest2@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2");

        when(userRepository.save(any(User.class))).thenReturn(userSaved2);

        UserResponse userResponse = userService.updateUser(1L, userRequest);

        assertEquals(userResponseExpected, userResponse);
    }

    @Test
    void should_updateUser_throws_exceptionId(){
        UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userRequest) );

        assertEquals("Not user with this ID", exception.getMessage());
    }

    @Test
    void should_updateUser_throws_exceptionNullRequest(){
        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        when(userRepository.findById(1L)).thenReturn(Optional.of(userSaved));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, null) );

        assertEquals("The request is not valid", exception.getMessage());
    }

    @Test
    void should_updateUser_throws_exceptionUsername(){
        UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        when(userRepository.findById(1L)).thenReturn(Optional.of(userSaved));
        when(userRepository.findByUsername("userTest")).thenReturn(Optional.of(userSaved));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userRequest) );
        assertEquals("Username already exist", exception.getMessage());
    }

    @Test
    void should_updateUser_throws_exceptionEmail(){
        UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        when(userRepository.findById(1L)).thenReturn(Optional.of(userSaved));
        when(userRepository.findByUsername("userTest")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("usertest@test.com")).thenReturn(Optional.of(userSaved));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userRequest) );
        assertEquals("Email already exist", exception.getMessage());
    }

    @Test
    void  should_deleteUser_fromId(){

        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void  should_deleteUser_throw_exceptionId(){

        when(userRepository.existsById(1L)).thenReturn(false);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("User id not found", exception.getMessage());
    }

}

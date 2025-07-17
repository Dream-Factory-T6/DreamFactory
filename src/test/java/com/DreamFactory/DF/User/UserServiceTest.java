package com.DreamFactory.DF.User;

import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.exceptions.EmptyListException;
import com.DreamFactory.DF.user.UserRepository;
import com.DreamFactory.DF.user.UserService;
import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.UserRequest;
import com.DreamFactory.DF.user.dto.UserRequestAdmin;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.user.exceptions.EmailAlreadyExistException;
import com.DreamFactory.DF.user.exceptions.UserIdNotFoundException;
import com.DreamFactory.DF.user.exceptions.UsernameAlreadyExistException;
import com.DreamFactory.DF.user.model.User;
import jakarta.mail.MessagingException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private EmailService emailService;

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
    void should_registerNewUser_fromRequest() throws MessagingException {
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
        doNothing().when(emailService).sendUserWelcomeEmail(
                anyString(), anyString(), anyString(), anyString());

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
        assertEquals(new UsernameAlreadyExistException(userRequest.username()).getMessage(), exception.getMessage());

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
        assertEquals(new EmailAlreadyExistException(userRequest.email()).getMessage(), exception.getMessage());

    }

    @Test
    void should_registerNewUserByAdmin_fromRequest() throws MessagingException {
        UserRequestAdmin userRequest = new UserRequestAdmin("userTest", "usertest@test.com", "password123", Role.ADMIN);

        when(userRepository.findByUsername("userTest")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("usertest@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2");

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.ADMIN));

        when(userRepository.save(any(User.class))).thenReturn(userSaved);
        doNothing().when(emailService).sendUserWelcomeEmail(
                anyString(), anyString(), anyString(), anyString());

        UserResponse userResponse = userService.registerUserByAdmin(userRequest);

        assertEquals("userTest", userResponse.username());
        assertEquals("usertest@test.com", userResponse.email());

    }

    @Test
    void should_registerNewUserByAdmin_throw_exceptionUsername(){

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        UserRequestAdmin userRequest = new UserRequestAdmin("userTest", "usertest@test.com", "password123", Role.USER);

        when(userRepository.findByUsername("userTest")).thenReturn(Optional.of(userSaved));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUserByAdmin(userRequest));
        assertEquals(new UsernameAlreadyExistException(userRequest.username()).getMessage(), exception.getMessage());

    }

    @Test
    void should_registerNewUserByAdmin_throw_exceptionEmail(){

        User userSaved = new User();
        userSaved.setId(1L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        UserRequestAdmin userRequest = new UserRequestAdmin("userTest", "usertest@test.com", "password123", Role.ADMIN);

        when(userRepository.findByEmail("usertest@test.com")).thenReturn(Optional.of(userSaved));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUserByAdmin(userRequest));
        assertEquals(new EmailAlreadyExistException(userRequest.email()).getMessage(), exception.getMessage());

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
    void should_getAllUsers_throws_emptyListException() {
        when(userRepository.findAll()).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getAllUsers());
        assertEquals(new EmptyListException().getMessage(), exception.getMessage());
    }

    @Test
    void should_getUserById() {

        User userSaved = new User();
        userSaved.setId(2L);
        userSaved.setUsername("userTest");
        userSaved.setEmail("usertest@test.com");
        userSaved.setPassword("password123");
        userSaved.setRoles(Set.of(Role.USER));

        when(userRepository.findById(2L)).thenReturn(Optional.of(userSaved));

        UserResponse expectedUser = UserMapper.fromEntity(userSaved);

        UserResponse responseUser = userService.getUserById(2L);

        assertEquals(expectedUser, responseUser);
    }

    @Test
    void should_getUserById_throw_exception() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(2L));
        assertEquals(new UserIdNotFoundException(2L).getMessage(), exception.getMessage());

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

        UserRequestAdmin userRequest = new UserRequestAdmin("userTest2", "usertest2@test.com", "password123", Role.USER);



        when(userRepository.findById(1L)).thenReturn(Optional.of(userSaved1));
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2");


        UserResponse userResponse = userService.updateUser(1L, userRequest);

        assertEquals(userResponseExpected, userResponse);
    }

    @Test
    void should_updateUser_throws_exceptionId(){
        UserRequestAdmin userRequest = new UserRequestAdmin("userTest", "usertest@test.com", "password123", Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userRequest) );

        assertEquals(new UserIdNotFoundException(1L).getMessage(), exception.getMessage());
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
        assertEquals(new UserIdNotFoundException(1L).getMessage(), exception.getMessage());
    }

}

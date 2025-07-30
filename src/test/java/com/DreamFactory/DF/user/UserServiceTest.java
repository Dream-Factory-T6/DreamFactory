package com.DreamFactory.DF.user;

import com.DreamFactory.DF.exceptions.EmailSendException;
import com.DreamFactory.DF.exceptions.EmptyListException;
import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.adminRole.UserRequestUpdateAdmin;
import com.DreamFactory.DF.user.dto.userRole.UserRequest;
import com.DreamFactory.DF.user.dto.adminRole.UserRequestAdmin;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.user.exceptions.EmailAlreadyExistException;
import com.DreamFactory.DF.user.exceptions.UserIdNotFoundException;
import com.DreamFactory.DF.user.exceptions.UsernameAlreadyExistException;
import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.user.utils.UserServiceHelper;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserServiceHelper userServiceHelper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    class LoginUserTest {

        @Test
        void should_loginExistingUser_fromRequest(){
            UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");
            User userSaved = new User();
            userSaved.setId(1L);
            userSaved.setUsername("userTest");
            userSaved.setEmail("usertest@test.com");
            userSaved.setPassword("password123");
            userSaved.setRoles(Set.of(Role.USER));

            when(userServiceHelper.getUserLogin("userTest")).thenReturn(Optional.of(userSaved));

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

            when(userServiceHelper.getUserLogin("userTest"))
                    .thenThrow(new UsernameNotFoundException("userTest does not exist."));

            assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("userTest") );
        }

        @Test
        void should_registerNewUser_fromRequest() {
            UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

            doNothing().when(userServiceHelper).checkUsername(userRequest.username());
            doNothing().when(userServiceHelper).checkEmail(userRequest.email());

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
    }

    @Nested
    class RegisterNewUserTest {

        @Test
        void should_registerNewUser_fromRequest(){
            UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

            doNothing().when(userServiceHelper).checkUsername(userRequest.username());
            doNothing().when(userServiceHelper).checkEmail(userRequest.email());

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

            doThrow(new UsernameAlreadyExistException(userRequest.username()))
                    .when(userServiceHelper).checkUsername(userRequest.username());

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

            doThrow(new EmailAlreadyExistException(userRequest.email()))
                    .when(userServiceHelper).checkEmail(userRequest.email());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(userRequest));
            assertEquals(new EmailAlreadyExistException(userRequest.email()).getMessage(), exception.getMessage());
        }

        @Test
        void should_registerNewUser_throw_exceptionEmailSendingFails() throws IOException, MessagingException {
            UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

            doNothing().when(userServiceHelper).checkUsername(userRequest.username());
            doNothing().when(userServiceHelper).checkEmail(userRequest.email());

            doThrow(new EmailSendException("Failed to send welcome email: Email error"))
                    .when(userServiceHelper).sendEmailRegisterNewUser(any());

            assertThatThrownBy(() -> userService.registerUser(userRequest))
                    .isInstanceOf(EmailSendException.class)
                    .hasMessageContaining("Failed to send welcome email:");
        }

        @Test
        void should_RegisterNewUser_throw_dataIntegrityViolationException() throws Exception {
            UserRequest userRequest = new UserRequest("userTest", "usertest@test.com", "password123");

            doNothing().when(userServiceHelper).checkUsername(userRequest.username());
            doNothing().when(userServiceHelper).checkEmail(userRequest.email());

            when(userRepository.save(any(User.class)))
                    .thenThrow(new DataIntegrityViolationException("Username or email already exists"));

            DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userService.registerUser(userRequest));
            assertEquals("Username or email already exists", exception.getMessage());
        }
    }

    @Nested
    class RegisterNewUserByAdminTest {

        @Test
        void should_registerNewUserByAdmin_fromRequest() throws MessagingException {
            UserRequestAdmin userRequest = new UserRequestAdmin("userTest", "usertest@test.com", "password123", Role.ADMIN);

            doNothing().when(userServiceHelper).checkUsername(userRequest.username());
            doNothing().when(userServiceHelper).checkEmail(userRequest.email());

            User userSaved = new User();
            userSaved.setId(1L);
            userSaved.setUsername("userTest");
            userSaved.setEmail("usertest@test.com");
            userSaved.setPassword("password123");
            userSaved.setRoles(Set.of(Role.ADMIN));

            when(userRepository.save(any(User.class))).thenReturn(userSaved);

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

            doThrow(new UsernameAlreadyExistException(userRequest.username()))
                    .when(userServiceHelper).checkUsername(userRequest.username());

            UsernameAlreadyExistException exception = assertThrows(UsernameAlreadyExistException.class, () -> userService.registerUserByAdmin(userRequest));
            assertEquals(new UsernameAlreadyExistException(userRequest.username()).getMessage(), exception.getMessage());
            verify(userServiceHelper).checkUsername(userRequest.username());
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

            doThrow(new EmailAlreadyExistException(userRequest.email()))
                    .when(userServiceHelper).checkEmail(userRequest.email());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUserByAdmin(userRequest));
            assertEquals(new EmailAlreadyExistException(userRequest.email()).getMessage(), exception.getMessage());
        }

        @Test
        void should_registerNewUserByAdmin_throw_exceptionEmailSendingFails() throws IOException, MessagingException {
            UserRequestAdmin userRequest = new UserRequestAdmin("userTest", "usertest@test.com", "password123", Role.ADMIN);
            doThrow(new EmailSendException("Failed to send welcome email: Email error"))
                    .when(userServiceHelper).sendEmailRegisterNewUser(any());

            assertThatThrownBy(() -> userService.registerUserByAdmin(userRequest))
                    .isInstanceOf(EmailSendException.class)
                    .hasMessageContaining("Failed to send welcome email:");
        }

        @Test
        void should_RegisterNewUserByAdmin_throw_dataIntegrityViolationException() throws Exception {
            UserRequestAdmin userRequest = new UserRequestAdmin("userTest", "usertest@test.com", "password123", Role.USER);

            doNothing().when(userServiceHelper).checkUsername(userRequest.username());
            doNothing().when(userServiceHelper).checkEmail(userRequest.email());

            when(userRepository.save(any(User.class)))
                    .thenThrow(new DataIntegrityViolationException("Username or email already exists"));

            DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> userService.registerUserByAdmin(userRequest));
            assertEquals("Username or email already exists", exception.getMessage());
        }
    }

    @Nested
    class GetAllUsersTest {

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

            List<UserResponse> expectedList = List.of(UserMapper.fromEntity(userSaved1), UserMapper.fromEntity(userSaved2));

            when(userServiceHelper.getAllUserResponseList()).thenReturn(expectedList);

            List<UserResponse> responseList = userService.getAllUsers();

            assertEquals(expectedList, responseList);
        }

        @Test
        void should_getAllUsers_throws_emptyListException() {
            doThrow(new EmptyListException()).
                    when(userServiceHelper).getAllUserResponseList();

            RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getAllUsers());
            assertEquals(new EmptyListException().getMessage(), exception.getMessage());
        }
    }

    @Nested
    class GetUserByIdTest {

        @Test
        void should_getUserById() {
            User userSaved = new User();
            userSaved.setId(2L);
            userSaved.setUsername("userTest");
            userSaved.setEmail("usertest@test.com");
            userSaved.setPassword("password123");
            userSaved.setRoles(Set.of(Role.USER));

            when(userServiceHelper.checkUserId(2L)).thenReturn(userSaved);

            UserResponse expectedUser = UserMapper.fromEntity(userSaved);

            UserResponse responseUser = userService.getUserById(2L);

            assertEquals(expectedUser, responseUser);
        }

        @Test
        void should_getUserById_throw_exception() {
            when(userServiceHelper.checkUserId(2L)).thenThrow(new UserIdNotFoundException(2L));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(2L));
            assertEquals(new UserIdNotFoundException(2L).getMessage(), exception.getMessage());
        }
    }

    @Nested
    class UpdateUserByIdTest {

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

            UserRequestUpdateAdmin userRequest = new UserRequestUpdateAdmin("userTest2", "usertest2@test.com", "password123", Role.USER);

            when(userServiceHelper.checkUserId(1L)).thenReturn(userSaved1);
            lenient().when(passwordEncoder.encode("password123")).thenReturn("$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2");
            doAnswer(invocation -> {
                UserRequestUpdateAdmin req = invocation.getArgument(0);
                User user = invocation.getArgument(1);

                user.setUsername(req.username());
                user.setEmail(req.email());
                user.setPassword("$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2");
                user.setRoles(Set.of(req.role()));

                return null;
            }).when(userServiceHelper).updateUserData(any(), any());

            UserResponse userResponseExpected = UserMapper.fromEntity(userSaved2);
            UserResponse userResponse = userService.updateUser(1L, userRequest);

            assertEquals(userResponseExpected, userResponse);
        }

        @Test
        void should_updateUser_throws_exceptionId(){
            UserRequestUpdateAdmin userRequest = new UserRequestUpdateAdmin("userTest", "usertest@test.com", "password123", Role.USER);

            when(userServiceHelper.checkUserId(1L)).thenThrow(new UserIdNotFoundException(1L));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userRequest) );

            assertEquals(new UserIdNotFoundException(1L).getMessage(), exception.getMessage());
        }
    }

    @Nested
    class DeleteUserByIdTest {

        @Test
        void  should_deleteUser_fromId(){
            User user = new User();
            when(userServiceHelper.checkUserId(1L)).thenReturn(user);

            userService.deleteUser(1L);

            verify(userRepository).deleteById(1L);
        }

        @Test
        void  should_deleteUser_throw_exceptionId(){
            when(userServiceHelper.checkUserId(1L)).thenThrow(new UserIdNotFoundException(1L));

            RuntimeException exception = assertThrows(UserIdNotFoundException.class, () -> userService.deleteUser(1L));
            assertEquals(new UserIdNotFoundException(1L).getMessage(), exception.getMessage());
        }
    }

    @Nested
    class getAuthenticatedUser {

        @Test
        void getAuthenticatedUser_success() {
            User testUser = new User();
            testUser.setId(10L);
            testUser.setUsername("testUser");
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = Mockito.mock(Authentication.class);
            Mockito.when(authentication.isAuthenticated()).thenReturn(true);
            Mockito.when(authentication.getName()).thenReturn("testUser");

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            Mockito.when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

            User user = userService.getAuthenticatedUser();

            assertNotNull(user);
            assertEquals("testUser", user.getUsername());
        }

        @Test
        void getAuthenticatedUser_failure() {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(null);
            SecurityContextHolder.setContext(context);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getAuthenticatedUser());
            assertEquals("No authenticated user found", exception.getMessage());
        }

        @Test
        void getAuthenticatedUser_notAuthenticated() {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = Mockito.mock(Authentication.class);
            Mockito.when(authentication.isAuthenticated()).thenReturn(false);

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getAuthenticatedUser());
            assertEquals("No authenticated user found", exception.getMessage());
        }
    }
}

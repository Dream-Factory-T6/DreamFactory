package com.DreamFactory.DF.user.utils;

import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.email.UserEmailTemplates;
import com.DreamFactory.DF.exceptions.EmailSendException;
import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.user.UserRepository;
import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.user.dto.adminRole.UserRequestUpdateAdmin;
import com.DreamFactory.DF.user.exceptions.EmailAlreadyExistException;
import com.DreamFactory.DF.user.exceptions.UserIdNotFoundException;
import com.DreamFactory.DF.user.exceptions.UsernameAlreadyExistException;
import com.DreamFactory.DF.user.model.User;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceHelperTest {

    @InjectMocks
    private UserServiceHelper userServiceHelper;


    @Mock
    private  UserRepository userRepository;

    @Mock
    private  PasswordEncoder passwordEncoder;

    @Mock
    private  EmailService emailService;

    @Nested
    class checkEmail {

        @Test
        void when_checkEmail_return_void() {
            when(userRepository.findByEmail("email@test.com")).thenReturn(Optional.empty());

            assertDoesNotThrow(() ->userServiceHelper.checkEmail("email@test.com"));

            verify(userRepository).findByEmail("email@test.com");
        }

        @Test
        void when_checkEmail_throw_EmailAlreadyExistException() {
            when(userRepository.findByEmail("email@test.com")).thenReturn(Optional.of(new User()));

            assertThrows(EmailAlreadyExistException.class,() ->userServiceHelper.checkEmail("email@test.com"));
        }
    }


    @Nested
    class checkUsername {

        @Test
        void when_checkUsername_return_void() {
            when(userRepository.findByUsername("usernameTest")).thenReturn(Optional.empty());

            assertDoesNotThrow(() ->userServiceHelper.checkUsername("usernameTest"));

            verify(userRepository).findByUsername("usernameTest");
        }

        @Test
        void when_checkUsername_throw_UsernameAlreadyExistException() {
            when(userRepository.findByUsername("usernameTest")).thenReturn(Optional.of(new User()));

            assertThrows(UsernameAlreadyExistException.class,() ->userServiceHelper.checkUsername("usernameTest"));
        }
    }

    @Nested
    class checkUserId {

        @Test
        void when_checkUserId_return_user() {
            User user = new User();
            user.setId(99L);
            user.setUsername("test");
            when(userRepository.findById(99L)).thenReturn(Optional.of(user));

            User result = userServiceHelper.checkUserId(99L);

            assertNotNull(result);
            assertDoesNotThrow(() ->userServiceHelper.checkUserId(99L));
            assertEquals(99L, result.getId());
            assertEquals("test", result.getUsername());
        }

        @Test
        void when_checkUserId_throw_UserIdNotFoundException() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserIdNotFoundException.class, () -> userServiceHelper.checkUserId(99L));

        }
    }

    @Nested
    class getUserLogin {

        @Test
        void when_getUserLogin_return_user() {
            User user = new User();
            user.setId(99L);
            user.setUsername("test");
            when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

            Optional<User> result = userServiceHelper.getUserLogin("test");
            User userResponse = result.get();
            assertNotNull(result);
            assertDoesNotThrow(() ->userServiceHelper.getUserLogin("test"));
            assertEquals(99L, userResponse.getId());
            assertEquals("test", userResponse.getUsername());
        }

        @Test
        void when_getUserLogin_throw_UsernameNotFoundException() {
            when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> userServiceHelper.getUserLogin("test"));

        }
    }


    @Test
    void getEncodePassword() {
        when(passwordEncoder.encode("Password123")).thenReturn("passwordEncoded");

        String result = userServiceHelper.getEncodePassword("Password123");

        assertEquals("passwordEncoded", result);
    }

    @Test
    void getAllUserResponseList() {
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

        when(userRepository.findAll()).thenReturn(List.of(userSaved1, userSaved2));

        List<UserResponse> resultList = userServiceHelper.getAllUserResponseList();

        assertIterableEquals(expectedList, resultList);
    }

    @Nested
    class updateUserData {

        @Test
        void when_updateUserDataAllFields_return_void() {
            User user = new User();
            user.setUsername("oldUsername");
            user.setEmail("old@example.com");
            user.setPassword("oldPassword");
            user.setRoles(Set.of(Role.USER));

            UserRequestUpdateAdmin request = new UserRequestUpdateAdmin(
                    "newUsername",
                    "new@example.com",
                    "newPassword123",
                    Role.ADMIN
            );

            when(passwordEncoder.encode("newPassword123")).thenReturn("encodedPassword123");

            userServiceHelper.updateUserData(request, user);

            assertEquals("newUsername", user.getUsername());
            assertEquals("new@example.com", user.getEmail());
            assertEquals("encodedPassword123", user.getPassword());
            assertTrue(user.getRoles().contains(Role.ADMIN));
        }

        @Test
        void when_updateUserDataOneField_return_void() {
            User user = new User();
            user.setUsername("existingUsername");
            user.setEmail("existing@example.com");
            user.setPassword("existingPassword");
            user.setRoles(Set.of(Role.USER));

            UserRequestUpdateAdmin request = new UserRequestUpdateAdmin(
                    "",
                    null,
                    "",
                    Role.ADMIN
            );

            userServiceHelper.updateUserData(request, user);

            assertEquals("existingUsername", user.getUsername());
            assertEquals("existing@example.com", user.getEmail());
            assertEquals("existingPassword", user.getPassword());
            assertTrue(user.getRoles().contains(Role.ADMIN));
        }

        @Test
        void should_encode_password_only_when_provided() {
            User user = new User();
            user.setPassword("oldPassword");

            UserRequestUpdateAdmin request = new UserRequestUpdateAdmin(
                    null,
                    null,
                    "newSecret",
                    Role.USER
            );

            when(passwordEncoder.encode("newSecret")).thenReturn("newEncoded");

            userServiceHelper.updateUserData(request, user);

            assertEquals("newEncoded", user.getPassword());
        }
    }

    @Nested
    class sendEmailRegisterNewUser {

        @Test
        void when_sendEmailRegisterNewUser_return_void() throws Exception {
            User user = new User();
            user.setEmail("test@example.com");
            user.setUsername("testUser");

            assertDoesNotThrow(() -> userServiceHelper.sendEmailRegisterNewUser(user));

            verify(emailService).sendUserWelcomeEmail(
                    eq("test@example.com"),
                    eq(UserEmailTemplates.getUserCreatedSubject()),
                    eq(UserEmailTemplates.getUserWelcomeEmailPlainText(user)),
                    eq(UserEmailTemplates.getUserWelcomeEmailHtml(user))
            );
        }

        @Test
        void when_sendEmailRegisterNewUser_throw_EmailSendException() throws Exception {
            User user = new User();
            user.setEmail("test@example.com");
            user.setUsername("testUser");

            doThrow(new MessagingException("SMTP error"))
                    .when(emailService)
                    .sendUserWelcomeEmail(
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString()
                    );

            EmailSendException exception = assertThrows(
                    EmailSendException.class,
                    () -> userServiceHelper.sendEmailRegisterNewUser(user)
            );

            assertTrue(exception.getMessage().contains("Failed to send welcome email"));
        }
    }

}
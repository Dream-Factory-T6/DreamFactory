package com.DreamFactory.DF.user.utils;

import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
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

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void checkEmail() {
    }

    @Test
    void checkUsername() {
    }

    @Test
    void checkUserId() {
    }

    @Test
    void getUserLogin() {
    }

    @Test
    void getEncodePassword() {
    }

    @Test
    void sendEmailRegisterNewUser() {
    }

    @Test
    void getAllUserResponseList() {
    }

    @Test
    void updateUserData() {
    }
}
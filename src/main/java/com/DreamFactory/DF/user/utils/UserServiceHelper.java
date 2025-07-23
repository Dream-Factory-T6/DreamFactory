package com.DreamFactory.DF.user.utils;

import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.email.UserEmailTemplates;
import com.DreamFactory.DF.exceptions.EmailSendException;
import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.user.UserRepository;
import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.UserRequestAdmin;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.user.exceptions.EmailAlreadyExistException;
import com.DreamFactory.DF.user.exceptions.UserIdNotFoundException;
import com.DreamFactory.DF.user.exceptions.UsernameAlreadyExistException;
import com.DreamFactory.DF.user.model.User;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserServiceHelper {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceHelper(EmailService emailService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void checkEmail(String request) {
        Optional<User> isExistingEmail = userRepository.findByEmail(request);
        if (isExistingEmail.isPresent()) {
            throw new EmailAlreadyExistException(request);
        }
    }

    public void checkUsername(String request) {
        Optional<User> isExistingUsername = userRepository.findByUsername(request);
        if (isExistingUsername.isPresent()) {
            throw new UsernameAlreadyExistException(request);
        }
    }

    public User checkUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException(id));
        return user;
    }

    public Optional<User> getUserLogin(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(username + " does not exist.");
        }
        return optionalUser;
    }

    public String getEncodePassword(String password) {
        return passwordEncoder.encode(password);
    }



    public void sendEmailRegisterNewUser(User user) {
        try {
            String subject = UserEmailTemplates.getUserCreatedSubject();
            String plainText = UserEmailTemplates.getUserWelcomeEmailPlainText(user);
            String html = UserEmailTemplates.getUserWelcomeEmailHtml(user);

            emailService.sendUserWelcomeEmail(user.getEmail(), subject, plainText, html);
        } catch (MessagingException e){
            throw new EmailSendException("Failed to send welcome email: " + e.getMessage(), e);
        }
    }

    public List<UserResponse> getAllUserResponseList() {
        List<UserResponse> userResponseList = userRepository.findAll()
                .stream()
                .map(UserMapper::fromEntity)
                .collect(Collectors.toList());
        return userResponseList;
    }




    public void updateUserData(UserRequestAdmin request, User user) {
        user.setUsername(request.username());
        user.setEmail(request.email());

        if (request.password() != null && !request.password().isEmpty()) {
            user.setPassword(this.getEncodePassword(request.password()));
        }
        Set<Role> roles = new HashSet<>();
        roles.add(request.role());
        user.setRoles(roles);
    }

}
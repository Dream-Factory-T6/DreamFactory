package com.DreamFactory.DF.user;

import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.email.UserEmailTemplates;
import com.DreamFactory.DF.exceptions.EmailSendException;
import com.DreamFactory.DF.exceptions.EmptyListException;
import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.UserRequest;
import com.DreamFactory.DF.user.dto.UserRequestAdmin;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.user.exceptions.EmailAlreadyExistException;
import com.DreamFactory.DF.user.exceptions.UserIdNotFoundException;
import com.DreamFactory.DF.user.exceptions.UsernameAlreadyExistException;
import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.user.model.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(username + " does not exist.");
        }

        User user = optionalUser.orElseThrow();

        List<GrantedAuthority> authorities = getAuthoritiesRole(user);
        return createUserByUserDetails(user, authorities);
    }

    public UserResponse registerUser(UserRequest request) {
        checkUsername(request.username());
        checkEmail(request.email());

        User user = UserMapper.toEntity(request);
        user.setPassword(getEncodePassword(request.password()));
        user.setRoles(Set.of(Role.USER));

        User savedUser = userRepository.save(user);

        try {
            String subject = UserEmailTemplates.getUserCreatedSubject();
            String plainText = UserEmailTemplates.getUserWelcomeEmailPlainText(user);
            String html = UserEmailTemplates.getUserWelcomeEmailHtml(user);

            emailService.sendUserWelcomeEmail(user.getEmail(), subject, plainText, html);
        } catch (MessagingException e){
            throw new EmailSendException("Failed to send welcome email: " + e.getMessage(), e);
        }

        return UserMapper.fromEntity(savedUser);
    }

    public UserResponse registerUserByAdmin(UserRequestAdmin request) {
        checkUsername(request.username());
        checkEmail(request.email());

        User user = UserMapper.toEntityAdmin(request);
        user.setPassword(getEncodePassword(request.password()));
        user.setRoles(Set.of(request.role()));

        User savedUser = userRepository.save(user);

        try {
            String subject = UserEmailTemplates.getUserCreatedSubject();
            String plainText = UserEmailTemplates.getUserWelcomeEmailPlainText(user);
            String html = UserEmailTemplates.getUserWelcomeEmailHtml(user);

            emailService.sendUserWelcomeEmail(user.getEmail(), subject, plainText, html);
        } catch (MessagingException e){
            throw new EmailSendException("Failed to send welcome email: " + e.getMessage(), e);
        }
        return UserMapper.fromEntity(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponseList = userRepository.findAll()
                .stream()
                .map(UserMapper::fromEntity)
                .collect(Collectors.toList());

        if (userResponseList.isEmpty()){
            throw new EmptyListException();
        }
        return userResponseList;
    }

    public UserResponse getUserById(Long id){
        User user = checkUserId(id);
        return UserMapper.fromEntity(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequestAdmin request) {
        User user = checkUserId(id);

        user.setUsername(request.username());
        user.setEmail(request.email());;
        if (request.password() != null && !request.password().isEmpty()) {
            user.setPassword(getEncodePassword(request.password()));
        }
        Set<Role> roles = new HashSet<>();
        roles.add(request.role());
        user.setRoles(roles);

        return UserMapper.fromEntity(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserIdNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    private static org.springframework.security.core.userdetails.User createUserByUserDetails(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }

    private static List<GrantedAuthority> getAuthoritiesRole(User user) {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    private void checkEmail(String request) {
        Optional<User> isExistingEmail = userRepository.findByEmail(request);
        if (isExistingEmail.isPresent()) {
            throw new EmailAlreadyExistException(request);
        }
    }

    private void checkUsername(String request) {
        Optional<User> isExistingUsername = userRepository.findByUsername(request);
        if (isExistingUsername.isPresent()) {
            throw new UsernameAlreadyExistException(request);
        }
    }

    private User checkUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException(id));
        return user;
    }

    private String getEncodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }
}

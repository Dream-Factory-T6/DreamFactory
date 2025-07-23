package com.DreamFactory.DF.user;

import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.exceptions.EmptyListException;
import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.UserRequest;
import com.DreamFactory.DF.user.dto.UserRequestAdmin;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.role.Role;
import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.user.utils.UserSecurityUtils;
import com.DreamFactory.DF.user.utils.UserServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserServiceHelper userServiceHelper;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userServiceHelper.getUserLogin(username);
        User user = optionalUser.orElseThrow();
        List<GrantedAuthority> authorities = UserSecurityUtils.getAuthoritiesRole(user);

        return UserSecurityUtils.createUserByUserDetails(user, authorities);
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

    public UserResponse registerUser(UserRequest request) {
        userServiceHelper.checkUsername(request.username());
        userServiceHelper.checkEmail(request.email());

        User user = UserMapper.toEntity(request);
        user.setPassword(userServiceHelper.getEncodePassword(request.password()));
        user.setRoles(Set.of(Role.USER));

        User savedUser = userRepository.save(user);
        userServiceHelper.sendEmailRegisterNewUser(user);

        return UserMapper.fromEntity(savedUser);
    }


    public UserResponse registerUserByAdmin(UserRequestAdmin request) {
        userServiceHelper.checkUsername(request.username());
        userServiceHelper.checkEmail(request.email());

        User user = UserMapper.toEntityAdmin(request);
        user.setPassword(userServiceHelper.getEncodePassword(request.password()));
        user.setRoles(Set.of(request.role()));

        User savedUser = userRepository.save(user);

        userServiceHelper.sendEmailRegisterNewUser(user);
        return UserMapper.fromEntity(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        if (userServiceHelper.getAllUserResponseList().isEmpty()){
            throw new EmptyListException();
        }

        return userServiceHelper.getAllUserResponseList();
    }

    public UserResponse getUserById(Long id){
        User user = userServiceHelper.checkUserId(id);
        return UserMapper.fromEntity(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequestAdmin request) {
        User user = userServiceHelper.checkUserId(id);
        userServiceHelper.updateUserData(request, user);
        return UserMapper.fromEntity(user);
    }

    public void deleteUser(Long id) {
        userServiceHelper.checkUserId(id);
        userRepository.deleteById(id);
    }

}

package com.DreamFactory.DF.user;

import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.UserRequest;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.user.model.Role;
import com.DreamFactory.DF.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(username + " does not exist.");
        }

        User user = optionalUser.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }

    public UserResponse registerUser(UserRequest request) {
        Optional<User> isExistingUsername = userRepository.findByUsername(request.username());
        if (isExistingUsername.isPresent()){
            throw new RuntimeException("Username already exist");
        }
        Optional<User> isExistingEmail = userRepository.findByEmail(request.email());
        if (isExistingEmail.isPresent()){
            throw new RuntimeException("Email already exist");
        }
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(Role.USER));
        User savedUser = userRepository.save(user);
        return UserMapper.fromEntity(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponseList = userRepository.findAll()
                .stream()
                .map(UserMapper::fromEntity)
                .collect(Collectors.toList());

        if (userResponseList.isEmpty()){
            throw new RuntimeException("Empty list");
        }
        return userResponseList;
    }

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User id not fpund"));
        return UserMapper.fromEntity(user);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not user with this ID"));

        Optional<User> isExistingUsername = userRepository.findByUsername(request.username());
        if (isExistingUsername.isPresent()){
            throw new RuntimeException("Username already exist");
        }
        Optional<User> isExistingEmail = userRepository.findByEmail(request.email());
        if (isExistingEmail.isPresent()){
            throw new RuntimeException("Email already exist");
        }
        user.setUsername(request.username());
        user.setEmail(request.email());;
        if (request.password() != null && !request.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        User updatedUser = userRepository.save(user);
        return UserMapper.fromEntity(updatedUser);
    }



    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User id not found");
        }
        userRepository.deleteById(id);
    }

}

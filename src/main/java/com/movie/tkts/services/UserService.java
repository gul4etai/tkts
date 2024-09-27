package com.movie.tkts.services;

import com.movie.tkts.dto.UserDto;
import com.movie.tkts.entities.User;
import com.movie.tkts.exception.InvalidPasswordException;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.exception.UserAlreadyExistsException;
import com.movie.tkts.mappers.impl.UserMapperImpl;
import com.movie.tkts.repositories.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final IUserRepository userRepository;
    private final UserMapperImpl userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(IUserRepository userRepository, UserMapperImpl userMapper, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder1;
    }
/*
    public User saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        return userRepository.save(user);
    }*/


/*// Save new user latest not encripted
@Transactional
public UserDto saveUser(UserDto userDto) {
    System.out.println("UserDto before save ");
    User user = userMapper.toEntity(userDto);
    User savedUser = userRepository.save(user);
    return userMapper.toDto(savedUser);  // Return a DTO after saving
}*/



    public UserDto saveUser(UserDto userDto) throws InvalidPasswordException {
        // Check if the username or email already exists
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Convert DTO to entity
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAdmin(userDto.isAdmin());

        // Encode the password
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            throw new InvalidPasswordException("Password cannot be empty");
        }

        // Save the user
        userRepository.save(user);

        // Convert back to DTO and return
        return userMapper.toDto(user);
    }


    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update the fields that need to be updated
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAdmin(userDto.isAdmin());

        // If the password is provided and it's different from the current password, encode it
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
        }

        // Save the updated user
        userRepository.save(user);

        // Convert the updated user back to DTO and return it
        return userMapper.toDto(user);
    }


 /*   @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto); // Convert DTO to Entity
        User savedUser = userRepository.save(user);  // Save the user entity

        // Convert the saved User entity back to a DTO and return it
        return userMapper.toDto(savedUser);
    }*/

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

/*    @Transactional
    public UserDto createUser(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }*/

/*    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update user details based on the provided UserDto
        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword());  // Ensure password is encrypted
        existingUser.setAdmin(userDto.isAdmin());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);  // Return the updated user as DTO
    }*/

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

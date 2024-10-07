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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public UserDto saveUser(UserDto userDto) throws InvalidPasswordException {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAdmin(userDto.isAdmin());

        // password encoding
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            throw new InvalidPasswordException("Password cannot be empty");
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAdmin(userDto.isAdmin());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }

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


    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDto> getMostActiveUsers(String startDate, String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59", formatter);

        List<User> mostActiveUsers = userRepository.findMostActiveUsers(start, end);
        return mostActiveUsers.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getLeastActiveUsers(String startDate, String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59", formatter);


        List<User> leastActiveUsers = userRepository.findLeastActiveUsers(start, end);
        return leastActiveUsers.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}

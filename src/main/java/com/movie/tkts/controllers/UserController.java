package com.movie.tkts.controllers;

import com.movie.tkts.dto.UserDto;
import com.movie.tkts.entities.User;
import com.movie.tkts.exception.InvalidPasswordException;
import com.movie.tkts.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tkts/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) throws InvalidPasswordException {
        return userService.saveUser(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/most-active/{startDate}/{endDate}")
    public ResponseEntity<List<UserDto>> getMostActiveUsers(@PathVariable String startDate, @PathVariable String endDate) {
        List<UserDto> mostActiveUsers = userService.getMostActiveUsers(startDate, endDate);
        return ResponseEntity.ok(mostActiveUsers);
    }

    @GetMapping("/least-active/{startDate}/{endDate}")
    public ResponseEntity<List<UserDto>> getLeastActiveUsers(@PathVariable String startDate, @PathVariable String endDate) {
        List<UserDto> leastActiveUsers = userService.getLeastActiveUsers(startDate, endDate);
        return ResponseEntity.ok(leastActiveUsers);
    }

}

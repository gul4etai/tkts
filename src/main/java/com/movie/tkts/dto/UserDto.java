package com.movie.tkts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String password;
    private String email;
    private String username;
    private boolean isAdmin;
}

package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.UserDto;
import com.movie.tkts.entities.User;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements IMapper<User, UserDto> {

    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = modelMapper.map(user, UserDto.class);
        System.out.println("UserDto: " + userDto);
        return userDto;
    }

    @Override
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return modelMapper.map(userDto, User.class);
    }
}


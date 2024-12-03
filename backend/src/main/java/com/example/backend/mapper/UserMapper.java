package com.example.backend.mapper;


import com.example.backend.dto.SignUpDto;
import com.example.backend.model.User;
import com.example.backend.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel ="spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target="password", ignore = true)
    User signUpToUser(SignUpDto userDto);
}

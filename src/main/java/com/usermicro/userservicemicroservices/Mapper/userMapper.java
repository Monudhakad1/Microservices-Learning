package com.usermicro.userservicemicroservices.Mapper;

import com.usermicro.userservicemicroservices.entity.Dto.UserDto;
import com.usermicro.userservicemicroservices.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface userMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);

}

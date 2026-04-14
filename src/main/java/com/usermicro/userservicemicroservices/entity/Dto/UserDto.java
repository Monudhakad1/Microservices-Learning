package com.usermicro.userservicemicroservices.entity.Dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String name;
    private String email;
    private String about;
    private List<RatingDto> ratings = new ArrayList<>();
}

package com.example.engineer.Model.DTO.User;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOBasicData {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String title;
}

package com.example.engineer.Model.DTO.Team;


import com.example.engineer.Model.DTO.User.UserDTOBasicData;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTOMembers {
    Long id;
    String name;
    Set<UserDTOBasicData> members;
}

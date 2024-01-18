package com.example.engineer.Model.DTO.Issue;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTODashboard {
    Long id;
    String name;
    String description;
}

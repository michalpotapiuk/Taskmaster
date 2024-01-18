package com.example.engineer.Model.DTO.Node;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTOBasicData {
    Long id;
    String title;
    String content;
    Double posX;
    Double posY;
}

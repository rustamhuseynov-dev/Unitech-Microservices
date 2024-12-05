package com.rustam.unitech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDeletedResponse {
    private UUID id;
    private String username;
    private String name;
    private String text;
}

package com.applimax.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class AppUserDTO {
    private String userId;
    private String employeeId;
    private String userName;
    private String password;
    private String email;
}

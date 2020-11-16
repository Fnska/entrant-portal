package ru.ncedu.frolov.entrantportal.web.rest.v1.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String login;
    private String password;
}

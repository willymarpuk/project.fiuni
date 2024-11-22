package com.fiuni.distri.project.fiuni.utils.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class FailedResponseDto {

    private String message;
    private int status;
    private String fieldname;
    private Map<String, String> errors;

    public FailedResponseDto(String message, int status, String fieldname, Map<String, String> errors) {
        this.message = message;
        this.status = status;
        this.fieldname = fieldname;
        this.errors = errors;
    }

}
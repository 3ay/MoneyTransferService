package ru.netology.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Data
@Validated
public class ConfirmOperationRequest {
    private String operationId;

    @Pattern(regexp = "\\d{4,6}", message = "code must contain from 4 to 6 digits")
    private String code;
}

package ru.netology.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Data
@Validated
public class ConfirmOperationRequest {
    @Pattern(regexp = "^\\d{1,18}$", message = "operationId must contain from 1 to 18 digits and no other characters")
    String operationId;

    @Pattern(regexp = "\\d{4,6}", message = "code must contain from 4 to 6 digits")
    String code;
}

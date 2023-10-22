package ru.netology.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class TransferInfoRequest {
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 characters long")
    String cardFromNumber;
    @NotBlank(message = "Expiry date is required")
    @Size(min = 5, max = 5, message = "Expiry date must be 4 characters long")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Invalid expiry date format")
    String cardFromValidTill;
    @NotBlank(message = "CVC is required")
    @Size(min = 3, max = 3, message = "CVC must be 3 digits")
    String cardFromCVV;
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 characters long")
    String cardToNumber;
    @Valid
    Amount amount;
}

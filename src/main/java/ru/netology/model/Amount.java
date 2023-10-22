package ru.netology.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class Amount {
    @NotNull(message = "Value of amount is required")
    @Min(value = 1, message = "Value of amount must be greater than 0")
    int value;
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^(RUB|USD|EUR)$", message = "Currency must be 'RUB', 'USD', or 'EUR'")
    String currency;
    public Amount() {
    }
}

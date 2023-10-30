package ru.netology.dao;

import lombok.Getter;
import lombok.Setter;
import ru.netology.model.Amount;

@Getter
@Setter
public class TransferInfoDAO {
    private String operationId;
    private String cardFromNumber;
    private String cardToNumber;
    private Amount amount;
    private double commission;
}

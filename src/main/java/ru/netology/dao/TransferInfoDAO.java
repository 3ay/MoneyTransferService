package ru.netology.dao;

import lombok.Data;
import ru.netology.model.Amount;

@Data
public class TransferInfoDAO {
    long operationId;
    String cardFromNumber;
    String cardToNumber;
    Amount amount;
    double commission;
}

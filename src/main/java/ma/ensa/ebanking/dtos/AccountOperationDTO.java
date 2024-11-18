package ma.ensa.ebanking.dtos;

import ma.ensa.ebanking.entities.OperationType;

import java.util.Date;

public class AccountOperationDTO {
    private Long id ;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;
}

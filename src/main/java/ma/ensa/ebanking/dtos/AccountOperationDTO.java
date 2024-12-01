package ma.ensa.ebanking.dtos;

import lombok.Data;
import ma.ensa.ebanking.entities.OperationType;

import java.util.Date;
@Data
public class AccountOperationDTO {
    private Long id ;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;
}

package ma.ensa.ebanking.dtos;

import lombok.Data;
import ma.ensa.ebanking.entities.AccountStatus;

@Data

public class SavingBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Data creaTAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interstRate;
}

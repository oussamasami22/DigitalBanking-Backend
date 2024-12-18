package ma.ensa.ebanking.dtos;

import ma.ensa.ebanking.entities.AccountStatus;

import javax.xml.crypto.Data;
@lombok.Data
public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Data creaTAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;

}

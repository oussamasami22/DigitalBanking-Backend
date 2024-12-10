package ma.ensa.ebanking.services.interfaces;

import ma.ensa.ebanking.dtos.*;
import ma.ensa.ebanking.entities.AccountOperation;
import ma.ensa.ebanking.entities.BankAccount;
import ma.ensa.ebanking.entities.Customer;
import ma.ensa.ebanking.exceptions.BalanceNotSufficientException;
import ma.ensa.ebanking.exceptions.BankAccountNotFoundException;
import ma.ensa.ebanking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double inialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance,double interestRate,Long customerId) throws CustomerNotFoundException;


List<CustomerDTO> lisCustomers();
BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
void debit(String accountId,double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
void credit(String accountId,double amount, String description) throws BankAccountNotFoundException;
    void trasnfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccountDTO> bankAccountList();
    CustomerDTO getCustomer(Long customerId) throws  CustomerNotFoundException;
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    List<AccountOperationDTO> accountHistory(String accountId);
    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws  BankAccountNotFoundException;

}
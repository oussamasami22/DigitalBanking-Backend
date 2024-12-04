package ma.ensa.ebanking.services.interfaces;

import ma.ensa.ebanking.entities.BankAccount;
import ma.ensa.ebanking.entities.Customer;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    BankAccount saveBackAccount(double initialBalance, Long customerId, String type);
    List<Customer> listCustomers();
    BankAccount getBankAccount(String accountID);
    void debit(String accountId, double amount, String description );
    void credit(String accountId, double amount, String description );
    void transfer(String accountIdSource,String accountIdDestination,double amount);

}

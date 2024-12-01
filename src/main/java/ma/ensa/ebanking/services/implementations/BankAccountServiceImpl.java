package ma.ensa.ebanking.services.implementations;

import ma.ensa.ebanking.entities.BankAccount;
import ma.ensa.ebanking.entities.Customer;
import ma.ensa.ebanking.repositories.AccountOperationRepository;
import ma.ensa.ebanking.repositories.BankAccountRepository;
import ma.ensa.ebanking.repositories.CustomerRepository;
import ma.ensa.ebanking.services.interfaces.BankAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) {


        return null;
    }

    @Override
    public BankAccount saveBackAccount(double initialBalance, Long customerId, String type) {
        return null;
    }

    @Override
    public List<Customer> listCustumers() {
        return null;
    }

    @Override
    public BankAccount getBankAccount(String accountID) {
        return null;
    }

    @Override
    public void debit(String accountId, double amount, String description) {

    }

    @Override
    public void credit(String accountId, double amount, String description) {

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) {

    }
}

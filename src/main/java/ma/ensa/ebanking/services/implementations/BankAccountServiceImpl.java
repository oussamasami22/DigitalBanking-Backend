package ma.ensa.ebanking.services.implementations;

import ma.ensa.ebanking.dtos.*;
import ma.ensa.ebanking.entities.*;
import ma.ensa.ebanking.exceptions.BalanceNotSufficientException;
import ma.ensa.ebanking.exceptions.BankAccountNotFoundException;
import ma.ensa.ebanking.exceptions.CustomerNotFoundException;
import ma.ensa.ebanking.mappers.BankAccountMapperImpl;
import ma.ensa.ebanking.repositories.AccountOperationRepository;
import ma.ensa.ebanking.repositories.BankAccountRepository;
import ma.ensa.ebanking.repositories.CustomerRepository;
import ma.ensa.ebanking.services.interfaces.BankAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        // Mapper le DTO en entité
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);

        // Sauvegarder l'entité dans la base de données
        Customer savedCustomer = customerRepository.save(customer);

        // Mapper l'entité sauvegardée en DTO et le retourner
        return dtoMapper.fromCustomer(savedCustomer);
    }


    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double inialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null){
            throw  new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setBalance(inialBalance);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException(" Customer not Found !");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> lisCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());

        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException(" BankAccount not Found !"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }
        else{
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return  dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
     BankAccount bankAccount =  bankAccountRepository.findById(accountId)
             .orElseThrow(() -> new BankAccountNotFoundException(" BantAccount not Found !"));
          if(bankAccount.getBalance() < amount){
              throw  new BalanceNotSufficientException("Balance not sufficient !");
          }
          AccountOperation accountOperation = new AccountOperation();
          accountOperation.setOperationType(OperationType.DEBIT);
          accountOperation.setAmount(amount);
          accountOperation.setOperationDate(new Date());
          accountOperation.setDescription(description);
          accountOperation.setBankAccount(bankAccount);
          accountOperationRepository.save(accountOperation);
          bankAccount.setBalance(bankAccount.getBalance());
          bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
   BankAccount bankAccount = bankAccountRepository.findById(accountId)
           .orElseThrow(() -> new BankAccountNotFoundException(" BankAccount not Found !"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance());
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void trasnfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
    debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
    credit(accountIdDestination,amount,"Transform from "+accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream()
                .map(bankAccount -> {
                    if(bankAccount instanceof SavingAccount){
                        SavingAccount savingAccount=(SavingAccount) bankAccount;
                        return dtoMapper.fromSavingBankAccount(savingAccount);
                    }
                    else{
                        CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return dtoMapper.fromCurrentBankAccount(currentAccount);
                    }
                }).collect(Collectors.toList());

        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not Found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return  accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op ->
                dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;

    }
}

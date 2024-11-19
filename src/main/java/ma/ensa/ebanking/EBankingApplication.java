package ma.ensa.ebanking;

import ma.ensa.ebanking.entities.*;
import ma.ensa.ebanking.repositories.AccountOperationRepository;
import ma.ensa.ebanking.repositories.BankAccountRepository;
import ma.ensa.ebanking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingApplication {

    public static void main(String[] args) {

        SpringApplication.run(EBankingApplication.class, args);
    }
    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            // Création et sauvegarde des clients
            Stream.of("oussama", "sami", "sima").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer); // Enregistrement dans la base de données
            });

            // Vérification des données insérées
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(account -> {
                for(int i=0 ; i<5; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setDescription("description");
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setOperationType();
                    accountOperationRepository.save(accountOperation);
                }
                    }
            );

        };
    }


}

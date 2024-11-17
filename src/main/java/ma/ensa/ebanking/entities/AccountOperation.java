package ma.ensa.ebanking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date operationDate;

    private double amount;

    @Enumerated(EnumType.STRING) // Stores enum as a string in the database
    private OperationType operationType;

    @ManyToOne
    private BankAccount bankAccount;

    private String description;


}

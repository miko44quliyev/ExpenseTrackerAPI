package com.example.expencetrackerapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @NotBlank
    @Email(message = "Invalid email")
    private String email;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}

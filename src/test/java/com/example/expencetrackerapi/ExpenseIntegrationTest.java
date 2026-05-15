package com.example.expencetrackerapi;

import com.example.expencetrackerapi.dto.request.CreateExpenseRequest;
import com.example.expencetrackerapi.dto.request.UpdateExpenseRequest;
import com.example.expencetrackerapi.entity.Account;
import com.example.expencetrackerapi.entity.Category;
import com.example.expencetrackerapi.entity.Expense;
import com.example.expencetrackerapi.entity.PaymentMethod;
import com.example.expencetrackerapi.repository.AccountRepository;
import com.example.expencetrackerapi.repository.CategoryRepository;
import com.example.expencetrackerapi.repository.ExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Account account;
    private Category category;

    @BeforeEach
    void setup() {
        expenseRepository.deleteAll();
        accountRepository.deleteAll();
        categoryRepository.deleteAll();

        account = new Account();
        account.setFullName("Test User");
        account.setEmail("test@gmail.com");
        account.setCurrentBalance(new BigDecimal("1000.00"));
        account = accountRepository.save(account);

        category = new Category();
        category.setName("Food");
        category.setDescription("Food expenses");
        category.setAccount(account);
        category = categoryRepository.save(category);
    }

    @Test
    void shouldCreateExpense() throws Exception {

        CreateExpenseRequest request = new CreateExpenseRequest(
                "Market Shopping",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 1, 1),
                PaymentMethod.CASH,
                account.getId(),
                category.getId(),
                null
        );

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Market Shopping"))
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    void shouldUpdateExpense() throws Exception {

        Expense expense = new Expense();
        expense.setTitle("Old Title");
        expense.setAmount(new BigDecimal("100"));
        expense.setExpenseDate(LocalDate.of(2026, 1, 1));
        expense.setPaymentMethod(PaymentMethod.CASH);
        expense.setAccount(account);
        expense.setCategory(category);

        expense = expenseRepository.save(expense);

        UpdateExpenseRequest request = new UpdateExpenseRequest();
        request.setTitle("New Title");
        request.setAmount(new BigDecimal("200"));
        request.setExpenseDate(LocalDate.of(2026, 1, 2));
        request.setPaymentMethod(PaymentMethod.CASH);

        mockMvc.perform(put("/api/expenses/" + expense.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.amount").value(200));
    }

    @Test
    void shouldReturnAllExpenses() throws Exception {

        Expense expense = new Expense();
        expense.setTitle("Test Expense");
        expense.setAmount(new BigDecimal("50"));
        expense.setExpenseDate(LocalDate.of(2026, 1, 1));
        expense.setPaymentMethod(PaymentMethod.CASH);
        expense.setAccount(account);
        expense.setCategory(category);

        expenseRepository.save(expense);

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnExpenseById() throws Exception {

        Expense expense = new Expense();
        expense.setTitle("Laptop");
        expense.setAmount(new BigDecimal("300"));
        expense.setExpenseDate(LocalDate.of(2026, 1, 1));
        expense.setPaymentMethod(PaymentMethod.CASH);
        expense.setAccount(account);
        expense.setCategory(category);

        expense = expenseRepository.save(expense);

        mockMvc.perform(get("/api/expenses/" + expense.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Laptop"));
    }

    @Test
    void shouldDeleteExpense() throws Exception {

        Expense expense = new Expense();
        expense.setTitle("Delete Test");
        expense.setAmount(new BigDecimal("20"));
        expense.setExpenseDate(LocalDate.of(2026, 1, 1));
        expense.setPaymentMethod(PaymentMethod.CASH);
        expense.setAccount(account);
        expense.setCategory(category);

        expense = expenseRepository.save(expense);

        mockMvc.perform(delete("/api/expenses/" + expense.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/expenses/" + expense.getId()))
                .andExpect(status().isNotFound());
    }
}
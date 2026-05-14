package com.example.expencetrackerapi;

import com.example.expencetrackerapi.dto.request.CreateBudgetRequest;
import com.example.expencetrackerapi.entity.Account;
import com.example.expencetrackerapi.entity.Category;
import com.example.expencetrackerapi.repository.AccountRepository;
import com.example.expencetrackerapi.repository.BudgetRepository;
import com.example.expencetrackerapi.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BudgetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    private ObjectMapper objectMapper;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        budgetRepository.deleteAll();
        categoryRepository.deleteAll();
        accountRepository.deleteAll();

        Account account = new Account();
        account.setFullName("Test User");
        account.setEmail("test@test.com");
        account.setCurrentBalance(BigDecimal.valueOf(1000));
        accountRepository.save(account);

        testCategory = new Category();
        testCategory.setName("Food");
        testCategory.setAccount(account);
        categoryRepository.save(testCategory);
    }

    @Test
    void createBudget_Returns201() throws Exception {
        CreateBudgetRequest request = new CreateBudgetRequest();
        request.setCategoryId(testCategory.getId());
        request.setAmountLimit(BigDecimal.valueOf(500));
        request.setStartDate(LocalDate.of(2026, 1, 1));
        request.setEndDate(LocalDate.of(2026, 1, 31));

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amountLimit").value(500))
                .andExpect(jsonPath("$.categoryName").value("Food"));
    }

    @Test
    void getAllBudgets_Returns200() throws Exception {
        CreateBudgetRequest request = new CreateBudgetRequest();
        request.setCategoryId(testCategory.getId());
        request.setAmountLimit(BigDecimal.valueOf(300));
        request.setStartDate(LocalDate.of(2026, 2, 1));
        request.setEndDate(LocalDate.of(2026, 2, 28));

        mockMvc.perform(post("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/budgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getBudgetById_Returns200() throws Exception {
        CreateBudgetRequest request = new CreateBudgetRequest();
        request.setCategoryId(testCategory.getId());
        request.setAmountLimit(BigDecimal.valueOf(200));
        request.setStartDate(LocalDate.of(2026, 3, 1));
        request.setEndDate(LocalDate.of(2026, 3, 31));

        String response = mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/budgets/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getBudgetById_NotFound_Returns404() throws Exception {
        mockMvc.perform(get("/api/budgets/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBudget_Returns200() throws Exception {
        CreateBudgetRequest request = new CreateBudgetRequest();
        request.setCategoryId(testCategory.getId());
        request.setAmountLimit(BigDecimal.valueOf(100));
        request.setStartDate(LocalDate.of(2026, 4, 1));
        request.setEndDate(LocalDate.of(2026, 4, 30));

        String response = mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        CreateBudgetRequest updateRequest = new CreateBudgetRequest();
        updateRequest.setCategoryId(testCategory.getId());
        updateRequest.setAmountLimit(BigDecimal.valueOf(999));
        updateRequest.setStartDate(LocalDate.of(2026, 4, 1));
        updateRequest.setEndDate(LocalDate.of(2026, 4, 30));

        mockMvc.perform(put("/api/budgets/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amountLimit").value(999));
    }

    @Test
    void deleteBudget_Returns204() throws Exception {
        CreateBudgetRequest request = new CreateBudgetRequest();
        request.setCategoryId(testCategory.getId());
        request.setAmountLimit(BigDecimal.valueOf(150));
        request.setStartDate(LocalDate.of(2026, 5, 1));
        request.setEndDate(LocalDate.of(2026, 5, 31));

        String response = mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/budgets/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/budgets/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBudget_NotFound_Returns404() throws Exception {
        mockMvc.perform(delete("/api/budgets/999"))
                .andExpect(status().isNotFound());
    }
}
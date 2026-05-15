package com.example.expencetrackerapi;

import com.example.expencetrackerapi.dto.request.CreateAccountRequest;
import com.example.expencetrackerapi.dto.request.UpdateAccountRequest;
import com.example.expencetrackerapi.repository.AccountRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        accountRepository.deleteAll();
    }

    private String createAccount(String fullName, String email, double balance) throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setFullName(fullName);
        request.setEmail(email);
        request.setCurrentBalance(BigDecimal.valueOf(balance));

        return mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void createAccount_Returns201() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setFullName("John Doe");
        request.setEmail("john@example.com");
        request.setCurrentBalance(BigDecimal.valueOf(1000.0));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.currentBalance").value(1000.0));
    }

    @Test
    void createAccount_DuplicateEmail_Returns500() throws Exception {
        createAccount("John Doe", "john@example.com", 1000.0);

        CreateAccountRequest duplicate = new CreateAccountRequest();
        duplicate.setFullName("Another John");
        duplicate.setEmail("john@example.com");
        duplicate.setCurrentBalance(BigDecimal.valueOf(500.0));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllAccounts_ReturnsEmptyList_WhenNoAccounts() throws Exception {
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllAccounts_ReturnsSummaryList_Returns200() throws Exception {
        createAccount("Alice", "alice@example.com", 500.0);
        createAccount("Bob", "bob@example.com", 750.0);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAccountById_Returns200() throws Exception {
        String response = createAccount("Jane Doe", "jane@example.com", 2000.0);
        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/accounts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    void getAccountById_NotFound_Returns404() throws Exception {
        mockMvc.perform(get("/api/accounts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_Returns200() throws Exception {
        String response = createAccount("Old Name", "old@example.com", 100.0);
        Long id = objectMapper.readTree(response).get("id").asLong();

        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setFullName("New Name");
        updateRequest.setEmail("new@example.com");
        updateRequest.setCurrentBalance(BigDecimal.valueOf(9999.0));

        mockMvc.perform(put("/api/accounts/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.currentBalance").value(9999.0));
    }

    @Test
    void updateAccount_WithDuplicateEmail_Returns500() throws Exception {
        createAccount("Alice", "alice@example.com", 500.0);
        String bobResponse = createAccount("Bob", "bob@example.com", 300.0);
        Long bobId = objectMapper.readTree(bobResponse).get("id").asLong();

        // Try to update Bob's email to Alice's existing email
        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setFullName("Bob");
        updateRequest.setEmail("alice@example.com");
        updateRequest.setCurrentBalance(BigDecimal.valueOf(300.0));

        mockMvc.perform(put("/api/accounts/" + bobId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest()
                );
    }

    @Test
    void updateAccount_NotFound_Returns404() throws Exception {
        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setFullName("Ghost");
        updateRequest.setEmail("ghost@example.com");
        updateRequest.setCurrentBalance(BigDecimal.valueOf(0));

        mockMvc.perform(put("/api/accounts/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_Returns204() throws Exception {
        String response = createAccount("To Delete", "delete@example.com", 50.0);
        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/accounts/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/accounts/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_NotFound_Returns404() throws Exception {
        mockMvc.perform(delete("/api/accounts/999"))
                .andExpect(status().isNotFound());
    }
}
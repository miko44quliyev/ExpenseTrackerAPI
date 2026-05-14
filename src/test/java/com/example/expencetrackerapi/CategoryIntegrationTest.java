package com.example.expencetrackerapi;

import com.example.expencetrackerapi.dto.request.CategoryRequest;
import com.example.expencetrackerapi.entity.Account;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    private ObjectMapper objectMapper;
    private Long testAccountId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // Delete children first, then parents
        budgetRepository.deleteAll();
        categoryRepository.deleteAll();
        accountRepository.deleteAll();

        Account account = new Account();
        account.setFullName("Test User");
        account.setEmail("test@test.com");
        account.setCurrentBalance(BigDecimal.valueOf(1000));
        testAccountId = accountRepository.save(account).getId();
    }

    private String createCategory(String name, String description, Long accountId) throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setAccountId(accountId);
        request.setName(name);
        request.setDescription(description);

        return mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void createCategory_Returns201() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setAccountId(testAccountId);
        request.setName("Food");
        request.setDescription("Groceries and eating out");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Food"))
                .andExpect(jsonPath("$.description").value("Groceries and eating out"))
                .andExpect(jsonPath("$.accountId").value(testAccountId));
    }

    @Test
    void createCategory_InvalidAccountId_Returns404() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setAccountId(999L);
        request.setName("Food");
        request.setDescription("Some description");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCategory_BlankName_Returns500() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setAccountId(testAccountId);
        request.setName("  ");
        request.setDescription("Some description");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createCategory_NameTooShort_Returns500() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setAccountId(testAccountId);
        request.setName("A");
        request.setDescription("Some description");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createCategory_NullAccountId_Returns500() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setAccountId(null);
        request.setName("Food");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllCategories_ReturnsEmptyList_WhenNoCategories() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllCategories_Returns200WithCategories() throws Exception {
        createCategory("Food", "Groceries", testAccountId);
        createCategory("Transport", "Bus and taxi", testAccountId);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCategoryById_Returns200() throws Exception {
        String response = createCategory("Entertainment", "Movies and games", testAccountId);
        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/categories/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Entertainment"))
                .andExpect(jsonPath("$.description").value("Movies and games"))
                .andExpect(jsonPath("$.accountId").value(testAccountId));
    }

    @Test
    void getCategoryById_NotFound_Returns404() throws Exception {
        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCategory_Returns200() throws Exception {
        String response = createCategory("Old Name", "Old description", testAccountId);
        Long id = objectMapper.readTree(response).get("id").asLong();

        CategoryRequest updateRequest = new CategoryRequest();
        updateRequest.setAccountId(testAccountId);
        updateRequest.setName("New Name");
        updateRequest.setDescription("New description");

        mockMvc.perform(put("/api/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.description").value("New description"));
    }

    @Test
    void updateCategory_NotFound_Returns404() throws Exception {
        CategoryRequest updateRequest = new CategoryRequest();
        updateRequest.setAccountId(testAccountId);
        updateRequest.setName("Any Name");
        updateRequest.setDescription("Any description");

        mockMvc.perform(put("/api/categories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCategory_InvalidAccountId_Returns404() throws Exception {
        String response = createCategory("Food", "Groceries", testAccountId);
        Long id = objectMapper.readTree(response).get("id").asLong();

        CategoryRequest updateRequest = new CategoryRequest();
        updateRequest.setAccountId(999L);
        updateRequest.setName("Food");
        updateRequest.setDescription("Groceries");

        mockMvc.perform(put("/api/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCategory_PartialUpdate_OnlyUpdatesProvidedFields() throws Exception {
        String response = createCategory("Food", "Original description", testAccountId);
        Long id = objectMapper.readTree(response).get("id").asLong();

        // Only update the name, leave description null so it stays unchanged
        CategoryRequest updateRequest = new CategoryRequest();
        updateRequest.setAccountId(testAccountId);
        updateRequest.setName("Updated Food");
        updateRequest.setDescription(null);

        mockMvc.perform(put("/api/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Food"))
                .andExpect(jsonPath("$.description").value("Original description"));
    }

    @Test
    void deleteCategory_Returns204() throws Exception {
        String response = createCategory("To Delete", "Will be gone", testAccountId);
        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/categories/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categories/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_NotFound_Returns404() throws Exception {
        mockMvc.perform(delete("/api/categories/999"))
                .andExpect(status().isNotFound());
    }
}
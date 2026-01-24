package io.github.alvarorg14.kashy.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.alvarorg14.kashy.api.mapper.ExpenseApiMapper;
import io.github.alvarorg14.kashy.api.model.api.CreateExpenseRequest;
import io.github.alvarorg14.kashy.api.model.api.ExpenseResponse;
import io.github.alvarorg14.kashy.api.model.domain.Category;
import io.github.alvarorg14.kashy.api.model.domain.Expense;
import io.github.alvarorg14.kashy.api.service.ExpenseService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ExpensesApiController.class)
@Import(ExpensesApiDelegateImpl.class)
@DisplayName("ExpensesApiDelegateImpl Controller Tests")
class ExpensesApiDelegateImplTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private ExpenseService expenseService;

  @MockitoBean private ExpenseApiMapper apiMapper;

  private CreateExpenseRequest createRequest;
  private Expense expense;
  private ExpenseResponse expenseResponse;

  @BeforeEach
  void setUp() {
    UUID id = UUID.randomUUID();
    OffsetDateTime now = OffsetDateTime.now();

    createRequest = new CreateExpenseRequest();
    createRequest.setDescription("Test expense");
    createRequest.setDateTime(now);
    createRequest.setAmount(BigDecimal.valueOf(100.50));
    createRequest.setCurrency("EUR");
    createRequest.setCategory(io.github.alvarorg14.kashy.api.model.api.Category.FOOD);
    createRequest.setNotes("Test notes");

    expense =
        Expense.builder()
            .id(id)
            .description("Test expense")
            .dateTime(now)
            .amount(BigDecimal.valueOf(100.50))
            .currency("EUR")
            .category(Category.FOOD)
            .notes("Test notes")
            .createdAt(now)
            .updatedAt(now)
            .build();

    expenseResponse = new ExpenseResponse();
    expenseResponse.setId(id);
    expenseResponse.setDescription("Test expense");
    expenseResponse.setDateTime(now);
    expenseResponse.setAmount(BigDecimal.valueOf(100.50));
    expenseResponse.setCurrency("EUR");
    expenseResponse.setCategory(io.github.alvarorg14.kashy.api.model.api.Category.FOOD);
    expenseResponse.setNotes("Test notes");
    expenseResponse.setCreatedAt(now);
    expenseResponse.setUpdatedAt(now);
  }

  @Test
  @DisplayName("Given valid expense request when createExpense then expense is created")
  void given_validExpenseRequest_when_createExpense_then_expenseIsCreated() throws Exception {
    // Given
    when(apiMapper.toDomain(any(CreateExpenseRequest.class))).thenReturn(expense);
    when(expenseService.createExpense(any(Expense.class))).thenReturn(expense);
    when(apiMapper.toResponse(any(Expense.class))).thenReturn(expenseResponse);

    // When
    mockMvc
        .perform(
            post("/api/v1/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data.id").value(expense.id().toString()))
        .andExpect(jsonPath("$.data.description").value("Test expense"))
        .andExpect(jsonPath("$.data.amount").value(100.50))
        .andExpect(jsonPath("$.data.currency").value("EUR"))
        .andExpect(jsonPath("$.data.category").value("FOOD"));
    // Then - assertions above
  }

  @Test
  @DisplayName("Given invalid expense request when createExpense then bad request is returned")
  void given_invalidExpenseRequest_when_createExpense_then_badRequestIsReturned() throws Exception {
    // Given
    CreateExpenseRequest invalidRequest = new CreateExpenseRequest();
    invalidRequest.setDescription(""); // Blank description
    invalidRequest.setAmount(BigDecimal.valueOf(-10)); // Negative amount

    // When
    mockMvc
        .perform(
            post("/api/v1/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());
    // Then - bad request status returned
  }

  @Test
  @DisplayName("Given multiple expenses when listExpenses then all expenses are returned")
  void given_multipleExpenses_when_listExpenses_then_allExpensesAreReturned() throws Exception {
    // Given
    OffsetDateTime now2 = OffsetDateTime.now();
    Expense expense2 =
        Expense.builder()
            .id(UUID.randomUUID())
            .description("Second expense")
            .dateTime(now2)
            .amount(BigDecimal.valueOf(50.00))
            .currency("USD")
            .category(Category.TRANSPORT)
            .notes(null)
            .createdAt(now2)
            .updatedAt(now2)
            .build();

    ExpenseResponse response2 = new ExpenseResponse();
    response2.setId(expense2.id());
    response2.setDescription("Second expense");
    response2.setAmount(BigDecimal.valueOf(50.00));
    response2.setCurrency("USD");
    response2.setCategory(io.github.alvarorg14.kashy.api.model.api.Category.TRANSPORT);

    when(expenseService.listExpenses()).thenReturn(List.of(expense, expense2));
    when(apiMapper.toResponse(expense)).thenReturn(expenseResponse);
    when(apiMapper.toResponse(expense2)).thenReturn(response2);

    // When
    mockMvc
        .perform(get("/api/v1/expenses"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data.length()").value(2))
        .andExpect(jsonPath("$.data[0].description").value("Test expense"))
        .andExpect(jsonPath("$.data[1].description").value("Second expense"));
    // Then - all expenses returned in data array
  }
}

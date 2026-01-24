package io.github.alvarorg14.kashy.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.github.alvarorg14.kashy.api.model.api.CreateExpenseRequest;
import io.github.alvarorg14.kashy.api.model.api.ExpenseResponse;
import io.github.alvarorg14.kashy.api.model.domain.Category;
import io.github.alvarorg14.kashy.api.model.domain.Expense;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ExpenseApiMapperImpl.class})
@DisplayName("ExpenseApiMapper Tests")
class ExpenseApiMapperTest {

  @Autowired private ExpenseApiMapper mapper;

  private CreateExpenseRequest createRequest;
  private Expense expense;

  @BeforeEach
  void setUp() {
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
            .id(UUID.randomUUID())
            .description("Test expense")
            .dateTime(now)
            .amount(BigDecimal.valueOf(100.50))
            .currency("EUR")
            .category(Category.FOOD)
            .notes("Test notes")
            .createdAt(now)
            .updatedAt(now)
            .build();
  }

  @Test
  @DisplayName("Given CreateExpenseRequest when mapToDomain then domain Expense is returned")
  void given_createExpenseRequest_when_mapToDomain_then_domainExpenseIsReturned() {
    // When
    Expense result = mapper.toDomain(createRequest);

    // Then
    assertNotNull(result);
    assertNull(result.id());
    assertEquals("Test expense", result.description());
    assertEquals(0, result.amount().compareTo(BigDecimal.valueOf(100.50)));
    assertEquals("EUR", result.currency());
    assertEquals(Category.FOOD, result.category());
    assertEquals("Test notes", result.notes());
    assertNull(result.createdAt());
    assertNull(result.updatedAt());
  }

  @Test
  @DisplayName("Given domain Expense when mapToResponse then ExpenseResponse is returned")
  void given_domainExpense_when_mapToResponse_then_expenseResponseIsReturned() {
    // When
    ExpenseResponse result = mapper.toResponse(expense);

    // Then
    assertNotNull(result);
    assertEquals(expense.id(), result.getId());
    assertEquals("Test expense", result.getDescription());
    assertEquals(0, result.getAmount().compareTo(BigDecimal.valueOf(100.50)));
    assertEquals("EUR", result.getCurrency());
    assertEquals(io.github.alvarorg14.kashy.api.model.api.Category.FOOD, result.getCategory());
    assertEquals("Test notes", result.getNotes());
    assertEquals(expense.createdAt(), result.getCreatedAt());
    assertEquals(expense.updatedAt(), result.getUpdatedAt());
  }
}

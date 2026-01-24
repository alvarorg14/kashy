package io.github.alvarorg14.kashy.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.alvarorg14.kashy.api.model.domain.Category;
import io.github.alvarorg14.kashy.api.model.domain.Expense;
import io.github.alvarorg14.kashy.api.model.entity.ExpenseEntity;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ExpenseEntityMapperImpl.class})
@DisplayName("ExpenseEntityMapper Tests")
class ExpenseEntityMapperTest {

  @Autowired private ExpenseEntityMapper mapper;

  private Expense expense;
  private ExpenseEntity entity;

  @BeforeEach
  void setUp() {
    UUID id = UUID.randomUUID();
    OffsetDateTime now = OffsetDateTime.now();

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

    entity =
        ExpenseEntity.builder()
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
  }

  @Test
  @DisplayName("Given domain Expense when mapToEntity then ExpenseEntity is returned")
  void given_domainExpense_when_mapToEntity_then_expenseEntityIsReturned() {
    // When
    ExpenseEntity result = mapper.toEntity(expense);

    // Then
    assertNotNull(result);
    assertEquals(expense.id(), result.getId());
    assertEquals("Test expense", result.getDescription());
    assertEquals(0, result.getAmount().compareTo(BigDecimal.valueOf(100.50)));
    assertEquals("EUR", result.getCurrency());
    assertEquals(Category.FOOD, result.getCategory());
    assertEquals("Test notes", result.getNotes());
    assertEquals(expense.createdAt(), result.getCreatedAt());
    assertEquals(expense.updatedAt(), result.getUpdatedAt());
  }

  @Test
  @DisplayName("Given ExpenseEntity when mapToDomain then domain Expense is returned")
  void given_expenseEntity_when_mapToDomain_then_domainExpenseIsReturned() {
    // When
    Expense result = mapper.toDomain(entity);

    // Then
    assertNotNull(result);
    assertEquals(entity.getId(), result.id());
    assertEquals("Test expense", result.description());
    assertEquals(0, result.amount().compareTo(BigDecimal.valueOf(100.50)));
    assertEquals("EUR", result.currency());
    assertEquals(Category.FOOD, result.category());
    assertEquals("Test notes", result.notes());
    assertEquals(entity.getCreatedAt(), result.createdAt());
    assertEquals(entity.getUpdatedAt(), result.updatedAt());
  }
}

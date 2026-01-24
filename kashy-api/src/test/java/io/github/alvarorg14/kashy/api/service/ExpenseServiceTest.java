package io.github.alvarorg14.kashy.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.alvarorg14.kashy.api.mapper.ExpenseEntityMapper;
import io.github.alvarorg14.kashy.api.model.domain.Category;
import io.github.alvarorg14.kashy.api.model.domain.Expense;
import io.github.alvarorg14.kashy.api.model.entity.ExpenseEntity;
import io.github.alvarorg14.kashy.api.repository.ExpenseRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExpenseService Tests")
class ExpenseServiceTest {

  @Mock private ExpenseRepository repository;

  @Mock private ExpenseEntityMapper mapper;

  @InjectMocks private ExpenseServiceImpl expenseService;

  private Expense testExpense;
  private ExpenseEntity testEntity;

  @BeforeEach
  void setUp() {
    OffsetDateTime now = OffsetDateTime.now();
    UUID id = UUID.randomUUID();

    testExpense =
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

    testEntity =
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
  @DisplayName("Given valid expense when createExpense then expense is created")
  void given_validExpense_when_createExpense_then_expenseIsCreated() {
    // Given
    Expense expenseWithoutId =
        Expense.builder()
            .id(null)
            .description(testExpense.description())
            .dateTime(testExpense.dateTime())
            .amount(testExpense.amount())
            .currency(testExpense.currency())
            .category(testExpense.category())
            .notes(testExpense.notes())
            .createdAt(null)
            .updatedAt(null)
            .build();

    when(mapper.toEntity(any(Expense.class))).thenReturn(testEntity);
    when(repository.save(any(ExpenseEntity.class))).thenReturn(testEntity);
    when(mapper.toDomain(testEntity)).thenReturn(testExpense);

    // When
    Expense result = expenseService.createExpense(expenseWithoutId);

    // Then
    assertNotNull(result);
    assertNotNull(result.id());
    assertEquals("Test expense", result.description());
    assertEquals(0, result.amount().compareTo(BigDecimal.valueOf(100.50)));
    assertNotNull(result.createdAt());
    assertNotNull(result.updatedAt());
    verify(repository).save(any(ExpenseEntity.class));
  }

  @Test
  @DisplayName("Given multiple expenses when listExpenses then all expenses are returned")
  void given_multipleExpenses_when_listExpenses_then_allExpensesAreReturned() {
    // Given
    OffsetDateTime now2 = OffsetDateTime.now();
    ExpenseEntity entity2 =
        ExpenseEntity.builder()
            .id(UUID.randomUUID())
            .description("Second expense")
            .dateTime(now2)
            .amount(BigDecimal.valueOf(50.00))
            .currency("USD")
            .category(Category.TRANSPORT)
            .createdAt(now2)
            .updatedAt(now2)
            .build();

    Expense expense2 =
        Expense.builder()
            .id(entity2.getId())
            .description(entity2.getDescription())
            .dateTime(entity2.getDateTime())
            .amount(entity2.getAmount())
            .currency(entity2.getCurrency())
            .category(entity2.getCategory())
            .notes(entity2.getNotes())
            .createdAt(entity2.getCreatedAt())
            .updatedAt(entity2.getUpdatedAt())
            .build();

    when(repository.findAll()).thenReturn(List.of(testEntity, entity2));
    when(mapper.toDomain(testEntity)).thenReturn(testExpense);
    when(mapper.toDomain(entity2)).thenReturn(expense2);

    // When
    List<Expense> result = expenseService.listExpenses();

    // Then
    assertEquals(2, result.size());
    assertEquals("Test expense", result.get(0).description());
    assertEquals("Second expense", result.get(1).description());
    verify(repository).findAll();
  }
}

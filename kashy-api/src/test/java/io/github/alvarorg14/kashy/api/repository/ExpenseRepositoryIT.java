package io.github.alvarorg14.kashy.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.alvarorg14.kashy.api.config.AbstractIT;
import io.github.alvarorg14.kashy.api.model.domain.Category;
import io.github.alvarorg14.kashy.api.model.entity.ExpenseEntity;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("ExpenseRepository Integration Tests")
class ExpenseRepositoryIT extends AbstractIT {

  @Autowired private ExpenseRepository repository;

  private ExpenseEntity testEntity1;
  private ExpenseEntity testEntity2;

  @BeforeEach
  void setUp() {
    repository.deleteAll();

    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    OffsetDateTime now = OffsetDateTime.now();

    testEntity1 =
        ExpenseEntity.builder()
            .id(id1)
            .description("Grocery shopping")
            .dateTime(now.minusDays(1))
            .amount(BigDecimal.valueOf(45.99))
            .currency("EUR")
            .category(Category.FOOD)
            .notes("Weekly groceries")
            .createdAt(now.minusDays(1))
            .updatedAt(now.minusDays(1))
            .build();

    testEntity2 =
        ExpenseEntity.builder()
            .id(id2)
            .description("Bus ticket")
            .dateTime(now)
            .amount(BigDecimal.valueOf(2.50))
            .currency("EUR")
            .category(Category.TRANSPORT)
            .notes(null)
            .createdAt(now)
            .updatedAt(now)
            .build();
  }

  @Test
  @DisplayName("Given expense entity when save then entity is saved")
  void given_expenseEntity_when_save_then_entityIsSaved() {
    // When
    ExpenseEntity saved = repository.save(testEntity1);

    // Then
    assertNotNull(saved);
    assertEquals(testEntity1.getId(), saved.getId());
    assertEquals("Grocery shopping", saved.getDescription());
    assertEquals(0, saved.getAmount().compareTo(BigDecimal.valueOf(45.99)));
    assertEquals(Category.FOOD, saved.getCategory());
  }

  @Test
  @DisplayName("Given saved expense when findById then expense is found")
  void given_savedExpense_when_findById_then_expenseIsFound() {
    // Given
    ExpenseEntity saved = repository.save(testEntity1);

    // When
    var found = repository.findById(saved.getId());

    // Then
    assertTrue(found.isPresent());
    assertEquals("Grocery shopping", found.get().getDescription());
  }

  @Test
  @DisplayName("Given multiple expenses when findAll then all expenses are returned")
  void given_multipleExpenses_when_findAll_then_allExpensesAreReturned() {
    // Given
    repository.save(testEntity1);
    repository.save(testEntity2);

    // When
    List<ExpenseEntity> all = repository.findAll();

    // Then
    assertEquals(2, all.size());
    assertTrue(
        all.stream()
            .map(ExpenseEntity::getDescription)
            .anyMatch(desc -> desc.equals("Grocery shopping")));
    assertTrue(
        all.stream()
            .map(ExpenseEntity::getDescription)
            .anyMatch(desc -> desc.equals("Bus ticket")));
  }

  @Test
  @DisplayName("Given saved expense when update then expense is updated")
  void given_savedExpense_when_update_then_expenseIsUpdated() {
    // Given
    ExpenseEntity saved = repository.save(testEntity1);
    saved.setDescription("Updated description");
    saved.setAmount(BigDecimal.valueOf(50.00));
    saved.setUpdatedAt(OffsetDateTime.now());

    // When
    ExpenseEntity updated = repository.save(saved);

    // Then
    assertEquals("Updated description", updated.getDescription());
    assertEquals(0, updated.getAmount().compareTo(BigDecimal.valueOf(50.00)));
  }

  @Test
  @DisplayName("Given saved expense when delete then expense is deleted")
  void given_savedExpense_when_delete_then_expenseIsDeleted() {
    // Given
    ExpenseEntity saved = repository.save(testEntity1);

    // When
    repository.deleteById(saved.getId());

    // Then
    assertFalse(repository.findById(saved.getId()).isPresent());
  }
}

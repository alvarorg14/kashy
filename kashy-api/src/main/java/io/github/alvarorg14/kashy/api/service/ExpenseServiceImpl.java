package io.github.alvarorg14.kashy.api.service;

import io.github.alvarorg14.kashy.api.mapper.ExpenseEntityMapper;
import io.github.alvarorg14.kashy.api.model.domain.Expense;
import io.github.alvarorg14.kashy.api.repository.ExpenseRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ExpenseService providing expense business logic.
 *
 * <p>Handles expense creation and retrieval operations, managing the mapping between domain models
 * and persistence entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

  private final ExpenseRepository repository;
  private final ExpenseEntityMapper mapper;

  @Override
  @Transactional
  public Expense createExpense(Expense expense) {
    log.debug("Creating expense: {}", expense.description());
    OffsetDateTime now = OffsetDateTime.now();
    UUID id = UUID.randomUUID();

    Expense expenseWithId =
        Expense.builder()
            .id(id)
            .description(expense.description())
            .dateTime(expense.dateTime())
            .amount(expense.amount())
            .currency(expense.currency())
            .category(expense.category())
            .notes(expense.notes())
            .createdAt(now)
            .updatedAt(now)
            .build();

    var entity = mapper.toEntity(expenseWithId);
    var savedEntity = repository.save(entity);
    log.info("Created expense with id: {}", savedEntity.getId());
    return mapper.toDomain(savedEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Expense> listExpenses() {
    log.debug("Retrieving all expenses");
    return repository.findAll().stream().map(mapper::toDomain).toList();
  }
}

package io.github.alvarorg14.kashy.api.controller;

import io.github.alvarorg14.kashy.api.mapper.ExpenseApiMapper;
import io.github.alvarorg14.kashy.api.model.api.CreateExpenseRequest;
import io.github.alvarorg14.kashy.api.model.api.CreateExpenseResponse;
import io.github.alvarorg14.kashy.api.model.api.ExpenseResponse;
import io.github.alvarorg14.kashy.api.model.api.ListExpensesResponse;
import io.github.alvarorg14.kashy.api.model.domain.Expense;
import io.github.alvarorg14.kashy.api.service.ExpenseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Implementation of the generated ExpensesApiDelegate interface.
 *
 * <p>This delegate handles HTTP request/response mapping and delegates business logic to the
 * ExpenseService. It converts between API DTOs (generated from OpenAPI) and domain models using
 * MapStruct mappers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExpensesApiDelegateImpl implements ExpensesApiDelegate {

  private final ExpenseService expenseService;
  private final ExpenseApiMapper apiMapper;

  @Override
  public CreateExpenseResponse createExpense(CreateExpenseRequest createExpenseRequest) {
    log.debug("Received create expense request");
    Expense domainExpense = apiMapper.toDomain(createExpenseRequest);
    Expense createdExpense = expenseService.createExpense(domainExpense);
    ExpenseResponse expenseResponse = apiMapper.toResponse(createdExpense);

    CreateExpenseResponse response = new CreateExpenseResponse();
    response.setData(expenseResponse);

    log.info("Created expense with id: {}", createdExpense.id());
    return response;
  }

  @Override
  public ListExpensesResponse listExpenses() {
    log.debug("Received list expenses request");
    List<Expense> expenses = expenseService.listExpenses();
    List<ExpenseResponse> expenseResponses = expenses.stream().map(apiMapper::toResponse).toList();

    ListExpensesResponse response = new ListExpensesResponse();
    response.setData(expenseResponses);

    log.info("Retrieved {} expenses", expenseResponses.size());
    return response;
  }
}

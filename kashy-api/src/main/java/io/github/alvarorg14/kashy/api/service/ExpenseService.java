package io.github.alvarorg14.kashy.api.service;

import io.github.alvarorg14.kashy.api.model.domain.Expense;
import java.util.List;

/**
 * Service interface for expense business logic operations.
 *
 * <p>Defines the contract for expense-related business operations, keeping the service layer
 * decoupled from implementation details.
 */
public interface ExpenseService {

  /**
   * Creates a new expense.
   *
   * <p>Generates a UUID, sets creation and update timestamps, and persists the expense to the
   * database.
   *
   * @param expense the expense to create (without id and timestamps)
   * @return the created expense with generated id and timestamps
   */
  Expense createExpense(Expense expense);

  /**
   * Retrieves all expenses.
   *
   * @return list of all expenses, ordered by creation date (newest first)
   */
  List<Expense> listExpenses();
}

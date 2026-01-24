package io.github.alvarorg14.kashy.api.mapper;

import io.github.alvarorg14.kashy.api.model.domain.Expense;
import io.github.alvarorg14.kashy.api.model.entity.ExpenseEntity;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between domain models and JPA entities.
 *
 * <p>Maps between domain Expense models and ExpenseEntity JPA entities for persistence operations.
 */
@Mapper(componentModel = "spring")
public interface ExpenseEntityMapper {

  /**
   * Maps a domain Expense to an ExpenseEntity for persistence.
   *
   * @param expense the domain expense model
   * @return ExpenseEntity for database operations
   */
  ExpenseEntity toEntity(Expense expense);

  /**
   * Maps an ExpenseEntity from the database to a domain Expense.
   *
   * @param entity the JPA entity
   * @return domain Expense model
   */
  Expense toDomain(ExpenseEntity entity);
}

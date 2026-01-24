package io.github.alvarorg14.kashy.api.mapper;

import io.github.alvarorg14.kashy.api.model.api.CreateExpenseRequest;
import io.github.alvarorg14.kashy.api.model.api.ExpenseResponse;
import io.github.alvarorg14.kashy.api.model.domain.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between API DTOs and domain models.
 *
 * <p>Maps between generated OpenAPI DTOs (CreateExpenseRequest, ExpenseResponse) and domain Expense
 * models.
 */
@Mapper(componentModel = "spring")
public interface ExpenseApiMapper {

  /**
   * Maps a CreateExpenseRequest DTO to a domain Expense.
   *
   * <p>Note: id, createdAt, and updatedAt are not present in the request and will be set by the
   * service layer.
   *
   * @param request the create expense request DTO
   * @return domain Expense model (without id and timestamps)
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Expense toDomain(CreateExpenseRequest request);

  /**
   * Maps a domain Expense to an ExpenseResponse DTO.
   *
   * @param expense the domain expense model
   * @return ExpenseResponse DTO
   */
  ExpenseResponse toResponse(Expense expense);
}

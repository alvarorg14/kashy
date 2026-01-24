package io.github.alvarorg14.kashy.api.repository;

import io.github.alvarorg14.kashy.api.model.entity.ExpenseEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for expense persistence operations.
 *
 * <p>Provides standard CRUD operations for ExpenseEntity through Spring Data JPA. Additional query
 * methods can be added here as needed.
 */
@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {}

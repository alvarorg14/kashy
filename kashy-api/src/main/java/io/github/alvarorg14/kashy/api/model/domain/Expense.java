package io.github.alvarorg14.kashy.api.model.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

/**
 * Domain model representing an expense.
 *
 * <p>This immutable record represents the core business entity for expenses. It contains all the
 * essential information about a financial expense.
 *
 * @param id Unique identifier for the expense
 * @param description Brief description of the expense
 * @param dateTime When the expense occurred
 * @param amount Monetary amount of the expense
 * @param currency ISO 4217 currency code (e.g., EUR, USD)
 * @param category Category classification of the expense
 * @param notes Optional extended description or notes
 * @param createdAt Timestamp when the expense was created
 * @param updatedAt Timestamp when the expense was last updated
 */
@Builder
public record Expense(
    UUID id,
    String description,
    OffsetDateTime dateTime,
    BigDecimal amount,
    String currency,
    Category category,
    String notes,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {}

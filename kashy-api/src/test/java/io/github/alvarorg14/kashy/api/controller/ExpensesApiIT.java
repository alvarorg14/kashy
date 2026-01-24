package io.github.alvarorg14.kashy.api.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import io.github.alvarorg14.kashy.api.config.AbstractIT;
import io.github.alvarorg14.kashy.api.repository.ExpenseRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Expenses API Integration Tests")
class ExpensesApiIT extends AbstractIT {

  private static final String EXPENSES_BASE_PATH = "/api/v1/expenses";

  @Value("${local.server.port}")
  private int port;

  @Autowired private ExpenseRepository expenseRepository;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
    RestAssured.baseURI = "http://localhost";
    expenseRepository.deleteAll();
  }

  @Nested
  @DisplayName("POST /api/v1/expenses - Create Expense")
  class CreateExpense {

    @Test
    @DisplayName(
        "Given valid expense request, when creating expense, then returns 201 with created expense")
    void givenValidExpenseRequest_whenCreatingExpense_thenReturns201WithCreatedExpense() {
      String requestBody =
          """
          {
            "description": "Grocery shopping",
            "dateTime": "2024-01-15T10:30:00Z",
            "amount": 45.99,
            "currency": "EUR",
            "category": "FOOD",
            "notes": "Weekly groceries from supermarket"
          }
          """;

      given()
          .contentType(ContentType.JSON)
          .body(requestBody)
          .when()
          .post(EXPENSES_BASE_PATH)
          .then()
          .statusCode(201)
          .contentType(ContentType.JSON)
          .body("data.id", notNullValue())
          .body("data.description", equalTo("Grocery shopping"))
          .body("data.dateTime", equalTo("2024-01-15T10:30:00Z"))
          .body("data.amount", equalTo(45.99f))
          .body("data.currency", equalTo("EUR"))
          .body("data.category", equalTo("FOOD"))
          .body("data.notes", equalTo("Weekly groceries from supermarket"))
          .body("data.createdAt", notNullValue())
          .body("data.updatedAt", notNullValue());
    }

    @Test
    @DisplayName(
        "Given valid expense request without notes, when creating expense, then returns 201 with null notes")
    void givenValidExpenseRequestWithoutNotes_whenCreatingExpense_thenReturns201WithNullNotes() {
      String requestBody =
          """
          {
            "description": "Bus ticket",
            "dateTime": "2024-01-14T08:15:00Z",
            "amount": 2.50,
            "currency": "EUR",
            "category": "TRANSPORT"
          }
          """;

      given()
          .contentType(ContentType.JSON)
          .body(requestBody)
          .when()
          .post(EXPENSES_BASE_PATH)
          .then()
          .statusCode(201)
          .contentType(ContentType.JSON)
          .body("data.id", notNullValue())
          .body("data.description", equalTo("Bus ticket"))
          .body("data.amount", equalTo(2.50f))
          .body("data.currency", equalTo("EUR"))
          .body("data.category", equalTo("TRANSPORT"))
          .body("data.notes", emptyOrNullString());
    }

    @ParameterizedTest(name = "Given {0}, when creating expense, then returns 400")
    @MethodSource("invalidExpenseRequests")
    void givenInvalidExpenseRequest_whenCreatingExpense_thenReturns400(
        String scenario, String requestBody) {
      given()
          .contentType(ContentType.JSON)
          .body(requestBody)
          .when()
          .post(EXPENSES_BASE_PATH)
          .then()
          .statusCode(400);
    }

    static Stream<Arguments> invalidExpenseRequests() {
      return Stream.of(
          Arguments.of(
              "missing description",
              """
              {
                "dateTime": "2024-01-15T10:30:00Z",
                "amount": 45.99,
                "currency": "EUR",
                "category": "FOOD"
              }
              """),
          Arguments.of(
              "missing amount",
              """
              {
                "description": "Grocery shopping",
                "dateTime": "2024-01-15T10:30:00Z",
                "currency": "EUR",
                "category": "FOOD"
              }
              """),
          Arguments.of(
              "invalid category",
              """
              {
                "description": "Grocery shopping",
                "dateTime": "2024-01-15T10:30:00Z",
                "amount": 45.99,
                "currency": "EUR",
                "category": "INVALID_CATEGORY"
              }
              """));
    }

    @Test
    @DisplayName(
        "Given expense request with all categories, when creating expense, then returns 201 for each valid category")
    void givenExpenseRequestWithDifferentCategories_whenCreatingExpense_thenReturns201() {
      String[] categories = {
        "FOOD", "TRANSPORT", "HOUSING", "UTILITIES", "ENTERTAINMENT",
        "HEALTHCARE", "SHOPPING", "EDUCATION", "TRAVEL", "OTHER"
      };

      for (String category : categories) {
        String requestBody =
            """
            {
              "description": "Test expense for %s",
              "dateTime": "2024-01-15T10:30:00Z",
              "amount": 10.00,
              "currency": "EUR",
              "category": "%s"
            }
            """
                .formatted(category, category);

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(EXPENSES_BASE_PATH)
            .then()
            .statusCode(201)
            .body("data.category", equalTo(category));
      }
    }
  }

  @Nested
  @DisplayName("GET /api/v1/expenses - List Expenses")
  class ListExpenses {

    @Test
    @DisplayName("Given no expenses exist, when listing expenses, then returns 200 with empty list")
    void givenNoExpensesExist_whenListingExpenses_thenReturns200WithEmptyList() {
      given()
          .contentType(ContentType.JSON)
          .when()
          .get(EXPENSES_BASE_PATH)
          .then()
          .statusCode(200)
          .contentType(ContentType.JSON)
          .body("data", hasSize(0));
    }

    @Test
    @DisplayName(
        "Given one expense exists, when listing expenses, then returns 200 with one expense")
    void givenOneExpenseExists_whenListingExpenses_thenReturns200WithOneExpense() {
      createExpense("Grocery shopping", "45.99", "FOOD");

      given()
          .contentType(ContentType.JSON)
          .when()
          .get(EXPENSES_BASE_PATH)
          .then()
          .statusCode(200)
          .contentType(ContentType.JSON)
          .body("data", hasSize(1))
          .body("data[0].description", equalTo("Grocery shopping"))
          .body("data[0].amount", equalTo(45.99f))
          .body("data[0].category", equalTo("FOOD"));
    }

    @Test
    @DisplayName(
        "Given multiple expenses exist, when listing expenses, then returns 200 with all expenses")
    void givenMultipleExpensesExist_whenListingExpenses_thenReturns200WithAllExpenses() {
      createExpense("Grocery shopping", "45.99", "FOOD");
      createExpense("Bus ticket", "2.50", "TRANSPORT");
      createExpense("Netflix subscription", "15.99", "ENTERTAINMENT");

      given()
          .contentType(ContentType.JSON)
          .when()
          .get(EXPENSES_BASE_PATH)
          .then()
          .statusCode(200)
          .contentType(ContentType.JSON)
          .body("data", hasSize(3));
    }

    @Test
    @DisplayName(
        "Given expense was just created, when listing expenses, then returns the created expense with correct data")
    void givenExpenseWasJustCreated_whenListingExpenses_thenReturnsCreatedExpenseWithCorrectData() {
      String createdExpenseId = createExpenseAndGetId("Coffee", "4.50", "FOOD", "Morning coffee");

      given()
          .contentType(ContentType.JSON)
          .when()
          .get(EXPENSES_BASE_PATH)
          .then()
          .statusCode(200)
          .contentType(ContentType.JSON)
          .body("data", hasSize(1))
          .body("data[0].id", equalTo(createdExpenseId))
          .body("data[0].description", equalTo("Coffee"))
          .body("data[0].amount", equalTo(4.50f))
          .body("data[0].category", equalTo("FOOD"))
          .body("data[0].notes", equalTo("Morning coffee"))
          .body("data[0].createdAt", not(emptyOrNullString()))
          .body("data[0].updatedAt", not(emptyOrNullString()));
    }
  }

  private void createExpense(String description, String amount, String category) {
    String requestBody =
        """
        {
          "description": "%s",
          "dateTime": "2024-01-15T10:30:00Z",
          "amount": %s,
          "currency": "EUR",
          "category": "%s"
        }
        """
            .formatted(description, amount, category);

    given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post(EXPENSES_BASE_PATH)
        .then()
        .statusCode(201);
  }

  private String createExpenseAndGetId(
      String description, String amount, String category, String notes) {
    String requestBody =
        """
        {
          "description": "%s",
          "dateTime": "2024-01-15T10:30:00Z",
          "amount": %s,
          "currency": "EUR",
          "category": "%s",
          "notes": "%s"
        }
        """
            .formatted(description, amount, category, notes);

    return given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post(EXPENSES_BASE_PATH)
        .then()
        .statusCode(201)
        .extract()
        .path("data.id");
  }
}

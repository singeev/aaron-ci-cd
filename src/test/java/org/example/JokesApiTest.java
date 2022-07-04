package org.example;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.response.Category;
import org.example.response.DataResponse;
import org.example.response.JokeMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class JokesApiTest {
  private RequestSpecification requestSpecs;

  @BeforeEach
  void setup() {
    requestSpecs =
        given()
            .baseUri("https://jokes.p.rapidapi.com")
            .basePath("/jod")
            .header("x-rapidapi-host", "jokes.p.rapidapi.com")
            .header("x-rapidapi-key", "56d7a4653emsh4c19b463b18e6b7p144eb7jsn030e478c59b2");
  }

  // This test makes assertions according to the task
  @Test
  void shouldFailOnNotFoundResponse() {
    Response response =
        given()
            .spec(requestSpecs)
            .when()
            .get("/test")
            .then()
            .contentType(JSON)
            .extract()
            .response();

    var statusCode = response.getStatusCode();
    var message = response.path("message");

    assertTrue(statusCode != 404 && !"Not Found".equals(message));
  }

  /* This test makes assertions according to assumption that
  - any healthy endpoint should return 200 OK
  - any error code should fail the test, not only 404
  - there should be a separate set of tests to verify error response JSON structure
   */
  @Test
  void shouldReturnOK() {
    given().spec(requestSpecs).when().get("/test").then().statusCode(200);
  }

  @Test
  void shouldFetchJokeByCategory() throws JsonProcessingException {
    DataResponse categoriesResponse =
        given()
            .spec(requestSpecs)
            .when()
            .get("/categories")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(DataResponse.class);

    List<Category> categories = categoriesResponse.getContents().getCategories();

    log.info(
        "Got {} categories from server: {}",
        categories.size(),
        categories.stream().map(Category::getName).collect(Collectors.joining(", ")));

    var categoryName = categories.get(2).getName();
    log.info("Fetching jokes by category name: {}", categoryName);

    DataResponse jokesResponse =
        given()
            .spec(requestSpecs)
            .when()
            .param("category", categoryName)
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(DataResponse.class);

    log.info("Writing jokes to a text file");
    jokesResponse.getContents().getJokes().forEach(this::writeJokeToFile);
  }

  private void writeJokeToFile(JokeMeta joke) {
    String path = "src/test/resources/" + joke.getJoke().getId() + ".txt";

    StringJoiner content = new StringJoiner("\r\n");
    content.add("Description: " + joke.getDescription());
    content.add("Category: " + joke.getCategory());
    content.add("Title: " + joke.getJoke().getTitle());
    content.add("Text: " + joke.getJoke().getText());

    try {
      Files.writeString(Paths.get(path), content.toString());
      log.info("Text file with a new joke successfully created: {}", path);
    } catch (IOException e) {
      log.error("Can't write joke to a file: ", e);
      throw new RuntimeException(e);
    }
  }
}

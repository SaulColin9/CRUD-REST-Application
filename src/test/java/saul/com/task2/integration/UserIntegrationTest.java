package saul.com.task2.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import saul.com.task2.entity.UserEntity;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    private static final String LOGIN_URL = "/auth/login";
    private static final String USERS_URL = "/api/users";

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test user creation and fetching using authenticated requests.
     */
    @Test
    void testCreateAndGetUsers() {
        // Step 1: Authenticate and Obtain JWT Token from /auth/login
        String token = authenticateAndGetJwtToken("admin", "password");

        // Step 2: Prepare Authorization Header with the Bearer Token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Add Bearer Token
        headers.setContentType(MediaType.APPLICATION_JSON); // Set Content-Type to JSON

        // Step 3: Create a new user (POST /api/users)
        UserEntity newUser = new UserEntity("John", "Doe");
        HttpEntity<UserEntity> createRequest = new HttpEntity<>(newUser, headers);
        ResponseEntity<UserEntity> createResponse = restTemplate.postForEntity(USERS_URL, createRequest, UserEntity.class);

        // Verify the user creation response
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getName()).isEqualTo("John");

        // Step 4: Fetch all users (GET /api/users)
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<UserEntity[]> getResponse = restTemplate.exchange(USERS_URL, HttpMethod.GET, getRequest, UserEntity[].class);

        // Verify the response contains the newly created user
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().length).isGreaterThan(0);
        assertThat(Objects.requireNonNull(getResponse.getBody())[0].getName()).isEqualTo("John");
    }

    /**
     * Test unauthorized access when no token is provided.
     */
    @Test
    void testUnauthorizedAccess() {
        // Create a request without any token in the Authorization header
        HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Void> response = restTemplate.exchange(USERS_URL, HttpMethod.GET, request, Void.class);

        // Verify the response is 401 Unauthorized
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Authenticate as an admin user and retrieve the JWT token from AuthController.
     *
     * @param username The username
     * @param password The password
     * @return A valid JWT token as a String
     */
    private String authenticateAndGetJwtToken(String username, String password) {
        Map<String, String> loginRequest = Map.of("username", username, "password", password);

        // Send a POST request to /auth/login with valid credentials
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(LOGIN_URL, loginRequest, Map.class);

        // Verify the login was successful
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().containsKey("token")).isTrue();

        // Return the JWT token from the response
        return (String) loginResponse.getBody().get("token");
    }
}

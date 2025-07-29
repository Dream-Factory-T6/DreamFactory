package com.DreamFactory.DF.auth;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JWTIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


        @Test
        void when_authOkPostReview_return_created() throws Exception{

            String login =  """
                {
                    "username": "user", 
                    "password": "password123"
                }
                """;

            MvcResult loginResult = mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(login))
                    .andExpect(status().isOk())
                    .andReturn();

            String json = loginResult.getResponse().getContentAsString();
            String token = JsonPath.read(json, "$.token");

            String review = """
                {
                  "rating": 5,
                  "body": "string",
                  "destinationId": 1
                }
                """;
            mockMvc.perform(post("/api/reviews")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(review)
                            .header(TokenJwtConfig.headerAuthorization
                                    , TokenJwtConfig.prefixToken
                                            + token))
                    .andExpect(status().isCreated());

        }


        @Test
        void when_authFailedPostUser_return_forbidden() throws Exception {
            String login =  """
                {
                    "username": "user", 
                    "password": "password123"
                }
                """;

            MvcResult loginResult = mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(login))
                    .andExpect(status().isOk())
                    .andReturn();

            String json = loginResult.getResponse().getContentAsString();
            String token = JsonPath.read(json, "$.token");

            String newUser = """
                    {
                      "username": "user2",
                      "email": "user@example.com",
                      "password": "password123",
                      "role": "USER"
                    }
                """;

            mockMvc.perform(post("/register/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUser)
                        .header(TokenJwtConfig.headerAuthorization
                                    , TokenJwtConfig.prefixToken
                                            + token))
                    .andExpect(status().isForbidden())
                    .andReturn();

        }


        @Test
        void when_loginFailed_return_created() throws Exception{

            String login =  """
                {
                    "username": "user", 
                    "password": "passwordd123"
                }
                """;

            MvcResult loginResult = mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(login))
                    .andExpect(status().isUnauthorized())
                    .andReturn();


        }


        @Test
        void when_authTokenIsBad_return_unauthorized() throws Exception {

            String newUser = """
                    {
                      "username": "user2",
                      "email": "user@example.com",
                      "password": "password123",
                      "role": "USER"
                    }
                """;

            String invalidJwt = TokenJwtConfig.prefixToken + "eyJhbGciOiJIUzI1NiJ9.invalidPayload.invalidSignature";

            mockMvc.perform(post("/register/admin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUser)
                            .header(TokenJwtConfig.headerAuthorization
                                    , invalidJwt))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

        }

        @Test
        void when_authTokenWithoutPrefix_return_unauthorized() throws Exception {

            String newUser = """
                {
                    "username": "user2",
                    "email": "user@example.com",
                    "password": "password123",
                    "role": "USER"
                }
                """;

            String badToken = "Bad invalid.token.value";

            mockMvc.perform(post("/register/admin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUser)
                            .header(TokenJwtConfig.headerAuthorization, badToken))
                    .andExpect(status().isForbidden());

        }

        @Test
        void when_noAuthorizationHeader_then_continueChain() throws Exception {
            mockMvc.perform(get("/api/destinations"))
                    .andExpect(status().isOk());
        }

        @Test
        void when_loginRequestHasInvalidJson_return_unauthorized() throws Exception{
            String invalidJson = """
                    {
                     username: 'user'
                     }
                    """;

            mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
    void when_loginRequestJsonHasUnexpectedField_return_unauthorized() throws Exception{
            String wrongJson = """
                    {
                    "invalidField": "asdf",
                    "anotherOne": 123
                    }
                    """;
            mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(wrongJson))
                    .andExpect(status().isUnauthorized());
        }

}

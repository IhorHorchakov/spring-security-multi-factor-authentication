package com.example;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login() throws Exception {
        this.mockMvc
                .perform(
                        get("/login")
                                .contentType(APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"username\": \"fairyprincess@gmail.com\",\n" +
                                        "    \"password\": \"fairyprincess\"\n" +
                                        "}")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}

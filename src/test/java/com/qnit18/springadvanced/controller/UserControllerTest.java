package com.qnit18.springadvanced.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qnit18.springadvanced.dto.request.UserCreationRequest;
import com.qnit18.springadvanced.dto.response.UserResponse;
import com.qnit18.springadvanced.entity.User;
import com.qnit18.springadvanced.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Date;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc // Create Mock request
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach // method initDate run before all @Test
    void initData(){

        dob = LocalDate.of(1990, 12, 22);

        request = UserCreationRequest.builder()
                .username("username3")
                .firstName("Quang")
                .lastName("Nguyen")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id(12)
                .username("username3")
                .firstName("Quang")
                .lastName("Nguyen")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Config LocalDate because json not support LocalDate
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                        .thenReturn(userResponse);

        // WHEN, THEN(Expect)
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1000)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("result.id")
                        .value(12));
        // THEN

    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        request.setUsername("vq");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Config LocalDate because json not support LocalDate
        String content = objectMapper.writeValueAsString(request);

//        Mockito.when(userService.createUser(ArgumentMatchers.any()))
//                .thenReturn(userResponse);

        // WHEN, THEN(Expect)
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1003)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Username must be at least 3 characters"));
        // THEN
    }
}

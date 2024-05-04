package com.qnit18.springadvanced.service;

import com.qnit18.springadvanced.dto.request.UserCreationRequest;
import com.qnit18.springadvanced.dto.response.UserResponse;
import com.qnit18.springadvanced.entity.User;
import com.qnit18.springadvanced.exception.AppException;
import com.qnit18.springadvanced.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")

public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
        // method initDate run before all @Test
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

        user = User.builder()
                .id(12)
                .username("username3")
                .firstName("Quang")
                .lastName("Nguyen")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // Then
        assertThat(response.getId()).isEqualTo(12);
        assertThat(response.getUsername()).isEqualTo("username3");
    }

    @Test
    void createUser_userExist_fail(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
//        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

}

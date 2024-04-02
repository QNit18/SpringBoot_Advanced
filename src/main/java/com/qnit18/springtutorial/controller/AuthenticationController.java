package com.qnit18.springtutorial.controller;

import com.nimbusds.jose.JOSEException;
import com.qnit18.springtutorial.dto.request.AuthenticationRequestDTO;
import com.qnit18.springtutorial.dto.request.IntrospectTokenRequest;
import com.qnit18.springtutorial.dto.response.ApiResponse;
import com.qnit18.springtutorial.dto.response.AuthenticationResponseDTO;
import com.qnit18.springtutorial.dto.response.IntrospectTokenResponse;
import com.qnit18.springtutorial.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponseDTO> authenticationResponse(@RequestBody AuthenticationRequestDTO requestDTO){
        var result = authenticationService.authenticate(requestDTO);
        return ApiResponse.<AuthenticationResponseDTO>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectTokenResponse> authenticate(@RequestBody IntrospectTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectTokenResponse>builder()
                .result(result)
                .build();
    }

}

package com.emlakjet.purchasing.controller;

import com.emlakjet.purchasing.dto.BaseResponse;
import com.emlakjet.purchasing.dto.LoginRequest;
import com.emlakjet.purchasing.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the class where we handle Authentication related requests. API details are available in the service layer.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(summary = "This is for the user to login.")
    @ApiResponses(value = {@ApiResponse(responseCode = "401", description = "This is when the user or password is incorrect or the user cannot be found.")})
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

}

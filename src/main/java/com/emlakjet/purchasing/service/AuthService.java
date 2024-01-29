package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.dto.BaseResponse;
import com.emlakjet.purchasing.dto.LoginRequest;
import com.emlakjet.purchasing.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * This is the class where Authentication related operations are performed.
 */
@Slf4j
@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * This generates and returns JWT tokens for Users to authenticate.
     *
     * @param loginRequest LoginRequest contains email and password fields.
     *                     According to this information, the user is checked.
     */
    public ResponseEntity<BaseResponse> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new BaseResponse("The username or password is incorrect or the user could not be found."));
        }

        String token = jwtUtil.generateToken(loginRequest.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", token));
    }

}

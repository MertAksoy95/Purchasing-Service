package com.emlakjet.purchasing.controller;

import com.emlakjet.purchasing.dto.BaseResponse;
import com.emlakjet.purchasing.dto.UserDto;
import com.emlakjet.purchasing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This is the class where we handle User related requests. API details are available in the service layer.
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "This is to list all users.")
    @GetMapping
    public ResponseEntity<BaseResponse> list(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int size,
                                             @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                             @RequestParam(required = false, defaultValue = "created") String sort) {
        return userService.list(page, size, sortDir, sort);
    }

    @Operation(summary = "This is to get user by Id.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the user is not found.")})
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> get(@PathVariable UUID id) {
        return userService.get(id);
    }

    @Operation(summary = "This is to creating user.")
    @PostMapping
    public ResponseEntity<BaseResponse> create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @Operation(summary = "This is to updating the user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the user is not found.")})
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable UUID id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @Operation(summary = "This is to deleting the user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the user is not found.")})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable UUID id) {
        return userService.delete(id);
    }

}

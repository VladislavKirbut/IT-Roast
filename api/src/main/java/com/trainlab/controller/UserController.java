package com.trainlab.controller;

import com.trainlab.dto.request.UserRequest;
import com.trainlab.model.User;
import com.trainlab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Spring Data Create User",
            description = "Creates a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "User created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    @PostMapping
//    public ResponseEntity<User> createUser(@Valid @RequestBody @Parameter(description = "User information", required = true) UserRequest userRequest) {
//
//        User createdUser = userService.create(userRequest);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }
//}
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody @Parameter(description = "User information", required = true) UserRequest userRequest) {
        User createdUser = userService.create(userRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Complete Registration",
            description = "Completes the user registration process",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Registration completed successfully"
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Error occurred during registration completion"
                    )
            }
    )
    @GetMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(@RequestParam("userEmail") String userEmail) {
        try {
            userService.activateUser(userEmail);
            return ResponseEntity.ok("Registration completed successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error occurred during registration completion: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Spring Data User Find All Search",
            description = "Find All Users without limitations",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded Users",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))

                    ),
                    @ApiResponse(responseCode = "INTERNAL_SERVER_ERROR", description = "Internal Server Error")
            }
    )
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Update User",
            description = "Update User based on given id and request body",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully updated User",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
                                           @Valid @RequestBody UserRequest userRequest) {
        User updatedUser = userService.update(userRequest, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data User Search by user Id",
            description = "Receive user by Id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded User",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> receiveUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}

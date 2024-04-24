package com.trainlab.controller;

import com.trainlab.dto.*;
import com.trainlab.exception.*;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.User;
import com.trainlab.model.testapi.UserStats;
import com.trainlab.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private  final UserMapper userMapper;

    @Override
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        User user = userService.findAuthorizedUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toDto(user));
    }

    @Override
    @GetMapping("/page/{id}")
    public ResponseEntity<UserPageDto> userPageById(@PathVariable Long id){

        User user = userService.findAuthorizedUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserPageDto(user));
    }

    @Override
    @PutMapping("/change-password/{userId}")
    public ResponseEntity<String> changePassword(@Valid @PathVariable Long userId,  @RequestBody UserUpdateDto userUpdateDto) {
        userService.changePassword(userId,userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("You changed password");
    }

    @Override
    @PatchMapping("/page/{id}")
    public ResponseEntity<UserPageDto> updateUserPage(@PathVariable("id") Long id,
                                                      @Valid @RequestBody UserPageUpdateDto userUpdateDto,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }
        return ResponseEntity.ok(userService.update(userUpdateDto, id));
    }

    //UserSTats part

    //  todo ДЛЯ СТРАНИЦЫ СТАТЫ тут будет лист юзерТестРезалтов
    // todo  эедпоинт для СТРАНИЦЫ СТАТЫ тут будет лист юзерСтатов с прожарко для юзера



    @GetMapping("/stats/{userId}")
    public  ResponseEntity<UserStatsDTO> getAllUserStats(@PathVariable Long userId){

        return ResponseEntity.ok(userService.getAllUserStats(userId));
    }



}

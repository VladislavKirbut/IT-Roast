package com.trainlab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trainlab.dto.RoleDto;
import com.trainlab.dto.UserDto;
import com.trainlab.exception.*;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.User;
import com.trainlab.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(value = Lifecycle.PER_CLASS)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private UserDto userDto;

    private UserMapper userMapper;

    private User user;

    @BeforeAll
    void init() {
        objectMapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new JavaTimeModule());
        userDto = UserDto.builder()
                .id(1L)
                .generatedName("user-1")
                .email("vladthedevj6@gmail.com")
                .created(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .changed(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                .roles(List.of(
                        RoleDto.builder()
                                .id(1)
                                .roleName("ROLE_USER")
                                .created(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
                                .changed(Timestamp.valueOf(LocalDateTime.now().withNano(0))).build())
                ).build();

          user = userMapper.toEntity(userDto);
    }

    @Test
    void getAllUsersShouldBeSuccess() throws Exception {
        List<UserDto> expected = List.of(userDto, userDto);

        when(userService.findAll()).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(request(GET, "/api/v1/users"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expected));
        verify(userService, only()).findAll();
    }

    @Test
    void findUserByIdShouldBeSuccessIfUserExists() throws Exception {
        Long userId = 1L;
        when(userService.findAuthorizedUser(userId)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(request(GET, "/api/v1/users/1"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(user));
        verify(userService, only()).findAuthorizedUser(userId);
    }

    @Test
    void findUserByIdShouldBeFailIfUserNotExist() throws Exception {
        Long userId = 2L;
        when(userService.findAuthorizedUser(userId)).thenThrow(new ObjectNotFoundException("User could not be found"));

        MvcResult mvcResult = mockMvc.perform(request(GET, "/api/v1/users/2"))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(ObjectNotFoundException.class);
        assertThat(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage()).isEqualTo(
                "User could not be found"
        );
        verify(userService, only()).findAuthorizedUser(userId);
    }
}


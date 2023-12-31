package com.trainlab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trainlab.dto.RoleDto;
import com.trainlab.dto.UserCreateDto;
import com.trainlab.dto.UserDto;
import com.trainlab.exception.ValidationException;
import com.trainlab.model.security.RefreshToken;
import com.trainlab.security.TokenProvider;
import com.trainlab.security.dto.AuthResponseDto;
import com.trainlab.security.model.AccessToken;
import com.trainlab.security.model.JwtTokenProperties;
import com.trainlab.security.model.RefreshTokenProperties;
import com.trainlab.security.principal.UserPrincipal;
import com.trainlab.service.AuthService;
import com.trainlab.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_PLACEHOLDER;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(value = SpringExtension.class)
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class UserControllerImplTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProperties jwtProps;

    @Autowired
    private RefreshTokenProperties refreshProps;

    private ObjectMapper objectMapper;

    @BeforeAll
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new JavaTimeModule());
    }

    @Nested
    @DisplayName("User test registration functionality")
    @Tag(value = "registration")
    class RegistrationTest {
        @ParameterizedTest(name = ARGUMENTS_PLACEHOLDER)
        @MethodSource("paramsWithExceptions")
        void createUserShouldBeFail(UserCreateDto user, String exceptionMessage) throws Exception {
            MvcResult mvcResult = mockMvc.perform(request(POST, "/api/v1/users/register")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertThat(mvcResult.getResolvedException()).isInstanceOf(ValidationException.class);
            assertThat(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage()).isEqualTo(exceptionMessage);

            verify(userService, never()).create(user);
            verify(tokenProvider, never()).generate(any());
            verify(tokenProvider, never()).generateRefreshToken();
            verify(authService, never()).createRefreshSession(any(), any());
        }

        static Stream<Arguments> paramsWithExceptions() {
            return Stream.of(
                    Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password(" ").build(), "The password field is required"),
                    Arguments.of(UserCreateDto.builder().email(" ").password("123456qW").build(), "The email field is required"),
                    Arguments.of(UserCreateDto.builder().email(" ").password(" ").build(), "Email and password fields are required"),
                    Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password("1234qW").build(), "Invalid password." +
                            " The password must be typed in Latin letters, consist of at least 8 characters and contain at least one lowercase and one uppercase character"),
                    Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password("987654321").build(), "Invalid password." +
                            " The password must be typed in Latin letters, consist of at least 8 characters and contain at least one lowercase and one uppercase character"),
                    Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password("ABCDEFGH").build(), "Invalid password." +
                            " The password must be typed in Latin letters, consist of at least 8 characters and contain at least one lowercase and one uppercase character"),
                    Arguments.of(UserCreateDto.builder().email("vladthedevj6@gmail.com").password("ывн244р4").build(), "Invalid password." +
                            " The password must be typed in Latin letters, consist of at least 8 characters and contain at least one lowercase and one uppercase character"),
                    Arguments.arguments(UserCreateDto.builder().email("myemail@com").password("123456qW").build(), "Invalid email address"),
                    Arguments.arguments(UserCreateDto.builder().email("myemail%$^@domain").password("123456qW").build(), "Invalid email address"),
                    Arguments.arguments(UserCreateDto.builder().email("myemail@192.168.1.1").password("123456qW").build(), "Invalid email address"),
                    Arguments.arguments(UserCreateDto.builder().email(".myemail@1gmail.com").password("123456qW").build(), "Invalid email address"),
                    Arguments.of(UserCreateDto.builder()
                            .email("test.trainlab+Sun_of_the_sleepless_Melancholy_star_Whose_tearful_beam_glows_tremulously_far" +
                                    "_That_showst_the_darkness_thou_canst_not_dispel_How_like_art_thou_to_joy_rememberd_well_" +
                                    "What_is_this_life_if_ful_of_care_We_have_no_time_to_stand_and_stare1@gmail.com") // 257
                            .password("123456qW").build(), "User email must be between 8 and 256 characters"),
                    Arguments.of(UserCreateDto.builder()
                                    .email("vladthedevj6@gmail.com")
                                    .password("Wdj0lLfsiBWp0vQ0CocM2BnD7ZkqTeiELahreGsJgCBXR88diCoa7tAOf0nFUSufkmxRTFSQsCevZnhuQnse" +
                                            "9MCZ4d6K2fD3iDe0eRRv3Dn1r2RN21Q76k9AeHksffPn7fCjMgbSo3ApbxgH3VxWK7BMKYgFpUQ60efQwz96vI" +
                                            "VoQGww0OBpJzloOKy44nb977HDrL2Bi439ScEWa7Nkohq5PI18si5OX9ISzV8S87qIBbfpZGRpapsW292ic7d5d")
                                    .build(), //257
                            "Invalid password. The password must be typed in Latin letters, consist of at least 8 characters and contain at least one lowercase and one uppercase character"),
                    Arguments.of(UserCreateDto.builder().email("vlad@gmailcom").password("1234a").build(),
                            "Invalid email and password. The password must be typed in Latin letters, consist of at least 8 characters and contain at least one lowercase and one uppercase character")
            );
        }

        @Test
        void createUserIfEmailAndPasswordCorrect() throws Exception {
            UserCreateDto user = UserCreateDto.builder()
                    .email("vladthedevj6@gmail.com")
                    .password("123456Wq").build();
            UserDto userDto = UserDto.builder()
                    .id(1L)
                    .username("user-1")
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
            UserPrincipal userPrincipal = new UserPrincipal(userDto.getId(), userDto.getRoles());
            AccessToken accessToken = AccessToken.builder()
                    .value("c15ddb54-7bd9-40ba-9713-a0ebc2af2c6d")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(jwtProps.getTimeToLive())).build();
            RefreshToken refreshToken = RefreshToken.builder()
                    .value(UUID.fromString("7aebf344-73df-4ce1-ad91-a1e2c6a4bcdf"))
                    .issuedAt(Instant.now())
                    .expiredAt(Instant.now().plus(refreshProps.getTimeToLive())).build();
            AuthResponseDto expected = AuthResponseDto.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .userDto(userDto).build();

            when(userService.create(user)).thenReturn(userDto);
            when(tokenProvider.generate(userPrincipal)).thenReturn(accessToken);
            when(tokenProvider.generateRefreshToken()).thenReturn(refreshToken);

            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.request(POST, "/api/v1/users/register")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expected));

            verify(userService, only()).create(user);
            verify(authService, only()).createRefreshSession(userDto, refreshToken);
        }
    }
}


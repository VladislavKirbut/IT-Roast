package com.trainlab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequest {

    @Schema(example = "SvetaPiven", type = "string", description = "Username")
    private String username;

    @Schema(example = "svetapiven@gmail.com", type = "string", description = "User Email")
    private String email;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "123456qW", type = "string", description = "User password")
    private String password;
}

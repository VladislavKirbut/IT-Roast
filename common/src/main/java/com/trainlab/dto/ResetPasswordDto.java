package com.trainlab.dto;

import com.trainlab.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
@Schema(description = "Reset password Dto")
public class ResetPasswordDto {
    @Size(message = "User email must be between 8 and 256 characters", min = 8, max = 256)
    @Email(message = "Invalid email address")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "trainlab@gmail.com",
            type = "string", description = "User email")
    private String email;
}

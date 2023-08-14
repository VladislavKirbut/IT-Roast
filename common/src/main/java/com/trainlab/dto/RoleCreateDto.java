package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request role DTO data from UI to create role")
public class RoleCreateDto {
    @Size(max = 30)
    @NotNull
    @NotEmpty
    private String roleName;

    @NotNull
    private Timestamp created;

    @NotNull
    private Timestamp changed;

    @NotNull
    private boolean isDeleted = false;

}

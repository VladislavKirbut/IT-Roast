package com.trainlab.mapper;

import com.trainlab.dto.RoleRequestDto;
import com.trainlab.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    @Mapping(target = "created", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))")
    @Mapping(target = "changed", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))")
    Role toEntity(RoleRequestDto roleRequest);

    @Mapping(target = "changed", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))")
    Role partialUpdateToEntity(RoleRequestDto roleRequest, @MappingTarget Role role);

}
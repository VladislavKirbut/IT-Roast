package com.trainlab.dto;

import com.trainlab.model.testapi.UserStats;
import com.trainlab.model.testapi.UserTestResult;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatsDTO {
    private  String name;
    private List<UserStatsHeaderDTO> stats;
    private List<UserTestResultDTO> results;

}

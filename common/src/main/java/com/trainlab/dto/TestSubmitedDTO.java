package com.trainlab.dto;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.Enum.eUserLevel;
import com.trainlab.model.testapi.Test;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TestSubmitedDTO {
    private eSpecialty specialty;
    private int score;
    private eUserLevel level;
}

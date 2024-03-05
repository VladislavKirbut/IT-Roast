package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Answer info response")
public class AnswerDTO {
    private Long id;
    private  String answerTxt;
    boolean isCorrect;
}

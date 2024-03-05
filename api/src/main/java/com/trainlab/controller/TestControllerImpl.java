package com.trainlab.controller;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;
import com.trainlab.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tests")
@RequiredArgsConstructor
public class TestControllerImpl implements TestController {
    private final TestService testService;


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TestDTO> getTest(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.getTest(id));
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<List<TestDTO>> getAllTests() {
      return   ResponseEntity.status(HttpStatus.OK).body(testService.getAll());
    }

    @Override
    @GetMapping("/{specialty}")
    public ResponseEntity<List<TestDTO>> getAllTestsBySpeciality(@PathVariable eSpecialty specialty) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.getAllBySpeciality(specialty));
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<TestDTO> createTest(@Valid @RequestBody TestCreateDTO createTestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.create(createTestDTO));
    }

    @Override
    @PostMapping("/test/{testId}")
    public ResponseEntity<QuestionDTO> addQuestion(@PathVariable Long testId ,@Valid @RequestBody  QuestionCreateDTO questionDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.addQuestion(testId,questionDTO));
    }

    @Override
    @PostMapping("/question/{questionId}")
    public ResponseEntity<AnswerDTO> addAnswer(@PathVariable Long questionId, @Valid @RequestBody AnswerCreateDTO answerDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.addAnswer(questionId,answerDTO));
    }

    @Override
    @PatchMapping("/question/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long questionId, @RequestBody QuestionCreateDTO questionCreateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.updateQuestion(questionId,questionCreateDTO));
    }

    @Override
    @PatchMapping("/answer/{answerId}")
    public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable Long answerId, @RequestBody AnswerCreateDTO answerCreateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.updateAnswer(answerId,answerCreateDTO));
    }
}

package projetosg10.survey.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetosg10.survey.dto.SurveyCreateDTO;
import projetosg10.survey.dto.SurveyDTO;
import projetosg10.survey.service.SurveyService;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@CrossOrigin(origins = "*")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping
    public ResponseEntity<List<SurveyDTO>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDTO> getSurveyById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(surveyService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createSurvey(@Valid @RequestBody SurveyCreateDTO dto) {
        try {
            SurveyDTO created = surveyService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSurvey(@PathVariable Long id,
                                          @Valid @RequestBody SurveyCreateDTO dto) {
        try {
            SurveyDTO updated = surveyService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSurvey(@PathVariable Long id) {
        try {
            surveyService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<SurveyDTO> toggleActive(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(surveyService.toggleActive(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
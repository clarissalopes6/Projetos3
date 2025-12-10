package projetosg10.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetosg10.survey.dto.AIAnalysisDTO;
import projetosg10.survey.service.AIAnalysisService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIAnalysisController {

    @Autowired
    private AIAnalysisService aiAnalysisService;

    @GetMapping("/analyze")
    public ResponseEntity<AIAnalysisDTO> generateAnalysis() {
        try {
            AIAnalysisDTO analysis = aiAnalysisService.generateAnalysis();
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

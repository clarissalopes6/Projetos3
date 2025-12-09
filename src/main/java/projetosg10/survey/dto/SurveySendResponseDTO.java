package projetosg10.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveySendResponseDTO {
    
    private Long surveyId;
    private String surveyTitle;
    private int totalSent;
    private List<String> sentTo;
    private List<String> failed;
    private String message;
}

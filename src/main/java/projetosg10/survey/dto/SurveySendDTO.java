package projetosg10.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveySendDTO {
    
    private Long surveyId;
    private List<String> clientEmails;
}

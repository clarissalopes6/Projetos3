package projetosg10.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIAnalysisDTO {
    private String analysis;
    private Integer totalComments;
    private Integer positiveCount;
    private Integer negativeCount;
    private Integer neutralCount;
    private List<String> comments;
}

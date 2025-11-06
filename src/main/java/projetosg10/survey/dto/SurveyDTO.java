package projetosg10.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDTO {

    private Long id;
    private String title;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;

    // Futuramente, poderíamos adicionar:
    // private int responseCount;
}
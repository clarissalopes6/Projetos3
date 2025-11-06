package projetosg10.survey.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyCreateDTO {

    @NotBlank(message = "Título é obrigatório")
    private String title;

    private String description;
}
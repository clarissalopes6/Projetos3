package projetosg10.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    
    private Long id;
    private String name;
    private String email;
    private Boolean active;
    private LocalDateTime createdAt;
}

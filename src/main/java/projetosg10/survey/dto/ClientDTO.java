package projetosg10.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String cpfCnpj;
    private Boolean active;
    private LocalDateTime createdAt;
}

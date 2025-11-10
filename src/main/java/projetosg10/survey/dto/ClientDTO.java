package projetosg10.survey.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    
    private Long id;
    
    private String name;

    private String email;

    private String phone;

    private String cpfCnpj;
}

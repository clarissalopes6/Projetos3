package projetosg10.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetosg10.survey.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByEmail(String email);
    
    Optional<Client> findByCpfCnpj(String cpfCnpj);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpfCnpj(String cpfCnpj);
}

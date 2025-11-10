package projetosg10.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetosg10.survey.model.Client;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional <Client> findByEmail(String email);
    boolean existsByEmail(String email);
}

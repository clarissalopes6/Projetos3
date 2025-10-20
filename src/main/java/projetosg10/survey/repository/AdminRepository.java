package projetosg10.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetosg10.survey.model.Admin;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    Optional<Admin> findByEmail(String email);
    
    boolean existsByEmail(String email);
}

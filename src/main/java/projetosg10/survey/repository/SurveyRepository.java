package projetosg10.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetosg10.survey.model.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
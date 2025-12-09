package projetosg10.survey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetosg10.survey.dto.SurveyCreateDTO;
import projetosg10.survey.dto.SurveyDTO;
import projetosg10.survey.dto.SurveySendDTO;
import projetosg10.survey.dto.SurveySendResponseDTO;
import projetosg10.survey.model.Client;
import projetosg10.survey.model.Survey;
import projetosg10.survey.repository.ClientRepository;
import projetosg10.survey.repository.SurveyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ClientRepository clientRepository;

    private SurveyDTO toDTO(Survey survey) {
        return new SurveyDTO(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getActive(),
                survey.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<SurveyDTO> findAll() {
        return surveyRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SurveyDTO findById(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pesquisa não encontrada"));
        return toDTO(survey);
    }

    @Transactional
    public SurveyDTO create(SurveyCreateDTO dto) {
        Survey survey = new Survey();
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setActive(true);

        survey = surveyRepository.save(survey);
        return toDTO(survey);
    }

    @Transactional
    public SurveyDTO update(Long id, SurveyCreateDTO dto) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pesquisa não encontrada"));

        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());

        survey = surveyRepository.save(survey);
        return toDTO(survey);
    }

    @Transactional
    public void delete(Long id) {
        if (!surveyRepository.existsById(id)) {
            throw new RuntimeException("Pesquisa não encontrada");
        }
        surveyRepository.deleteById(id);
    }

    @Transactional
    public SurveyDTO toggleActive(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pesquisa não encontrada"));

        survey.setActive(!survey.getActive());
        survey = surveyRepository.save(survey);
        return toDTO(survey);
    }

    @Transactional
    public SurveySendResponseDTO sendSurveyToClients(SurveySendDTO sendDTO) {
        Survey survey = surveyRepository.findById(sendDTO.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Pesquisa não encontrada"));

        if (!survey.getActive()) {
            throw new RuntimeException("Não é possível enviar uma pesquisa inativa");
        }

        List<String> sentTo = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for (String email : sendDTO.getClientEmails()) {
            try {
                Client client = clientRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + email));

                sentTo.add(email);

            } catch (Exception e) {
                failed.add(email + " - " + e.getMessage());
            }
        }

        SurveySendResponseDTO response = new SurveySendResponseDTO();
        response.setSurveyId(survey.getId());
        response.setSurveyTitle(survey.getTitle());
        response.setTotalSent(sentTo.size());
        response.setSentTo(sentTo);
        response.setFailed(failed);
        
        if (failed.isEmpty()) {
            response.setMessage("Pesquisa enviada com sucesso para todos os clientes");
        } else {
            response.setMessage("Pesquisa enviada com sucesso para " + sentTo.size() + " cliente(s). " + 
                              failed.size() + " falha(s).");
        }

        return response;
    }
}
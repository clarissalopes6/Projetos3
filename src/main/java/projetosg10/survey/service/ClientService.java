package projetosg10.survey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetosg10.survey.dto.ClientCreateDTO;
import projetosg10.survey.dto.ClientDTO;
import projetosg10.survey.model.Client;
import projetosg10.survey.repository.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return toDTO(client);
    }

    @Transactional
    public ClientDTO create(ClientCreateDTO dto) {
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        if (clientRepository.existsByCpfCnpj(dto.getCpfCnpj())) {
            throw new RuntimeException("CPF/CNPJ já cadastrado");
        }

        Client client = new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setCpfCnpj(dto.getCpfCnpj());
        client.setActive(true);

        client = clientRepository.save(client);
        return toDTO(client);
    }

    @Transactional
    public ClientDTO update(Long id, ClientCreateDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!client.getEmail().equals(dto.getEmail()) && 
            clientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (!client.getCpfCnpj().equals(dto.getCpfCnpj()) && 
            clientRepository.existsByCpfCnpj(dto.getCpfCnpj())) {
            throw new RuntimeException("CPF/CNPJ já cadastrado");
        }

        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setCpfCnpj(dto.getCpfCnpj());

        client = clientRepository.save(client);
        return toDTO(client);
    }

    @Transactional
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado");
        }
        clientRepository.deleteById(id);
    }

    @Transactional
    public ClientDTO toggleActive(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        client.setActive(!client.getActive());
        client = clientRepository.save(client);
        return toDTO(client);
    }

    private ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getCpfCnpj(),
                client.getActive(),
                client.getCreatedAt()
        );
    }
}

package projetosg10.survey.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import projetosg10.survey.dto.ClientCreateDTO;
import projetosg10.survey.dto.ClientDTO;
import projetosg10.survey.model.Client;
import projetosg10.survey.repository.ClientRepository;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService (ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private ClientDTO convertToDTO (Client client) {
        return new ClientDTO(
            client.getId(), 
            client.getName(), 
            client.getEmail(),
            client.getPhone(),
            client.getCpfCnpj()
        );
    }

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional <ClientDTO> getClientById (Long id) {
        return clientRepository.findById(id).map(this::convertToDTO);
    }

    public Optional <ClientDTO> getClientByEmail (String email) {
        return clientRepository.findByEmail(email).map(this::convertToDTO);
    }

    public ClientDTO createClient (ClientCreateDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setCpfCnpj(dto.getCpfCnpj());

        Client savedClient = clientRepository.save(client);
        return convertToDTO(savedClient);
    }

    public Optional <ClientDTO> updateClient (Long id, ClientCreateDTO dto) {
        return clientRepository.findById(id).map(client -> {
            client.setName(dto.getName());
            client.setEmail(dto.getEmail());
            client.setPhone(dto.getPhone());
            client.setCpfCnpj(dto.getCpfCnpj());

            Client update = clientRepository.save(client);
            return convertToDTO(update);
        });
    }

    public boolean deleteClient (Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

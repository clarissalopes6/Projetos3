package projetosg10.survey.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projetosg10.survey.dto.ClientCreateDTO;
import projetosg10.survey.dto.ClientDTO;
import projetosg10.survey.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController (ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity <List<ClientDTO>> getAllClients() {
        List<ClientDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById (@PathVariable Long id) {
        return clientService.getClientById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity <ClientDTO> createClient (@RequestBody ClientCreateDTO dto) {
        ClientDTO created = clientService.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity <ClientDTO> updateClient (@PathVariable Long id,
    @RequestBody ClientCreateDTO dto) {
        return clientService.updateClient(id, dto).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteClient (@PathVariable Long id) {
        if (clientService.deleteClient(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    
}

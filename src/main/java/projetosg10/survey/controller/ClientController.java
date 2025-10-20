package projetosg10.survey.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetosg10.survey.dto.ClientCreateDTO;
import projetosg10.survey.dto.ClientDTO;
import projetosg10.survey.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clientService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody ClientCreateDTO dto) {
        try {
            ClientDTO created = clientService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, 
                                         @Valid @RequestBody ClientCreateDTO dto) {
        try {
            ClientDTO updated = clientService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            clientService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<ClientDTO> toggleActive(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clientService.toggleActive(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

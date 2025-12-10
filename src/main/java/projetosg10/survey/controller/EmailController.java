package projetosg10.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetosg10.survey.dto.EmailDTO;
import projetosg10.survey.dto.EmailSendDTO;
import projetosg10.survey.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-all")
    public ResponseEntity<String> sendEmailToAllClients(@RequestBody EmailDTO emailDTO) {
        try {
            emailService.sendEmailToAllClients(emailDTO.getSubject(), emailDTO.getMessage());
            return ResponseEntity.ok("Emails enviados com sucesso para todos os clientes!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao enviar emails: " + e.getMessage());
        }
    }

    @PostMapping("/send-selected")
    public ResponseEntity<String> sendEmailToSelectedClients(@RequestBody EmailSendDTO emailSendDTO) {
        try {
            emailService.sendEmailToSelectedClients(
                emailSendDTO.getClientIds(), 
                emailSendDTO.getSubject(), 
                emailSendDTO.getMessage()
            );
            return ResponseEntity.ok("Emails enviados com sucesso para " + emailSendDTO.getClientIds().size() + " cliente(s)!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao enviar emails: " + e.getMessage());
        }
    }
}

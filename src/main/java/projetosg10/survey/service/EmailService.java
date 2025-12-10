package projetosg10.survey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import projetosg10.survey.model.Client;
import projetosg10.survey.repository.ClientRepository;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ClientRepository clientRepository;

    public void sendEmailToAllClients(String subject, String message) {
        List<Client> clients = clientRepository.findAll();
        
        for (Client client : clients) {
            try {
                sendEmail(client.getEmail(), subject, message);
            } catch (Exception e) {
                System.err.println("Erro ao enviar email para " + client.getEmail() + ": " + e.getMessage());
            }
        }
    }

    public void sendEmailToSelectedClients(List<Long> clientIds, String subject, String message) {
        List<Client> clients = clientRepository.findAllById(clientIds);
        
        for (Client client : clients) {
            try {
                sendEmail(client.getEmail(), subject, message);
                System.out.println("Email enviado para: " + client.getEmail());
            } catch (Exception e) {
                System.err.println("Erro ao enviar email para " + client.getEmail() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("senaiprojetosg10@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        
        mailSender.send(message);
    }
}

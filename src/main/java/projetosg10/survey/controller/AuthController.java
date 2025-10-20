package projetosg10.survey.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetosg10.survey.dto.AdminDTO;
import projetosg10.survey.dto.LoginDTO;
import projetosg10.survey.dto.RegisterDTO;
import projetosg10.survey.service.AdminService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO dto, HttpSession session) {
        try {
            AdminDTO admin = adminService.register(dto);
            session.setAttribute("adminId", admin.getId());
            session.setAttribute("adminName", admin.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto, HttpSession session) {
        try {
            AdminDTO admin = adminService.login(dto);
            session.setAttribute("adminId", admin.getId());
            session.setAttribute("adminName", admin.getName());
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não autenticado");
        }
        
        try {
            AdminDTO admin = adminService.findById(adminId);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            session.invalidate();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão inválida");
        }
    }
}

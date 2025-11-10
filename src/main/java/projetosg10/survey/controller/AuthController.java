package projetosg10.survey.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetosg10.survey.dto.AdminDTO;
import projetosg10.survey.dto.LoginDTO;
import projetosg10.survey.dto.RegisterDTO;
import projetosg10.survey.service.AdminService;
import projetosg10.survey.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO dto) {
        try {
            AdminDTO admin = adminService.register(dto);
            
            String sessionId = UUID.randomUUID().toString();
            String token = jwtUtil.generateToken(admin.getEmail(), sessionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("admin", admin);
            response.put("token", token);
            response.put("sessionId", sessionId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        try {
            AdminDTO admin = adminService.login(dto);
            
            String sessionId = UUID.randomUUID().toString();
            String token = jwtUtil.generateToken(admin.getEmail(), sessionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("admin", admin);
            response.put("token", token);
            response.put("sessionId", sessionId);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkSession(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractUsername(token);
            
            if (jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado");
            }
            
            AdminDTO admin = adminService.findByEmail(email);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }
}

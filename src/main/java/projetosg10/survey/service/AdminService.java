package projetosg10.survey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetosg10.survey.dto.AdminDTO;
import projetosg10.survey.dto.LoginDTO;
import projetosg10.survey.dto.RegisterDTO;
import projetosg10.survey.model.Admin;
import projetosg10.survey.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Transactional
    public AdminDTO register(RegisterDTO dto) {
        if (adminRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Admin admin = new Admin();
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());

        admin.setPassword(dto.getPassword());
        admin.setActive(true);

        admin = adminRepository.save(admin);
        return toDTO(admin);
    }

    @Transactional(readOnly = true)
    public AdminDTO login(LoginDTO dto) {
        Admin admin = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        if (!admin.getActive()) {
            throw new RuntimeException("Usuário inativo");
        }

        if (!admin.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        return toDTO(admin);
    }

    @Transactional(readOnly = true)
    public AdminDTO findById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));
        return toDTO(admin);
    }

    private AdminDTO toDTO(Admin admin) {
        return new AdminDTO(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getActive(),
                admin.getCreatedAt()
        );
    }
}

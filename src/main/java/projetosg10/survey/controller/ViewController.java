package projetosg10.survey.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    @GetMapping("/clients")
    public String clients(HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/login";
        }
        return "clients";
    }
}

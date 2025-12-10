package projetosg10.survey.controller;

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
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/clients")
    public String clients() {
        return "clients";
    }

    @GetMapping("/surveys")
    public String surveys() {
        return "surveys";
    }

    @GetMapping("/send-surveys")
    public String sendSurveys() {
        return "send-surveys";
    }

    @GetMapping("/ai-analysis")
    public String aiAnalysis() {
        return "ai-analysis";
    }
}
package by.rymtsou.controller;

import by.rymtsou.model.Security;
import by.rymtsou.model.User;
import by.rymtsou.model.dto.RegistrationRequestDto;
import by.rymtsou.service.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Optional;

@Controller("/security")
public class SecurityController {

    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute @Valid RegistrationRequestDto requestDto,
                               BindingResult bindingResult, Model model, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("message", "Invalid registration data.");
            return "innerError";
        }
        try {
            Optional<User> registeredSecurity = securityService.registration(requestDto);
            if (registeredSecurity.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                model.addAttribute("message", "Registration failed.");
                return "innerError";
            }
            model.addAttribute("user", registeredSecurity.get());
            return "user";
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addAttribute("message", "Database error during registration.");
            return "innerError";
        }
    }

    @PostMapping("/update")
    public String updateSecurity(@ModelAttribute("security") Security security,
                                 Model model, HttpServletResponse response) {
        Optional<Security> updatedSecurity = securityService.updateSecurity(security);
        if (updatedSecurity.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "Security record update failed.");
            return "innerError";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return "user";
    }


    @GetMapping("/delete/{id}")
    public String showDeleteSecurityPage(@PathVariable("id") Long id, Model model, HttpServletResponse response) {
        Optional<Security> security = securityService.getSecurityById(id);
        if (security.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("message", "Security not found: id=" + id);
            return "innerError";
        }
        model.addAttribute("security", security.get());
        return "deleteSecurity";
    }

    @PostMapping("/delete{id}")
    public String deleteSecurity(@PathVariable("id") Long id,
                                 Model model, HttpServletResponse response) {
        Optional<Security> securityDeleted = securityService.deleteSecurity(id);
        if (securityDeleted.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "Security is not deleted.");
            return "innerError";
        }
        return "redirect:/user/users";
    }
}

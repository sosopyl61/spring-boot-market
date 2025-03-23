package by.rymtsou.controller;

import by.rymtsou.model.User;
import by.rymtsou.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            model.addAttribute("message", "No users found");
            return "innerError";
        }
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/create")
    public String getUserCreatePage() {
        return "createUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, Model model, HttpServletResponse response) {
        Optional<User> createdUser = userService.createUser(user);
        if (createdUser.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "User creation failed.");
            return "innerError";
        }
        model.addAttribute("user", createdUser.get());
        return "redirect:/user/users";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model, HttpServletResponse response) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("message", "User not found, id=" + id);
            return "innerError";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        model.addAttribute("user", user.get());
        return "user";
    }

    @GetMapping("/update/{id}")
    public String getUserUpdatePage(@PathVariable("id") Long userId, Model model, HttpServletResponse response) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("message", "User not found: id=" + userId);
            return "innerError";
        }
        model.addAttribute("user", user.get());
        return "editUser";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, Model model, HttpServletResponse response) {
        Optional<User> userUpdated = userService.updateUser(user);
        if (userUpdated.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "User is not updated.");
            return "innerError";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        model.addAttribute("user", userUpdated.get());
        return "user";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long userId, Model model, HttpServletResponse response) {
        Optional<User> deletedUser = userService.deleteUser(userId);
        if (deletedUser.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            model.addAttribute("message", "User is not deleted.");
            return "innerError";
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return "redirect:/user/users";
    }
}

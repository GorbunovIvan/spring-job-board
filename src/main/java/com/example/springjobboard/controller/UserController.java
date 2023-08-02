package com.example.springjobboard.controller;

import com.example.springjobboard.controller.util.ControllersUtil;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import com.example.springjobboard.util.UsersUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final ControllersUtil controllersUtil;
    private final UsersUtil usersUtil;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/my-page")
    public String getCurrentUserPage(Model model) {

        var user = getCurrentUser();
        if (user == null) {
            throw new RuntimeException("You are not authorized");
        }

        model.addAttribute("user", user);

        return "users/user";
    }

    @GetMapping("/my-page/edit")
    public String initUpdate( Model model) {

        var user = getCurrentUser();
        if (user == null) {
            throw new RuntimeException("You are not authorized");
        }

        model.addAttribute("user", user);

        return "users/updateForm";
    }

    @PostMapping("/my-page/edit")
    public String processUpdate(Model model,
                                @ModelAttribute @Valid User user, BindingResult bindingResult) {

        var userPersisted = getCurrentUser();
        if (user == null) {
            throw new RuntimeException("You are not authorized");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("user", user);
            return "users/updateForm";
        }

        userPersisted.setName(user.getName());
        userPersisted.setEmail(user.getEmail());

        if (!user.getPassword().equals(userPersisted.getPassword())) {
            userPersisted.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.update(userPersisted.getId(), userPersisted);

        return "redirect:/users/my-page";
    }

    @PostMapping("/my-page/delete")
    public String processDelete() {

        var user = getCurrentUser();
        if (user == null) {
            throw new RuntimeException("You are not authorized");
        }

        var result = userRepository.delete(user);
        if (!result) {
            throw new RuntimeException("Error during deleting user with id '%d'");
        }

        return "redirect:/";
    }
    
    @ModelAttribute("currentUser")
    private User getCurrentUser() {
        return usersUtil.getCurrentUser();
    }
}

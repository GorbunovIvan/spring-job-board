package com.example.springjobboard.controller;

import com.example.springjobboard.controller.util.ControllersUtil;
import com.example.springjobboard.model.users.Role;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final ControllersUtil controllersUtil;

    @GetMapping("/login")
    public String initLogin() {
        return "auth/loginForm";
    }

    @GetMapping("/register")
    public String initRegistration(Model model) {
        model.addAttribute("user", new User());
        return "auth/registerForm";
    }

    @PostMapping("/register")
    public String processRegistration(Model model, @ModelAttribute @Valid User user,
                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("user", user);
            return "auth/registerForm";
        }

        user.addRole(Role.USER);
        user.setIsActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var userPersisted = userRepository.save(user);

        return "redirect:/users/my-page";
    }
}

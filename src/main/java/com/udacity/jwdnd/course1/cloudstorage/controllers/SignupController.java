package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getSignupPage(@ModelAttribute("newUser") User user, Model model) {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute("newUser") User user, Model model, RedirectAttributes redirectAttributes) {
        String signupError = null;

        if (!userService.isUserNameAvailable(user.getUserName())) {
            signupError = "Username already exists";
        }

        if (signupError == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signupError = "Could not sign up. Please try again";
            }
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", "You successfully signed up!");
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", signupError);
            return "signup";
        }
    }
}

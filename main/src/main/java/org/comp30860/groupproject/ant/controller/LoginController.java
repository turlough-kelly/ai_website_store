package org.comp30860.groupproject.ant.controller;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;
import org.comp30860.groupproject.ant.persistence.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new AccountModel());

        return "register.html";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AccountModel account, RedirectAttributes redirectAttributes) {
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            redirectAttributes.addAttribute("error", "username");
            return "redirect:/register";
        }

        if (accountRepository.findByEmailAddress(account.getEmailAddress()) != null) {
            redirectAttributes.addAttribute("error", "email");
            return "redirect:/register";
        }

        accountRepository.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

}

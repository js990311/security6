package com.study.security6.controller.user;

import com.study.security6.controller.user.form.UserRegistForm;
import com.study.security6.user.repository.UserRepository;
import com.study.security6.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin(){
        return "user/login";
    }

    @GetMapping("/regist")
    public String getRegist(Model model){
        model.addAttribute("form", new UserRegistForm());
        return "user/regist";
    }

    @PostMapping("/regist")
    public String postRegist(@ModelAttribute UserRegistForm form, BindingResult bindingResult, Model model){
        boolean hasError = false;

        if(bindingResult.hasErrors()){
            hasError = true;
        }else {
            try {
                userService.createUser(
                        form.getUsername(),
                        form.getPassword()
                );
            }catch (Exception e){
                hasError = true;
            }
        }

        if(hasError){
            model.addAttribute("form", form);
            return "user/regist";
        }else {
            return "redirect:/";
        }
    }
}

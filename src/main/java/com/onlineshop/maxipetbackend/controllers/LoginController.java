package com.onlineshop.maxipetbackend.controllers;

import com.onlineshop.maxipetbackend.constants.UserConstant;
import com.onlineshop.maxipetbackend.dtos.UserDTO;
import com.onlineshop.maxipetbackend.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@CrossOrigin
@AllArgsConstructor
@SessionAttributes("userRole")
public class LoginController {
    private final UserService userService;

    /**
     * Method for user authentication.
     * @param email The email address of the user
     * @param password The user's password
     * @return ModelAndView for redirecting to the appropriate pages based on the user's role
     */
    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email, @RequestParam("password") String password){
        UserDTO localUser = userService.findUserByEmailAndPassword(email, password);
        UserConstant.userDTO = localUser;
        ModelAndView modelAndView = new ModelAndView();
        if (localUser != null) {
            if ("admin".equals(localUser.getRole())) {
                modelAndView.setViewName("redirect:/admin");
            } else {
                modelAndView.setViewName("redirect:/customer");
            }
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    /**
     * Method for user logout.
     * @return ModelAndView for redirecting to the home page after logout
     */
    @GetMapping("/logout")
    public ModelAndView logout(){
        UserConstant.userDTO = null;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}

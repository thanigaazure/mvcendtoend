package com.sbmvcprojects.mvcendtoend.controller;

import com.sbmvcprojects.mvcendtoend.entity.UserEntity;
import com.sbmvcprojects.mvcendtoend.model.User;
import com.sbmvcprojects.mvcendtoend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String getUser(Model model) {
        model.addAttribute("user", new User());

        Iterable<UserEntity> allUsers = userRepository.findAll();

        List<User> userList = new ArrayList<>();

        for (UserEntity entity : allUsers) {
            User user = new User();
            user.setUserID(entity.getUserId());
            user.setName(entity.getName());
            user.setEmail(entity.getEmail());
            user.setPhone(entity.getPhone());
            userList.add(user);
        }

        model.addAttribute("users", userList);
        return "index";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
//        model.addAttribute("user", new User());
        if(result.hasErrors())  {
            return "index";
        }

        UserEntity userEntity = new UserEntity();
        if(user.getUserID() != null) {
            userEntity.setUserId(user.getUserID());
        }
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());

        userRepository.save(userEntity);
        return "redirect:/";
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable String id, Model model) {
        Optional<UserEntity> userEntity = userRepository.findById(Integer.parseInt(id));
        User user = new User();
        if(userEntity.isPresent()) {
            user.setUserID(userEntity.get().getUserId());
            user.setName(userEntity.get().getName());
            user.setEmail(userEntity.get().getEmail());
            user.setPhone(userEntity.get().getPhone());
        }
        model.addAttribute(user);
        model.addAttribute("action", "update");

        Iterable<UserEntity> allUsers = userRepository.findAll();

        List<User> userList = new ArrayList<>();

        for (UserEntity entity : allUsers) {
            User user1 = new User();
            user1.setUserID(entity.getUserId());
            user1.setName(entity.getName());
            user1.setEmail(entity.getEmail());
            user1.setPhone(entity.getPhone());
            userList.add(user1);
        }

        model.addAttribute("users", userList);
        return "index";
    }


    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable String id, Model model) {
        userRepository.deleteById(Integer.parseInt(id));

        model.addAttribute("user", new User());

        Iterable<UserEntity> allUsers = userRepository.findAll();

        List<User> userList = new ArrayList<>();

        for (UserEntity entity : allUsers) {
            User user1 = new User();
            user1.setUserID(entity.getUserId());
            user1.setName(entity.getName());
            user1.setEmail(entity.getEmail());
            user1.setPhone(entity.getPhone());
            userList.add(user1);
        }

        model.addAttribute("users", userList);
        return "index";
    }

}

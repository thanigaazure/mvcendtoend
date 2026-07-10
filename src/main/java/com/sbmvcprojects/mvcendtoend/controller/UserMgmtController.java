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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/userMgmt")
public class UserMgmtController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String getUsers(Model model) {
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
        return "userMgmt";
    }

    @PostMapping("/addUser")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model)  {
        model.addAttribute("user", new User());
        if(result.hasErrors())  {
            return "error";
        }

        UserEntity userEntity = new UserEntity();
        if(user.getUserID() != null) {
            userEntity.setUserId(user.getUserID());
        }
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());

        userRepository.save(userEntity);

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

        return "userMgmt";
    }
}

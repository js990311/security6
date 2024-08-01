package com.study.security6.domain.user.controller;

import com.study.security6.domain.role.dto.RoleDto;
import com.study.security6.domain.role.service.RoleService;
import com.study.security6.domain.user.dto.UserDto;
import com.study.security6.domain.user.role.dto.UserRoleDto;
import com.study.security6.domain.user.role.service.UserRoleService;
import com.study.security6.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/manager")
@Controller
public class UserManagerController {
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final RoleService roleService;

    @GetMapping("")
    public String getUsers(Model model){
        List<UserDto> users = userService.readAllUser();
        model.addAttribute("users", users);
        return "/manager/users";
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable("userId") Long userId, Model model){
        UserDto user = userService.readByUserId(userId);
        List<UserRoleDto> userRoles = userRoleService.readUserRoleByUserId(userId);
        List<RoleDto> roles = roleService.getRoles();
        model.addAttribute("user",user);
        model.addAttribute("userRoles",userRoles);
        model.addAttribute("roles", roles);
        return "/manager/user";
    }

    @PostMapping("/{userId}/user-role")
    public String addUserRole(@PathVariable("userId") Long userId, @RequestParam(name = "roles") List<Long> roleIds){
        userRoleService.addUserRoles(userId, roleIds);
        return "redirect:/manager/"+userId;
    }

    @DeleteMapping("/{userId}/user-role/{userRoleId}")
    public String deleteUserRole(@PathVariable("userId") Long userId, @PathVariable("userRoleId") Long userRoleId){
        userRoleService.deleteUserRole(userRoleId);
        return "redirect:/manager/"+userId;
    }
}

package com.study.security6.domain.role.controller;

import com.study.security6.domain.role.dto.RoleDto;
import com.study.security6.domain.role.service.RoleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Boolean.FALSE;

@RequestMapping("/manager/role")
@RequiredArgsConstructor
@Controller
public class RoleController {
    private final RoleService roleService;

    @GetMapping()
    public String getRoles(Model model){
        List<RoleDto> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "/manager/role/roles";
    }

    @GetMapping("/{roldId}")
    public String getRole(@PathVariable("roldId") Long roleId, Model model){
        RoleDto role = roleService.getRole(roleId);
        model.addAttribute("role", role);
        return "/manager/role/role";
    }

    @PostMapping("/create")
    public String createRole(@RequestParam("name") String roleName, @RequestParam("expression") String expression, @RequestParam(name = "isBanned", defaultValue = "false") boolean isBanned){
        roleService.createRole(roleName, expression, isBanned);
        return "redirect:/manager/role";
    }

    @GetMapping("/create")
    public String getCreateRoleForm(){
        return "/manager/role/createRole";
    }

    @PutMapping("/{roleId}")
    public String updateRole(@PathVariable("roleId") Long roleId,@RequestParam("name") String roleName){
        roleService.updateName(roleId, roleName);
        return "redirect:/manager/role/"+roleId;
    }

    @DeleteMapping("/{roleId}")
    public String deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
        return "redirect:/manager/role";
    }
}

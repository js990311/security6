package com.study.security6.domain.role.controller;

import com.study.security6.domain.role.dto.RoleDto;
import com.study.security6.domain.role.service.RoleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/role")
@RequiredArgsConstructor
@Controller
public class RoleController {
    private final RoleService roleService;

    @GetMapping()
    public String getRoles(Model model){
        List<RoleDto> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "role/roles";
    }

    @GetMapping("/{roldId}")
    public String getRole(@PathVariable("roldId") Long roleId, Model model){
        RoleDto role = roleService.getRole(roleId);
        model.addAttribute("role", role);
        return "role/role";
    }

    @PostMapping("/create")
    public String createRole(@RequestParam("name") String roleName, @RequestParam("expression") String expression){
        roleService.createRole(roleName, expression);
        return "redirect:/role";
    }

    @GetMapping("/create")
    public String getCreateRoleForm(){
        return "role/createRole";
    }

    @PutMapping("/{roleId}")
    public String updateRole(@PathVariable("roleId") Long roleId,@RequestParam("name") String roleName){
        roleService.updateName(roleId, roleName);
        return "redirect:/role/"+roleId;
    }

    @DeleteMapping("/{roleId}")
    public String deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
        return "redirect:/role";
    }
}

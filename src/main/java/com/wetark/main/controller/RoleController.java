package com.wetark.main.controller;

import com.wetark.main.model.BaseService;
import com.wetark.main.model.user.role.Role;
import com.wetark.main.model.user.role.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<Role> {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        super(roleService);
        this.roleService = roleService;
    }
}

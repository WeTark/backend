package com.wetark.main.model.user.role;

import com.wetark.main.model.BaseService;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role> {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }
}

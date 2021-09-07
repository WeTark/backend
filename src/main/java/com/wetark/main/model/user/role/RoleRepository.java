package com.wetark.main.model.user.role;

import com.wetark.main.model.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role> {
  Optional<Role> findByName(ERole name);
}

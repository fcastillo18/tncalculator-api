package com.tncalculator.tncalculatorapi.repository;

import com.tncalculator.tncalculatorapi.constant.ERole;
import com.tncalculator.tncalculatorapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
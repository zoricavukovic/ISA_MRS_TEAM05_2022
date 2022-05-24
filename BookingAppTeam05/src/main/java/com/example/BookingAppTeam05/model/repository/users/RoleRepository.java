package com.example.BookingAppTeam05.model.repository.users;

import com.example.BookingAppTeam05.model.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

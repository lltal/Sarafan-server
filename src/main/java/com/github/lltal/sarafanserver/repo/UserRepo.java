package com.github.lltal.sarafanserver.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.lltal.sarafanserver.domain.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>
{

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

}

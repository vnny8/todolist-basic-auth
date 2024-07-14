package br.com.vinicius.todolist.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.vinicius.todolist.user.model.User;


public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
    
}

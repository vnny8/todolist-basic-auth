package br.com.vinicius.todolist.task.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.vinicius.todolist.task.model.Task;
import java.util.UUID;


public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByIdUser(UUID idUser);
    
}

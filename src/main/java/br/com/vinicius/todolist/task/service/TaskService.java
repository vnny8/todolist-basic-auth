package br.com.vinicius.todolist.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vinicius.todolist.task.model.Task;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.vinicius.todolist.task.repository.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) throws Exception{

        var currentDate = LocalDateTime.now();
        
        if (currentDate.isAfter(task.getBeginAt())){
            throw new Exception("The begin date of the task is before the current date.");
        }
        if (task.getBeginAt().isAfter(task.getEndAt())){
            throw new Exception("The begin date of the task is after the end date.");
        }

        return taskRepository.save(task);
    }

    public List<Task> listAll(String idUser){
        return taskRepository.findByIdUser(UUID.fromString(idUser));
    }

    public Task updateTask(Task task, HttpServletRequest request, UUID idTask) throws Exception{

        Optional<Task> taskConfirm = taskRepository.findById(idTask);
        if (!taskConfirm.isPresent()){
            throw new Exception("The ID don't belong to any task.");
        }
        Task originalTask = taskConfirm.get();
        task.setId(idTask);
        var idUserTask = (UUID) request.getAttribute("idUser");
        if (originalTask.getIdUser().equals(idUserTask)){
            task.setIdUser(idUserTask);
            task.setCreatedAt(originalTask.getCreatedAt());
            if (task.getBeginAt() == null){
                task.setBeginAt(originalTask.getBeginAt());
            }
            if (task.getEndAt() == null){
                task.setEndAt(originalTask.getEndAt());
            }
            if (task.getPriority() == null){
                task.setPriority(originalTask.getPriority());
            }
            if (task.getDescription() == null){
                task.setDescription(originalTask.getDescription());
            }
            if (task.getTitle() == null){
                task.setTitle(originalTask.getTitle());
            }
            return this.taskRepository.save(task);
        } else {
            throw new Exception("This task don't belong to this User.");
        }
    }
}

package br.com.vinicius.todolist.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vinicius.todolist.task.model.Task;
import br.com.vinicius.todolist.task.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task, HttpServletRequest request){
        try {
            task.setIdUser((UUID) request.getAttribute("idUser"));
            return ResponseEntity.created(null).body(taskService.createTask(task));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Task>> listTasks(HttpServletRequest request){
        try {
            return ResponseEntity.ok().body(taskService.listAll(request.getAttribute("idUser").toString()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update/{idTask}")
    public ResponseEntity<?> updateTask(@RequestBody Task task, HttpServletRequest request, @PathVariable UUID idTask){
        try{
            return ResponseEntity.status(202).body(taskService.updateTask(task, request, idTask));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

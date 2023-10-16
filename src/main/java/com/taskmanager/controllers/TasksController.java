package com.taskmanager.controllers;


import com.taskmanager.dto.CreateTaskDTO;
import com.taskmanager.dto.ErrorResponseDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.dto.UpdateTaskDTO;
import com.taskmanager.entities.TaskEntity;
import com.taskmanager.services.NoteService;
import com.taskmanager.services.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TaskService taskservice;
    private final NoteService noteService;
    private ModelMapper modelMapper =  new ModelMapper();

    public TasksController(TaskService taskService, NoteService noteService) {

        this.taskservice = taskService;
        this.noteService = noteService;
    }

    @GetMapping("")
    public ResponseEntity<List<TaskEntity>> getTasks() {
        var tasks = taskservice.getTasks();
        return  ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable("id") int id) {
        var task = taskservice.getTaskById(id);
        var notes = noteService.getNotesForTask(id);
        if(task == null) {
            return  ResponseEntity.notFound().build();
        }
        var taskResponse = modelMapper.map(task, TaskResponseDTO.class);
        taskResponse.setNotes(notes);
        return ResponseEntity.ok(taskResponse);
    }

    @PostMapping("")
    public ResponseEntity<TaskEntity> addTask(@RequestBody CreateTaskDTO body) throws ParseException {
        var task = taskservice.addTask(body.getTitle(), body.getDescription(), body.getDeadline());

        return ResponseEntity.ok(task);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception e) {
        if( e instanceof ParseException) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO("Invalid date format"));
        }
        return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Internal Server Error"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable("id") int id, @RequestBody UpdateTaskDTO body) throws ParseException {
        var task = taskservice.updateTask(id, body.getDescription(), body.getDeadline(), body.getCompleted());
        if(task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }
}

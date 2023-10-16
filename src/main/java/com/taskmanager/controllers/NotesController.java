package com.taskmanager.controllers;

import com.taskmanager.dto.CreateNoteDTO;
import com.taskmanager.dto.CreateNoteResponseDTO;
import com.taskmanager.entities.NoteEntity;
import com.taskmanager.services.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/notes")
public class NotesController {
    private NoteService noteService;

    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("")
    public ResponseEntity<List<NoteEntity>> getNotes(@PathVariable("taskId") Integer taskId) {
        var notes = noteService.getNotesForTask(taskId);

        return ResponseEntity.ok(notes);
    }

    @PostMapping("")
    public ResponseEntity<CreateNoteResponseDTO> addNote(
            @PathVariable("taskId") Integer taskId,
            @RequestBody CreateNoteDTO body
            ) {
            var note = noteService.addNoteForTask(taskId, body.getTitle(), body.getBody());

            return ResponseEntity.ok(new CreateNoteResponseDTO(taskId, note));
    }
}

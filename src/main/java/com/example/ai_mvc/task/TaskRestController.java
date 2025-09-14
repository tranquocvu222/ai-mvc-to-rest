package com.example.ai_mvc.task;

import com.example.ai_mvc.common.CommonController;
import com.example.ai_mvc.common.ManageInfo;
import com.example.ai_mvc.common.MessageControl;
import com.example.ai_mvc.common.OnlineException;
import com.example.ai_mvc.common.SessionMng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController extends CommonController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        try {
            ManageInfo mngInfo = new SessionMng(context).getMngInfo();
            MessageControl messageControl = new MessageControl();

            TaskBean taskBean = taskService.getAllTasks(mngInfo, messageControl);

            if (messageControl.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(messageControl.getErrors());
            }

            List<TaskDTO> taskDTOs = taskBean.getTaskList().stream()
                    .map(task -> {
                        TaskDTO dto = new TaskDTO();
                        dto.setId(task.getId());
                        dto.setTitle(task.getTitle());
                        dto.setDescription(task.getDescription());
                        dto.setDueDate(task.getDueDate());
                        dto.setCompleted(task.isCompleted());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(taskDTOs);
        } catch (OnlineException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving tasks: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) {
        try {
            ManageInfo mngInfo = new SessionMng(context).getMngInfo();
            MessageControl messageControl = new MessageControl();

            TaskBean taskBean = taskService.getTask(mngInfo, id, messageControl);

            if (messageControl.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(messageControl.getErrors());
            }

            if (taskBean.getId() == null) {
                return ResponseEntity.notFound().build();
            }

            TaskDTO taskDTO = new TaskDTO(taskBean);
            return ResponseEntity.ok(taskDTO);
        } catch (OnlineException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving task: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO) {
        try {
            ManageInfo mngInfo = new SessionMng(context).getMngInfo();
            MessageControl messageControl = new MessageControl();

            TaskBean taskBean = taskDTO.toTaskBean();
            taskBean = taskService.createTask(mngInfo, taskBean, messageControl);

            if (messageControl.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(messageControl.getErrors());
            }

            TaskDTO createdTaskDTO = new TaskDTO(taskBean);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDTO);
        } catch (OnlineException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating task: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody TaskDTO taskDTO) {
        try {
            ManageInfo mngInfo = new SessionMng(context).getMngInfo();
            MessageControl messageControl = new MessageControl();

            if (!id.equals(taskDTO.getId())) {
                return ResponseEntity.badRequest()
                        .body("Path ID doesn't match request body ID");
            }

            TaskBean taskBean = taskDTO.toTaskBean();
            taskBean = taskService.updateTask(mngInfo, taskBean, messageControl);

            if (messageControl.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(messageControl.getErrors());
            }

            TaskDTO updatedTaskDTO = new TaskDTO(taskBean);
            return ResponseEntity.ok(updatedTaskDTO);
        } catch (OnlineException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating task: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        try {
            ManageInfo mngInfo = new SessionMng(context).getMngInfo();
            MessageControl messageControl = new MessageControl();

            taskService.deleteTask(mngInfo, id, messageControl);

            if (messageControl.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(messageControl.getErrors());
            }

            return ResponseEntity.noContent().build();
        } catch (OnlineException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting task: " + e.getMessage());
        }
    }
}


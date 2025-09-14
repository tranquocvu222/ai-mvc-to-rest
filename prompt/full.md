# ✅ Checklist Conversion: Spring MVC to Spring REST API

## 📌 Overview
This document defines the strategy for converting Spring MVC + JSP applications to Spring REST API. It focuses on the conversion of view-based controllers to RESTful endpoints, with specific emphasis on creating DTOs and transforming controller methods while maintaining compatibility with existing service and repository layers.

## 📁 Project Structure
```
ai_mvc/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   └── example/
│   │   │   │       └── ai_mvc/
│   │   │   │           ├── AiMvcApplication.java
│   │   │   │           ├── common/
│   │   │   │           │   ├── BaseBean.java
│   │   │   │           │   └── ... (utility classes)
│   │   │   │           └── task/
│   │   │   │               ├── Task.java (entity)
│   │   │   │               ├── TaskBean.java (unchanged) → TaskDTO.java (to be created)
│   │   │   │               ├── TaskController.java (to be converted)
│   │   │   │               ├── TaskRepository.java (unchanged)
│   │   │   │               └── TaskService.java (unchanged)
│   │   ├── resources/
│   │   │   └── application.properties
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/
│   │               └── task.jsp (will be unused in REST API)
└── pom.xml
```

## 🧠 Conversion Approach
- Convert the existing Spring MVC + JSP application to a RESTful API
- Create TaskDTO as a replacement for TaskBean for REST communication
- Modify TaskController to use RESTful endpoints
- Maintain backward compatibility with existing services
- Ensure proper HTTP status codes and response entities

## 🛠️ Conversion Tasks

Each task defines a specific conversion rule with detailed steps presented as subtasks. GitHub Copilot should review all tasks and subtasks to implement the conversion for each file.

## 🤖 Prompt Usage Example
Users provide the file path that needs conversion, for example:
`src/main/java/com/example/ai_mvc/task/TaskBean.java`
`src/main/java/com/example/ai_mvc/task/TaskController.java`

AI will perform the following steps:
1. Confirm understanding of the conversion requirements
2. List the points to be modified according to the checklist
3. Verify the result after conversion
4. Implement the changes directly to the provided file

Note: Before making changes, AI should confirm all requirements and conversion rules.

## 🛠️ Task 1: Create DTO from TaskBean

### 🎯 Subtask 1: Create a new DTO class without extending unnecessary base classes

#### Implementation Details

##### ✅ Create TaskDTO Class

- **Before conversion:**
```java
package com.example.ai_mvc.task;

import com.example.ai_mvc.common.ListControl;
import java.time.LocalDate;
import java.util.List;

public class TaskBean extends ListControl {
    private static final long serialVersionUID = 1L;
    private int selectedLink = 0;

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private List<Task> taskList;

    public int getSelectedLink() {
        return this.selectedLink;
    }

    public void setSelectedLink(int selectedLink) {
        this.selectedLink = selectedLink;
    }

    // Other getters and setters
}
```

- **After conversion:**
```java
package com.example.ai_mvc.task;

import java.time.LocalDate;
import java.util.List;

public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private List<Task> taskList;
    
    // Default constructor
    public TaskDTO() {}
    
    // Constructor from Task entity
    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.dueDate = task.getDueDate();
        this.completed = task.isCompleted();
    }

    // Getters and setters
}
```

#### 📌 Summary of changes - TaskDTO:
- Remove inheritance from ListControl base class
- Remove selectedLink and serialVersionUID fields (not needed for REST)
- Add a constructor that takes a Task entity for easy conversion
- Keep all essential fields and getters/setters for JSON serialization
- Package name remains the same

### 🎯 Subtask 2: Remove any unnecessary fields and methods

#### Implementation Details

##### ✅ Clean up unnecessary fields and methods

- **Before conversion:**
```java
public class TaskBean extends ListControl {
    private static final long serialVersionUID = 1L;
    private int selectedLink = 0;

    // Fields and methods
    
    public int getSelectedLink() {
        return this.selectedLink;
    }

    public void setSelectedLink(int selectedLink) {
        this.selectedLink = selectedLink;
    }
}
```

- **After conversion:**
```java
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private List<Task> taskList;

    // Only essential fields and methods remain
}
```

#### 📌 Summary of changes - Remove unnecessary fields:
- Remove selectedLink field and related methods
- Remove serialVersionUID (not needed for DTO)
- Keep only the essential fields related to the Task domain

## 🛠️ Task 2: Convert TaskController to REST API

### 🎯 Subtask 1: Convert Controller annotations and return types

#### Implementation Details

##### ✅ Update Controller annotations

- **Before conversion:**
```java
@Controller
@RequestMapping(value = "/task")
public class TaskController extends CommonController {
    // Controller methods
}
```

- **After conversion:**
```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    // Controller methods
}
```

#### 📌 Summary of changes - Controller annotations:
- Replace @Controller with @RestController
- Update RequestMapping to use RESTful URL pattern (/api/tasks)
- Remove inheritance from CommonController if not needed

### 🎯 Subtask 2: Replace view-returning methods with ResponseEntity

#### Implementation Details

##### ✅ Convert GET methods

- **Before conversion:**
```java
@GetMapping
public String init(Model model) throws OnlineException {
    ManageInfo mngInfo = new SessionMng(context).getMngInfo();
    TaskBean taskBean = taskService.getAllTasks(mngInfo, messageControl);

    model.addAttribute("menuBean", new MenuBean());
    model.addAttribute("body", taskBean);
    model.addAttribute("errors", messageControl.getErrors());

    return "task";
}
```

- **After conversion:**
```java
@GetMapping
public ResponseEntity<?> getAllTasks() {
    try {
        ManageInfo mngInfo = new SessionMng(context).getMngInfo();
        TaskBean taskBean = taskService.getAllTasks(mngInfo, messageControl);

        // Convert Task entities to DTOs
        List<TaskDTO> taskDTOs = taskBean.getTaskList().stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDTOs);
    } catch (OnlineException e) {
        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

##### ✅ Convert POST methods

- **Before conversion:**
```java
@PostMapping(params = "create")
public String create(@ModelAttribute("body") TaskBean taskBean, Model model) throws OnlineException {
    ManageInfo mngInfo = new SessionMng(context).getMngInfo();
    messageControl = new MessageControl();

    taskBean = taskService.createTask(mngInfo, taskBean, messageControl);

    model.addAttribute("menuBean", new MenuBean());
    model.addAttribute("body", taskBean);
    model.addAttribute("errors", messageControl.getErrors());

    return "task";
}
```

- **After conversion:**
```java
@PostMapping
public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO) {
    try {
        ManageInfo mngInfo = new SessionMng(context).getMngInfo();
        messageControl = new MessageControl();

        // Convert DTO to Bean for service
        TaskBean taskBean = new TaskBean();
        taskBean.setTitle(taskDTO.getTitle());
        taskBean.setDescription(taskDTO.getDescription());
        taskBean.setDueDate(taskDTO.getDueDate());
        taskBean.setCompleted(taskDTO.isCompleted());

        TaskBean resultBean = taskService.createTask(mngInfo, taskBean, messageControl);

        if (messageControl.hasErrors()) {
            return createErrorResponse(messageControl.getErrors(), HttpStatus.BAD_REQUEST);
        }

        // Return the newly created task
        List<Task> tasks = resultBean.getTaskList();
        Task createdTask = tasks.stream()
                .filter(task -> task.getTitle().equals(taskDTO.getTitle()))
                .findFirst()
                .orElse(tasks.get(tasks.size() - 1));

        return ResponseEntity.status(HttpStatus.CREATED).body(new TaskDTO(createdTask));
    } catch (OnlineException e) {
        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

#### 📌 Summary of changes - HTTP Methods:
- Replace String return types with ResponseEntity<?>
- Convert @ModelAttribute to @RequestBody for request data
- Remove Model attribute manipulation
- Convert JSP view returns to appropriate HTTP status codes
- Handle exceptions with appropriate HTTP status codes

### 🎯 Subtask 3: Add appropriate REST endpoints for CRUD operations

#### Implementation Details

##### ✅ Add GET by ID endpoint

- **Before conversion:**
```java
@GetMapping(params = "edit")
public String edit(@RequestParam("id") Long id, Model model) throws OnlineException {
    ManageInfo mngInfo = new SessionMng(context).getMngInfo();
    messageControl = new MessageControl();

    TaskBean taskBean = taskService.getTask(mngInfo, id, messageControl);

    model.addAttribute("menuBean", new MenuBean());
    model.addAttribute("body", taskBean);
    model.addAttribute("errors", messageControl.getErrors());

    return "task";
}
```

- **After conversion:**
```java
@GetMapping("/{id}")
public ResponseEntity<?> getTaskById(@PathVariable Long id) {
    try {
        ManageInfo mngInfo = new SessionMng(context).getMngInfo();
        messageControl = new MessageControl();

        TaskBean taskBean = taskService.getTask(mngInfo, id, messageControl);

        if (messageControl.hasErrors()) {
            return createErrorResponse(messageControl.getErrors(), HttpStatus.NOT_FOUND);
        }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(taskBean.getId());
        taskDTO.setTitle(taskBean.getTitle());
        taskDTO.setDescription(taskBean.getDescription());
        taskDTO.setDueDate(taskBean.getDueDate());
        taskDTO.setCompleted(taskBean.isCompleted());

        return ResponseEntity.ok(taskDTO);
    } catch (OnlineException e) {
        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

##### ✅ Add PUT endpoint for update

- **Before conversion:**
```java
@PostMapping(params = "update")
public String update(@ModelAttribute("body") TaskBean taskBean, Model model) throws OnlineException {
    ManageInfo mngInfo = new SessionMng(context).getMngInfo();
    messageControl = new MessageControl();

    taskBean = taskService.updateTask(mngInfo, taskBean, messageControl);

    model.addAttribute("menuBean", new MenuBean());
    model.addAttribute("body", taskBean);
    model.addAttribute("errors", messageControl.getErrors());

    return "task";
}
```

- **After conversion:**
```java
@PutMapping("/{id}")
public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
    try {
        ManageInfo mngInfo = new SessionMng(context).getMngInfo();
        messageControl = new MessageControl();

        // Convert DTO to Bean for service
        TaskBean taskBean = new TaskBean();
        taskBean.setId(id);
        taskBean.setTitle(taskDTO.getTitle());
        taskBean.setDescription(taskDTO.getDescription());
        taskBean.setDueDate(taskDTO.getDueDate());
        taskBean.setCompleted(taskDTO.isCompleted());

        TaskBean resultBean = taskService.updateTask(mngInfo, taskBean, messageControl);

        if (messageControl.hasErrors()) {
            return createErrorResponse(messageControl.getErrors(), HttpStatus.BAD_REQUEST);
        }

        // Create a response DTO with updated data
        TaskDTO responseDTO = new TaskDTO();
        responseDTO.setId(id);
        responseDTO.setTitle(taskDTO.getTitle());
        responseDTO.setDescription(taskDTO.getDescription());
        responseDTO.setDueDate(taskDTO.getDueDate());
        responseDTO.setCompleted(taskDTO.isCompleted());

        return ResponseEntity.ok(responseDTO);
    } catch (OnlineException e) {
        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

##### ✅ Add DELETE endpoint

- **Before conversion:**
```java
@GetMapping(params = "delete")
public String delete(@RequestParam("id") Long id, Model model) throws OnlineException {
    ManageInfo mngInfo = new SessionMng(context).getMngInfo();
    messageControl = new MessageControl();

    TaskBean taskBean = taskService.deleteTask(mngInfo, id, messageControl);

    model.addAttribute("menuBean", new MenuBean());
    model.addAttribute("body", taskBean);
    model.addAttribute("errors", messageControl.getErrors());

    return "task";
}
```

- **After conversion:**
```java
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteTask(@PathVariable Long id) {
    try {
        ManageInfo mngInfo = new SessionMng(context).getMngInfo();
        messageControl = new MessageControl();

        TaskBean resultBean = taskService.deleteTask(mngInfo, id, messageControl);

        if (messageControl.hasErrors()) {
            return createErrorResponse(messageControl.getErrors(), HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task deleted successfully");
        response.put("id", id);

        return ResponseEntity.ok(response);
    } catch (OnlineException e) {
        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

#### 📌 Summary of changes - REST endpoints:
- Use @PathVariable instead of @RequestParam for ID
- Add proper HTTP method annotations (@GetMapping, @PostMapping, @PutMapping, @DeleteMapping)
- Use RESTful URL patterns with IDs in the path
- Return appropriate HTTP status codes for CRUD operations
- Create helper methods for error responses


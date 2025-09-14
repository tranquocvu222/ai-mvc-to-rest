# ✅ Checklist Conversion: Spring MVC to Spring REST API

## 📌 Overview
This document defines the strategy for converting Spring MVC + JSP applications to Spring REST API. It focuses on the conversion of view-based controllers to RESTful endpoints, with specific emphasis on creating DTOs and create controller  while maintaining compatibility with existing service and repository layers.

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
│   │   │   │           └── xxx/
│   │   │   │               ├── XXX.java (entity)
│   │   │   │               ├── XXXBean.java (unchanged) → XXXDTO.java (to be created)
│   │   │   │               ├── XXXController.java (unchanged) -> XXXRestController (to be created)
│   │   │   │               ├── XXXRepository.java (unchanged)
│   │   │   │               └── XXXService.java (unchanged)
│   │   ├── resources/
│   │   │   └── application.properties
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/
│   │               └── xxx.jsp (will be unused in REST API)
└── pom.xml
```

## 🧠 Conversion Approach
- Convert the existing Spring MVC + JSP application to a RESTful API
- Create XXXDTO as a replacement for XXXBean for REST communication
- Create xxxRestController to use RESTful endpoints
- Maintain backward compatibility with existing services
- Ensure proper HTTP status codes and ResponseEntity usage

## 🛠️ Conversion Tasks

Each rule defines a specific conversion rule with detailed steps presented as Conversion Rules. GitHub Copilot should review all rules to implement the conversion for each file.
- All code compiles successfully
- No runtime errors occur during execution

## 🤖 Prompt Usage Example
Users provide the file path that needs conversion, for example:
`src/main/java/com/example/ai_mvc/xxx/XXXBean.java`
`src/main/java/com/example/ai_mvc/xxx/XXXController.java`

AI will perform the following steps:
1. Confirm understanding of the conversion requirements
2. List the points to be modified according to the checklist rule
3. Verify the result after conversion
4. Implement the changes directly to the provided file

Note: Before making changes, AI should confirm all requirements and conversion rules.

## 🛠️ Conversion Rules

### 1️⃣ Bean to DTO Conversion

| Role | Spring MVC Bean | REST DTO |
|------|----------------|----------|
| Purpose | Holds data for views | Data transfer between client-server |
| Inheritance | Extends framework classes | No inheritance (POJO) |
| Fields | Contains UI-specific fields | Only domain-relevant fields |
| Serialization | Implements Serializable | JSON serialization by Jackson |
| Methods | Contains UI helper methods | Only getters/setters and mapping |


**Example:**
```java
// Before: MVC Bean
public class UserBean extends ListControl {
    private int selectedTab = 0;
    private List<User> userList;
    // UI-specific fields & methods
}

// After: REST DTO
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    // Only domain fields + conversion constructor
    
    public UserDTO(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.email = entity.getEmail();
    }
}
```

### 2️⃣ Controller Conversion

| Aspect | Spring MVC | Spring REST |
|--------|------------|------------|
| Annotation | @Controller | @RestController |
| URL Pattern | /resource?action=x | /api/resources/{id} |
| Parameters | @RequestParam, @ModelAttribute | @PathVariable, @RequestBody |
| Return Type | String (view name) | ResponseEntity<?> |
| Response | Model attributes | Direct JSON with status code |
| Error Handling | JSP error pages | Status codes + error JSON |

**Example:**
```java
// Spring MVC Controller
@GetMapping(params = "edit")
public String edit(@RequestParam("id") Long id, Model model) {
    // Add to model, return view
    return "user";
}

// REST Controller
@GetMapping("/{id}")
public ResponseEntity<?> getUserById(@PathVariable Long id) {
    // Return entity with status code
    return ResponseEntity.ok(userDTO);
}
```

### 3️⃣ HTTP Method Mapping

| Action | Spring MVC | REST API |
|--------|------------|----------|
| List all | @GetMapping | @GetMapping("/api/resources") |
| Get one | @GetMapping(params="id") | @GetMapping("/api/resources/{id}") |
| Create | @PostMapping | @PostMapping("/api/resources") |
| Update | @PostMapping(params="update") | @PutMapping("/api/resources/{id}") |
| Delete | @GetMapping(params="delete") | @DeleteMapping("/api/resources/{id}") |


## 🔍 Implementation Steps

1. Create DTO classes matching your domain entities
2. Add entity-to-DTO conversion methods or constructors
3. Crest Controllers to RestControllers
4. Replace view returns with ResponseEntity returns
5. Structure URLs according to REST conventions
6. Implement proper error handling with status codes
7. Update security to work with REST endpoints

## ⚙️ Best Practices

- Use consistent naming conventions for endpoints
- Return appropriate HTTP status codes (200, 201, 404, etc.)
- Implement pagination for list endpoints
- Add validation for request DTOs
package com.example.ai_mvc.task;

import com.example.ai_mvc.common.BaseBean;
import com.example.ai_mvc.common.ManageInfo;
import com.example.ai_mvc.common.MessageControl;
import com.example.ai_mvc.common.OnlineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService extends BaseBean {
    private static final long serialVersionUID = 1L;

    @Autowired
    private TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public TaskBean getAllTasks(ManageInfo mngInf, MessageControl errCtl) throws OnlineException {
        try {
            TaskBean taskBean = new TaskBean();
            List<Task> tasks = taskRepository.findAll();
            taskBean.setTaskList(tasks);
            return taskBean;
        } catch (Exception e) {
            errCtl.addError("Failed to retrieve tasks: " + e.getMessage());
            throw new OnlineException("Error retrieving tasks", e);
        }
    }

    @Transactional
    public TaskBean createTask(ManageInfo mngInf, TaskBean taskBean, MessageControl errCtl) throws OnlineException {
        try {
            Task task = new Task();
            task.setTitle(taskBean.getTitle());
            task.setDescription(taskBean.getDescription());
            task.setDueDate(taskBean.getDueDate() != null ? taskBean.getDueDate() : LocalDate.now());
            task.setCompleted(taskBean.isCompleted());

            Task savedTask = taskRepository.save(task);

            // Refresh task list
            List<Task> tasks = taskRepository.findAll();
            taskBean.setTaskList(tasks);

            // Reset form fields
            taskBean.setId(null);
            taskBean.setTitle("");
            taskBean.setDescription("");
            taskBean.setDueDate(null);
            taskBean.setCompleted(false);

            return taskBean;
        } catch (Exception e) {
            errCtl.addError("Failed to create task: " + e.getMessage());
            throw new OnlineException("Error creating task", e);
        }
    }

    @Transactional
    public TaskBean updateTask(ManageInfo mngInf, TaskBean taskBean, MessageControl errCtl) throws OnlineException {
        try {
            Optional<Task> optTask = taskRepository.findById(taskBean.getId());
            if (optTask.isPresent()) {
                Task task = optTask.get();
                task.setTitle(taskBean.getTitle());
                task.setDescription(taskBean.getDescription());
                task.setDueDate(taskBean.getDueDate());
                task.setCompleted(taskBean.isCompleted());

                taskRepository.save(task);

                // Refresh task list
                List<Task> tasks = taskRepository.findAll();
                taskBean.setTaskList(tasks);

                // Reset form fields
                taskBean.setId(null);
                taskBean.setTitle("");
                taskBean.setDescription("");
                taskBean.setDueDate(null);
                taskBean.setCompleted(false);
            } else {
                errCtl.addError("Task not found with ID: " + taskBean.getId());
            }

            return taskBean;
        } catch (Exception e) {
            errCtl.addError("Failed to update task: " + e.getMessage());
            throw new OnlineException("Error updating task", e);
        }
    }

    @Transactional
    public TaskBean getTask(ManageInfo mngInf, Long id, MessageControl errCtl) throws OnlineException {
        try {
            TaskBean taskBean = new TaskBean();
            Optional<Task> optTask = taskRepository.findById(id);

            if (optTask.isPresent()) {
                Task task = optTask.get();
                taskBean.setId(task.getId());
                taskBean.setTitle(task.getTitle());
                taskBean.setDescription(task.getDescription());
                taskBean.setDueDate(task.getDueDate());
                taskBean.setCompleted(task.isCompleted());
            } else {
                errCtl.addError("Task not found with ID: " + id);
            }

            List<Task> tasks = taskRepository.findAll();
            taskBean.setTaskList(tasks);

            return taskBean;
        } catch (Exception e) {
            errCtl.addError("Failed to retrieve task: " + e.getMessage());
            throw new OnlineException("Error retrieving task", e);
        }
    }

    @Transactional
    public TaskBean deleteTask(ManageInfo mngInf, Long id, MessageControl errCtl) throws OnlineException {
        try {
            Optional<Task> optTask = taskRepository.findById(id);

            if (optTask.isPresent()) {
                taskRepository.deleteById(id);
            } else {
                errCtl.addError("Task not found with ID: " + id);
            }

            TaskBean taskBean = new TaskBean();
            // Refresh task list
            List<Task> tasks = taskRepository.findAll();
            taskBean.setTaskList(tasks);

            return taskBean;
        } catch (Exception e) {
            errCtl.addError("Failed to delete task: " + e.getMessage());
            throw new OnlineException("Error deleting task", e);
        }
    }
}

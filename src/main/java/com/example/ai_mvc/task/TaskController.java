// src/main/java/jp/go/abc/task/TaskController.java
package com.example.ai_mvc.task;

import com.example.ai_mvc.common.CommonController;
import com.example.ai_mvc.common.ManageInfo;
import com.example.ai_mvc.common.MenuBean;
import com.example.ai_mvc.common.MessageControl;
import com.example.ai_mvc.common.OnlineException;
import com.example.ai_mvc.common.ScreenControl;
import com.example.ai_mvc.common.SessionMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping(value = "/task")
public class TaskController extends CommonController {

    @Autowired
    private TaskService taskService;

    private MessageControl messageControl = new MessageControl();

    @GetMapping
    public String init(Model model) throws OnlineException {
        ManageInfo mngInfo = new SessionMng(context).getMngInfo();
        TaskBean taskBean = taskService.getAllTasks(mngInfo, messageControl);

        model.addAttribute("menuBean", new MenuBean());
        model.addAttribute("body", taskBean);
        model.addAttribute("errors", messageControl.getErrors());

        return "task";
    }

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
}
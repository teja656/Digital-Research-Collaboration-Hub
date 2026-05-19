package com.researchsphere.controller;

import com.researchsphere.dao.ProjectDao;
import com.researchsphere.dao.TaskDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/charts")
public class ChartController {

    private final TaskDao taskDao = new TaskDao();
    private final ProjectDao projectDao = new ProjectDao();

    @GetMapping("/tasks")
    public Map<String, Object> taskChart() {
        Map<String, Object> data = new HashMap<>();
        data.put("labels", Arrays.asList("To Do", "In Progress", "Done"));
        data.put("values", Arrays.asList(
                taskDao.countByStatus("TODO"),
                taskDao.countByStatus("IN_PROGRESS"),
                taskDao.countByStatus("DONE")));
        return data;
    }

    @GetMapping("/projects")
    public Map<String, Object> projectChart() {
        Map<String, Object> data = new HashMap<>();
        data.put("labels", Arrays.asList("Planning", "In Progress", "Completed"));
        data.put("values", Arrays.asList(
                projectDao.countByStatus("PLANNING"),
                projectDao.countByStatus("IN_PROGRESS"),
                projectDao.countByStatus("COMPLETED")));
        return data;
    }
}

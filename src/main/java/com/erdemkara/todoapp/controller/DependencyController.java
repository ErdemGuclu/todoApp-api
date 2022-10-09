package com.erdemkara.todoapp.controller;

import com.erdemkara.todoapp.data.entity.Dependency;
import com.erdemkara.todoapp.service.DependencyService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/dependencies")
public class DependencyController {
    private final DependencyService dependencyService;

    public DependencyController(DependencyService dependencyService)
    {
        this.dependencyService = dependencyService;
    }

    @GetMapping("/all/{todoItemId}")
    public Set<Dependency> findAllDependenciesByTodoItemId(@PathVariable int todoItemId) {
        return dependencyService.findAllDependenciesByTodoItemId(todoItemId);
    }

    @PostMapping("/addDependency")
    public Dependency addDependency(@RequestBody Dependency dependency) {
        return dependencyService.addDependency(dependency);
    }

    @DeleteMapping("/deleteAll/{todoItemId}")
    public void deleteAllDependenciesByTodoItemId(@PathVariable int todoItemId) {
        dependencyService.deleteAllDependenciesByTodoItemId(todoItemId);
    }

    @DeleteMapping("/delete")
    public void deleteDependencyByTodoItemId(@RequestParam("td") int todoItemId, @RequestParam("dep") int dependencyItemId) {
        dependencyService.deleteDependencyByTodoItemIdAndDependencyItemId(todoItemId, dependencyItemId);
    }
}

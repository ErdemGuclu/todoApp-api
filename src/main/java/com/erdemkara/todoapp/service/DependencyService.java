package com.erdemkara.todoapp.service;

import com.erdemkara.todoapp.data.entity.Dependency;
import com.erdemkara.todoapp.data.repos.IDependencyRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DependencyService {
    private final IDependencyRepository dependencyRepository;
    private final TodoItemService todoItemService;

    public DependencyService(IDependencyRepository dependencyRepository, @Lazy TodoItemService todoItemService)
    {
        this.dependencyRepository = dependencyRepository;
        this.todoItemService = todoItemService;
    }

    public Set<Dependency> findAllDependenciesByTodoItemId(int todoItemId) {
        return dependencyRepository.findAllByTodoItemId(todoItemId);
    }

    public Dependency addDependency(Dependency dependency) {
        int todoItemId = dependency.getTodoItem().getId();
        int dependencyItemId = dependency.getDependencyItemId();

        if (todoItemId != dependencyItemId)
            if (dependencyRepository.findByTodoItemIdAndDependencyItemId(todoItemId, dependencyItemId).size() < 1)
                if (dependencyRepository.findByTodoItemIdAndDependencyItemId(dependencyItemId, todoItemId).size() < 1)
                    if (todoItemService.getTodoItemById(dependencyItemId) != null
                    && todoItemService.getTodoItemById(todoItemId) != null) {
                        dependencyRepository.save(dependency);
                        return dependency;
                    }

        throw new IllegalStateException("Cannot add dependency!");
    }

    public void deleteDependencyByTodoItemIdAndDependencyItemId(int todoItemId, int dependencyItemId) {
        dependencyRepository.deleteByTodoItemIdAndDependencyItemId(todoItemId, dependencyItemId);
    }

    public void deleteAllDependenciesByTodoItemId(int todoItemId) {
        dependencyRepository.deleteAll(dependencyRepository.findAllByTodoItemId(todoItemId));
        dependencyRepository.deleteAll(dependencyRepository.findAllByDependencyItemId(todoItemId));
    }
}

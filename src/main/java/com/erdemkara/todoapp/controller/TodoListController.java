package com.erdemkara.todoapp.controller;

import com.erdemkara.todoapp.data.entity.TodoList;
import com.erdemkara.todoapp.service.TodoListService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lists")
public class TodoListController {
    private final TodoListService todoListService;

    public TodoListController(TodoListService todoListService)
    {
        this.todoListService = todoListService;
    }

    @GetMapping("/all")
    public Iterable<TodoList> getAllTodoLists() {
        return todoListService.getAllTodoLists();
    }

    @GetMapping("/id/{id}")
    public TodoList findTodoListById(@PathVariable int id) {
        return todoListService.findTodoListById(id);
    }

    @PostMapping("/save")
    public TodoList saveTodoList(@RequestBody TodoList todoList) {
        return todoListService.saveTodoList(todoList);
    }

    @PutMapping("/update")
    public TodoList updateTodoList(@RequestBody TodoList todoList) {
        return todoListService.updateTodoList(todoList);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTodoListById(@PathVariable int id) {
        todoListService.deleteTodoListById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllTodoLists() {
        todoListService.deleteAllTodoLists();
    }
}

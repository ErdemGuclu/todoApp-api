package com.erdemkara.todoapp.controller;

import com.erdemkara.todoapp.data.entity.TodoItem;
import com.erdemkara.todoapp.service.TodoItemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class TodoItemController {
    private final TodoItemService todoItemService;

    public TodoItemController(TodoItemService todoItemService)
    {
        this.todoItemService = todoItemService;
    }

    @GetMapping("/all")
    public Iterable<TodoItem> getAllTodoItems() {
        return todoItemService.getAllTodoItems();
    }

    @GetMapping("/getItemById")
    public TodoItem getTodoItemById(@RequestParam int id) {
        return todoItemService.getTodoItemById(id);
    }

    @GetMapping("/allItemsByListId/{listId}")
    public Iterable<TodoItem> getAllTodoItemsByListId(@PathVariable int listId) {
        return todoItemService.getAllTodoItemsByListId(listId);
    }

    @PostMapping("/save/{todoListId}")
    public TodoItem saveTodoItemByListId(@RequestBody TodoItem todoItem, @PathVariable int todoListId) {
        return todoItemService.saveTodoItem(todoItem);
    }

    @PutMapping("/update")
    public TodoItem updateTodoItem(@RequestBody TodoItem todoItem) {
        return todoItemService.updateTodoItem(todoItem);
    }

    @PutMapping("/changeStatusById/{todoItemId}")
    public TodoItem changeItemStatusByTodoItemId(@PathVariable int todoItemId) {
        return todoItemService.changeItemStatusByTodoItemId(todoItemId);
    }

    @DeleteMapping("/deleteItem/{id}")
    public void deleteItemById(@PathVariable int id) {
        todoItemService.deleteItemById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllItems() {
        todoItemService.deleteAllItems();
    }

    @DeleteMapping("/deleteAll/{listId}")
    public void deleteAllItemsByListId(@PathVariable int listId) {
        todoItemService.deleteAllItemsByListId(listId);
    }
}

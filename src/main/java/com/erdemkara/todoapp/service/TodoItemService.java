package com.erdemkara.todoapp.service;

import com.erdemkara.todoapp.data.entity.Dependency;
import com.erdemkara.todoapp.data.entity.TodoItem;
import com.erdemkara.todoapp.data.entity.TodoList;
import com.erdemkara.todoapp.data.repos.ITodoItemRepository;
import com.erdemkara.todoapp.exception.DataNotFoundException;
import com.erdemkara.todoapp.exception.ExistingDataException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TodoItemService {
    private final ITodoItemRepository todoItemRepository;
    private final DependencyService dependencyService;
    private final TodoListService todoListService;

    public TodoItemService(ITodoItemRepository todoItemRepository,
                           DependencyService dependencyService,
                           TodoListService todoListService)
    {
        this.todoItemRepository = todoItemRepository;
        this.dependencyService = dependencyService;
        this.todoListService = todoListService;
    }

    public TodoItem getTodoItemById(int todoItemId) {
        return todoItemRepository.findById(todoItemId)
                .orElseThrow(
                        () -> new DataNotFoundException("No item found with this id: " + todoItemId));
    }

    public Iterable<TodoItem> getAllTodoItemsByListId(int listId) {
        TodoList list = todoListService.findTodoListById(listId);

        return todoItemRepository.findAllByTodoListId(listId);
    }

    public Iterable<TodoItem> getAllTodoItems() {
        return todoItemRepository.findAll();
    }

    public TodoItem changeItemStatusByTodoItemId(int todoItemId) {
        TodoItem existingItem = getTodoItemById(todoItemId);

        if (existingItem.isStatus())
            existingItem.setStatus(false);
        else {
            for (Dependency dep : existingItem.getDependencies())
                if (!getTodoItemById(dep.getDependencyItemId()).isStatus())
                    return existingItem;

            existingItem.setStatus(true);
        }

        todoItemRepository.save(existingItem);
        return existingItem;
    }

    public TodoItem saveTodoItem(TodoItem todoItem) {
        List<TodoItem> match = StreamSupport.stream(getAllTodoItems().spliterator(), false)
                .filter(item -> item.getId() == todoItem.getId())
                .collect(Collectors.toList());

        if (match.size() > 0)
            throw new ExistingDataException("Todo Item already exists!");
        else
            return todoItemRepository.save(todoItem);
    }

    public TodoItem updateTodoItem(TodoItem todoItem) {
        TodoItem todoItemToUpdate = getTodoItemById(todoItem.getId());

        todoItemToUpdate.setName(todoItem.getName());
        todoItemToUpdate.setDeadline(todoItem.getDeadline());
        todoItemToUpdate.setDescription(todoItem.getDescription());
        todoItemToUpdate.setStatus(todoItem.isStatus());

        return todoItemRepository.save(todoItemToUpdate);
    }

    public void deleteItemById(int id) {
        try {
            todoItemRepository.deleteById(id);
            dependencyService.deleteAllDependenciesByTodoItemId(id);
        }
        catch (EmptyResultDataAccessException ex) {
            throw new DataNotFoundException("There is no item to delete with id: " + id);
        }
    }

    public void deleteAllItems() {
        todoItemRepository.deleteAll();
    }

    public void deleteAllItemsByListId(int listId) {
        Iterable<TodoItem> todoItems = todoItemRepository.findAllByTodoListId(listId);

        if (StreamSupport.stream(todoItems.spliterator(), false).findAny().isEmpty())
            throw new DataNotFoundException("There is no item to delete!");

        todoItemRepository.deleteAll(todoItems);
    }
}

package com.erdemkara.todoapp.service;

import com.erdemkara.todoapp.data.entity.TodoList;
import com.erdemkara.todoapp.data.repos.ITodoListRepository;
import com.erdemkara.todoapp.exception.DataNotFoundException;
import com.erdemkara.todoapp.exception.ExistingDataException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TodoListService {
    private final ITodoListRepository todoListRepository;

    public TodoListService(ITodoListRepository todoListRepository)
    {
        this.todoListRepository = todoListRepository;
    }

    public Iterable<TodoList> getAllTodoLists() {
        return todoListRepository.findAll();
    }

    public TodoList findTodoListById(int id) {
            return todoListRepository.findById(id)
                    .orElseThrow(
                            () -> new DataNotFoundException("No list found with this id: " + id));
    }

    public TodoList saveTodoList(TodoList todoList) {
        List<TodoList> match = StreamSupport.stream(getAllTodoLists().spliterator(), false)
                .filter(list -> list.getName().equals(todoList.getName()))
                .collect(Collectors.toList());

        if (match.size() > 0)
            throw new ExistingDataException("Todo List already exists!");
        else
            return todoListRepository.save(todoList);
    }

    public TodoList updateTodoList(TodoList todoList) {
        TodoList existingList = findTodoListById(todoList.getId());

        existingList.setName(todoList.getName());

        return todoListRepository.save(existingList);
    }

    public void deleteTodoListById(int id) {
        todoListRepository.deleteById(id);
    }

    public void deleteAllTodoLists() {
        todoListRepository.deleteAll();
    }
}

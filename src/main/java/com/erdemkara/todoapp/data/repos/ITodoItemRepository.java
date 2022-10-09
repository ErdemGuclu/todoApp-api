package com.erdemkara.todoapp.data.repos;

import com.erdemkara.todoapp.data.entity.TodoItem;
import org.springframework.data.repository.CrudRepository;

public interface ITodoItemRepository extends CrudRepository<TodoItem, Integer> {
    Iterable<TodoItem> findAllByTodoListId(int listId);
}

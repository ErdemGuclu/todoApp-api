package com.erdemkara.todoapp.data.repos;

import com.erdemkara.todoapp.data.entity.TodoList;
import org.springframework.data.repository.CrudRepository;

public interface ITodoListRepository extends CrudRepository<TodoList, Integer> {

}

package com.erdemkara.todoapp.data.repos;

import com.erdemkara.todoapp.data.entity.Dependency;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface IDependencyRepository extends CrudRepository<Dependency, Integer> {
    @Query(value = "select * from dependencies d where d.todo_item_id=?", nativeQuery = true)
    Set<Dependency> findAllByTodoItemId(int todoItemId);
    Iterable<Dependency> findAllByDependencyItemId(int dependencyItemId);

    @Query(value = "select * from dependencies d where d.todo_item_id=? and d.dependency_item_id=?", nativeQuery = true)
    List<Dependency> findByTodoItemIdAndDependencyItemId(int todoItemId, int dependencyItemId);

    @Modifying
    @Transactional
    @Query(value = "delete from dependencies d where d.todo_item_id=? and d.dependency_item_id =?", nativeQuery = true)
    void deleteByTodoItemIdAndDependencyItemId(int todoItemId, int dependencyItemId);
}


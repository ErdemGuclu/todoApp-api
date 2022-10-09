package com.erdemkara.todoapp.service;

import com.erdemkara.todoapp.data.entity.Dependency;
import com.erdemkara.todoapp.data.entity.TodoItem;
import com.erdemkara.todoapp.data.repos.IDependencyRepository;
import com.erdemkara.todoapp.data.repos.ITodoItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DependencyServiceTest {
    @Mock
    IDependencyRepository dependencyRepository;

    @Mock
    TodoItemService todoItemService;

    @InjectMocks
    DependencyService dependencyService;

    @Test
    @DisplayName("Should return all dependencies by todo item id")
    public void testFindAllDependenciesByTodoItemId_ShouldReturnAllDependencies_ByTodoItemId() {
        int itemId = 1;

        Set<Dependency> expectedDependencies = new HashSet<>();
        expectedDependencies.add(new Dependency());

        when(dependencyRepository.findAllByTodoItemId(anyInt())).thenReturn(expectedDependencies);

        Set<Dependency> actualDependencies = dependencyService.findAllDependenciesByTodoItemId(itemId);

        assertEquals(expectedDependencies, actualDependencies);
    }

    @Test
    @DisplayName("Should throw IllegalStateException if dependency is to the item itself")
    public void testAddDependency_WhenTodoItemId_Equals_DependencyItemId_ThenThrowIllegalStateException() {
        int dependencyItemId = 1;

        TodoItem todoItem = new TodoItem();
        todoItem.setId(1);

        Dependency dependency = new Dependency();
        dependency.setTodoItem(todoItem);
        dependency.setDependencyItemId(dependencyItemId);

        Throwable actualException = assertThrows(IllegalStateException.class,
                () -> dependencyService.addDependency(dependency));

        assertTrue(actualException.getMessage().contains("Cannot add dependency!"));
    }

    @Test
    @DisplayName("Should throw IllegalStateException if there is already a dependency between items")
    public void testAddDependency_WhenThereIsAlreadyADependencyBetweenItems_ThenThrowIllegalStateException() {
        int dependencyItemId = 2;

        TodoItem todoItem = new TodoItem();
        todoItem.setId(1);

        Dependency dependency = new Dependency();
        dependency.setTodoItem(todoItem);
        dependency.setDependencyItemId(dependencyItemId);

        List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(dependency);

        when(dependencyRepository.findByTodoItemIdAndDependencyItemId(todoItem.getId(), dependencyItemId))
                .thenReturn(dependencies);

        Throwable actualException = assertThrows(IllegalStateException.class,
                () -> dependencyService.addDependency(dependency));

        assertTrue(actualException.getMessage().contains("Cannot add dependency!"));
    }

    @Test
    @DisplayName("Should add given dependency to repository")
    public void testAddDependency_ShouldAddDependency() {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(1);
        todoItem.setName("Todo Item");

        TodoItem dependencyItem = new TodoItem();
        dependencyItem.setId(2);
        dependencyItem.setName("Dependency Item");

        Dependency expectedDependency = new Dependency();
        expectedDependency.setId(1);
        expectedDependency.setTodoItem(todoItem);
        expectedDependency.setDependencyItemId(dependencyItem.getId());

        when(dependencyRepository.save(any(Dependency.class))).thenReturn(expectedDependency);

        Dependency actual = dependencyService.addDependency(expectedDependency);

        assertEquals(expectedDependency.getId(), actual.getId());
        assertEquals(expectedDependency.getTodoItem(), actual.getTodoItem());
        assertEquals(expectedDependency.getDependencyItemId(), actual.getDependencyItemId());
    }

    @Test
    @DisplayName("Should delete dependency by dependency item id")
    public void shouldDeleteDependency_ByDependencyItemId() {
        int todoItemId = 1;
        int dependencyItemId = 2;

        dependencyService.deleteDependencyByTodoItemIdAndDependencyItemId(todoItemId, dependencyItemId);

        verify(dependencyRepository).deleteByTodoItemIdAndDependencyItemId(eq(todoItemId), eq(dependencyItemId));
    }

    @Test
    @DisplayName("Should delete all dependencies by todo item id")
    public void shouldDeleteAllDependencies_ByTodoItemId() {
        int todoItemId = 1;

        List<Dependency> dependencies = new ArrayList<>();

        dependencyService.deleteAllDependenciesByTodoItemId(todoItemId);

        verify(dependencyRepository).deleteAll(dependencies);
    }
}
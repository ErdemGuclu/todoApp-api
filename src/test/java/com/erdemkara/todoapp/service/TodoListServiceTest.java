package com.erdemkara.todoapp.service;

import com.erdemkara.todoapp.data.entity.TodoList;
import com.erdemkara.todoapp.data.repos.ITodoListRepository;
import com.erdemkara.todoapp.exception.DataNotFoundException;
import com.erdemkara.todoapp.exception.ExistingDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoListServiceTest {
    @Mock
    private ITodoListRepository todoListRepository;

    @InjectMocks
    private TodoListService todoListService;

    @Test
    @DisplayName("This should return all todo lists")
    public void shouldReturn_AllTodoLists() {
        List<TodoList> expected = new ArrayList<>();

        TodoList list1 = new TodoList();
        TodoList list2 = new TodoList();

        expected.add(list1);
        expected.add(list2);

        when(todoListRepository.findAll()).thenReturn(expected);

        Iterable<TodoList> actual = todoListService.getAllTodoLists();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("This should return todolist with given id")
    public void testGetTodoListById_ShouldReturnTodoList_IfTodoListExists_WithGivenId() {
        TodoList todoList = new TodoList(1, "list 1", null);

        Optional<TodoList> expected = Optional.of(todoList);

        when(todoListRepository.findById(anyInt())).thenReturn(expected);

        TodoList actual = todoListService.findTodoListById(expected.get().getId());

        assertNotNull(actual, "Todo list with id: " + expected.get().getId() + "not found!");
        assertEquals(expected.get().getId(), actual.getId());
        assertEquals(expected.get().getName(), actual.getName());
    }

    @Test
    @DisplayName("This should throw DataNotFoundException with valid exception message")
    public void testGetTodoListById_ShouldThrow_DataNotFoundException_IfToListWithGivenId_DoesNotExist() {
        int id = 1;
        String expectedMessage = "No list found with this id: " + id;

        Throwable actualException = assertThrows(DataNotFoundException.class, () ->
                                    todoListService.findTodoListById(1));

        assertTrue(actualException.getMessage().contains(expectedMessage));
    }

    @Test
    @DisplayName("Should save the list to the repository")
    public void testSaveTodoList_WhenGivenTodoListDoesNotExistInDatabase_ThenSaveNewList() {
        TodoList todoList = new TodoList();
        todoList.setId(1);
        todoList.setName("List 1");

        when(todoListRepository.save(any(TodoList.class))).thenReturn(todoList);

        TodoList actual = todoListService.saveTodoList(todoList);

        assertEquals(todoList.getId(), actual.getId());
        assertEquals(todoList.getName(), actual.getName());
    }

    @Test
    @DisplayName("Should throw exception if there is a list with given item's name")
    public void testSaveTodoList_ShouldThrowExistingDataException_IfThereIsAList_WithTheSameName() {
        TodoList todoList = new TodoList();
        todoList.setId(1);
        todoList.setName("List 1");

        List<TodoList> expectedList = new ArrayList<>();
        expectedList.add(todoList);

        when(todoListRepository.findAll()).thenReturn(expectedList);

        Throwable actualException = assertThrows(ExistingDataException.class,
                () -> todoListService.saveTodoList(todoList));

        assertTrue(actualException.getMessage().contains("Todo List already exists!"));
    }

    @Test
    @DisplayName("Should update and return existing todo list with given body")
    public void testUpdateTodoList_WhenGivenTodoListExist_ThenShouldReturnExistingList() {
        TodoList todoListUpdate = new TodoList();
        todoListUpdate.setId(1);
        todoListUpdate.setName("List Update");

        TodoList existingList = new TodoList();
        existingList.setId(1);
        existingList.setName("Existing List");

        Optional<TodoList> listOptional = Optional.of(existingList);

        when(todoListRepository.findById(anyInt())).thenReturn(listOptional);
        when(todoListRepository.save(any(TodoList.class))).thenReturn(existingList);

        TodoList actual = todoListService.updateTodoList(todoListUpdate);

        assertEquals(todoListUpdate.getId(), actual.getId());
        assertEquals(todoListUpdate.getName(), actual.getName());
    }

    @Test
    @DisplayName("Should delete list with given id")
    public void shouldDeleteTodoList_WithGivenId() {
        int listId = 1;

        todoListService.deleteTodoListById(listId);

        verify(todoListRepository).deleteById(eq(listId));
    }

    @Test
    @DisplayName("Should delete all todo lists")
    public void shouldDeleteAllTodoLists() {
        todoListService.deleteAllTodoLists();

        verify(todoListRepository).deleteAll();
    }
}
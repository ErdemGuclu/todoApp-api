package com.erdemkara.todoapp.service;

import com.erdemkara.todoapp.data.entity.Dependency;
import com.erdemkara.todoapp.data.entity.TodoItem;
import com.erdemkara.todoapp.data.repos.ITodoItemRepository;
import com.erdemkara.todoapp.exception.DataNotFoundException;
import com.erdemkara.todoapp.exception.ExistingDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceTest {
    @Mock
    ITodoItemRepository todoItemRepository;

    @Mock
    DependencyService dependencyService;

    @Mock
    TodoListService todoListService;

    @InjectMocks
    TodoItemService todoItemService;

    @Test
    @DisplayName("If given todo item id exists, should return item with existing id")
    public void testGetTodoItemById_WhenTodoItemIdExists_ShouldReturnTodoItemWithGivenId() {
        TodoItem expected = new TodoItem();
        expected.setId(1);
        expected.setName("Item 1");

        when(todoItemRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        TodoItem actual = todoItemService.getTodoItemById(expected.getId());

        assertNotNull(actual, "Todo list with id: " + expected.getId() + "not found!");
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    @DisplayName("If item with given id doesn't exist then this should throw DataNotFoundException with valid message")
    public void testGetTodoItemById_WhenTodoItemIdDoesNotExist_ShouldThrowDataNotFoundException() {
        int id = 1;
        String expectedMessage = "No item found with this id: " + id;

        Throwable actualException = assertThrows(DataNotFoundException.class, () ->
                todoItemService.getTodoItemById(1));

        assertTrue(actualException.getMessage().contains(expectedMessage));
    }

    @Test
    @DisplayName("If todo list with given id exists then should return all items in that list")
    public void testGetAllTodoItemsByListId_WhenGivenListIdExists_ShouldReturnAllTodoList() {
        int listId = 1;

        List<TodoItem> allItemsExpected = new ArrayList<>();

        TodoItem todoItem1 = new TodoItem();
        TodoItem todoItem2 = new TodoItem();

        allItemsExpected.add(todoItem1);
        allItemsExpected.add(todoItem2);

        when(todoItemRepository.findAllByTodoListId(anyInt())).thenReturn(allItemsExpected);

        Iterable<TodoItem> actual = todoItemService.getAllTodoItemsByListId(listId);

        assertEquals(allItemsExpected, actual);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException if Todo List doesn't exist with given id")
    public void testGetAllTodoItemsByListId_ShouldThrowDataNotFoundException_WhenListWithGivenIdDoesNotExist() {
        int listId = 1;

        String expectedMessage = "No item found with this id: " + listId;

        Throwable actualException = assertThrows(DataNotFoundException.class, () ->
                todoItemService.getTodoItemById(listId));

        assertTrue(actualException.getMessage().contains(expectedMessage));
    }

    @Test
    @DisplayName("Should return all todo items")
    public void testGetAllTodoItems_shouldReturnAllTodoItems() {
        TodoItem todoItem1 = new TodoItem();
        TodoItem todoItem2 = new TodoItem();

        List<TodoItem> allItemsExpected = new ArrayList<>();

        allItemsExpected.add(todoItem1);
        allItemsExpected.add(todoItem2);

        when(todoItemRepository.findAll()).thenReturn(allItemsExpected);

        Iterable<TodoItem> actual = todoItemService.getAllTodoItems();

        assertEquals(allItemsExpected, actual);
    }

    @Test
    @DisplayName("Should set item status to false if it's current status is true")
    public void testChangeItemStatus_WhenItemStatusIsTrue_ShouldChangeItToFalse() {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(1);
        todoItem.setStatus(true);
        todoItem.setDependencies(new HashSet<>());

        Optional<TodoItem> todoItemOptional = Optional.of(todoItem);

        when(todoItemRepository.findById(anyInt())).thenReturn(todoItemOptional);

        TodoItem actual = todoItemService.changeItemStatusByTodoItemId(1);

        assertNotNull(actual);
        assertFalse(actual.isStatus());
    }

    @Test
    @DisplayName("If item status and dependency item status are false then should return existing item")
    public void testChangeItemStatus_ShouldCheckDependencyItemStatus_BeforeChangingItemStatus() {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(1);
        todoItem.setStatus(false);

        Optional<TodoItem> todoItemOpt = Optional.of(todoItem);

        Dependency dependency = new Dependency();
        dependency.setTodoItem(todoItem);
        dependency.setDependencyItemId(2);

        Set<Dependency> todoItemDependency = new HashSet<>();
        todoItemDependency.add(dependency);

        todoItem.setDependencies(todoItemDependency);

        TodoItem dependencyItem = new TodoItem();
        dependencyItem.setId(2);
        dependencyItem.setStatus(false);

        Optional<TodoItem> dependencyItemOpt = Optional.of(dependencyItem);

        //There are 2 getTodoItemById calls in changeItemStatusByTodoItemId method
        when(todoItemRepository.findById(1)).thenReturn(todoItemOpt);
        when(todoItemRepository.findById(2)).thenReturn(dependencyItemOpt);

        TodoItem actual = todoItemService.changeItemStatusByTodoItemId(1);

        assertNotNull(actual);
        assertFalse(actual.isStatus());
    }

    @Test
    @DisplayName("If item status is false but dependency item status is true then should set item status to true")
    public void testChangeItemStatus_WhenDependencyItemStatusIsTrue_ThenSetItemStatusToTrue() {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(1);
        todoItem.setStatus(false);

        Optional<TodoItem> todoItemOpt = Optional.of(todoItem);

        Dependency dependency = new Dependency();
        dependency.setTodoItem(todoItem);
        dependency.setDependencyItemId(2);

        Set<Dependency> todoItemDependency = new HashSet<>();
        todoItemDependency.add(dependency);

        todoItem.setDependencies(todoItemDependency);

        TodoItem dependencyItem = new TodoItem();
        dependencyItem.setId(2);
        dependencyItem.setStatus(true);

        Optional<TodoItem> dependencyItemOpt = Optional.of(dependencyItem);

        when(todoItemRepository.findById(1)).thenReturn(todoItemOpt);
        when(todoItemRepository.findById(2)).thenReturn(dependencyItemOpt);

        TodoItem actual = todoItemService.changeItemStatusByTodoItemId(1);

        assertNotNull(actual);
        assertTrue(actual.isStatus());
    }

    @Test
    @DisplayName("Should save the item to repository")
    public void testSaveTodoItem_ShouldSaveItemToRepository_WhenThereIsNotAnExistingItemWithGivenId() {
        TodoItem todoItemToSave = new TodoItem();
        todoItemToSave.setId(1);
        todoItemToSave.setName("Item 1");

        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItemToSave);

        TodoItem actual = todoItemService.saveTodoItem(todoItemToSave);

        assertEquals(todoItemToSave.getId(), actual.getId());
        assertEquals(todoItemToSave.getName(), actual.getName());
    }

    @Test
    @DisplayName("Should throw ExistingDataException")
    public void testSaveTodoItem_WhenItemWithGivenIdExists_ThenShouldThrowExistingDataException() {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(1);
        todoItem.setName("Item 1");

        List<TodoItem> expectedList = new ArrayList<>();
        expectedList.add(todoItem);

        when(todoItemRepository.findAll()).thenReturn(expectedList);

        Throwable actualException = assertThrows(ExistingDataException.class,
                () -> todoItemService.saveTodoItem(todoItem));

        assertTrue(actualException.getMessage().contains("Todo Item already exists!"));
    }

    @Test
    @DisplayName("should update and return existing item with given id")
    public void testUpdateTodoItem_WhenItemWithGivenIdExists_ThenShouldUpdateAndReturnItem() {
        TodoItem todoItemUpdate = new TodoItem();
        todoItemUpdate.setId(1);
        todoItemUpdate.setName("List name update");
        todoItemUpdate.setDescription("Description update");
        todoItemUpdate.setDeadline(LocalDate.parse("2022-05-21"));

        TodoItem existingItem = new TodoItem();
        existingItem.setId(1);
        existingItem.setName("Existing list name");
        existingItem.setDescription("Existing description");
        existingItem.setDeadline(LocalDate.parse("2020-01-08"));

        Optional<TodoItem> optExistingItem = Optional.of(existingItem);

        when(todoItemRepository.findById(anyInt())).thenReturn(optExistingItem);
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(existingItem);

        TodoItem actual = todoItemService.updateTodoItem(todoItemUpdate);

        assertEquals(todoItemUpdate.getId(), actual.getId());
        assertEquals(todoItemUpdate.getName(), actual.getName());
        assertEquals(todoItemUpdate.getDeadline(), actual.getDeadline());
        assertEquals(todoItemUpdate.getDescription(), actual.getDescription());
    }

    @Test
    @DisplayName("Should delete item with given id")
    public void testDeleteItemById_ShouldDeleteItemWithGivenId() {
        int id = 1;

        todoItemService.deleteItemById(1);

        verify(todoItemRepository).deleteById(1);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException if there is no item with given id")
    public void testDeleteItemById_WhenThereIsNoItemWithGivenId_ThenThrowDataNotFoundException() {
        int id = 1;

        String expectedMessage = "There is no item to delete with id: " + id;

        doThrow(new DataNotFoundException("There is no item to delete with id: " + id))
                .when(todoItemRepository).deleteById(anyInt());

        Throwable actualException = assertThrows(DataNotFoundException.class,
                () -> todoItemService.deleteItemById(id));

        verify(todoItemRepository).deleteById(id);

        assertTrue(actualException.getMessage().contains(expectedMessage));
    }

    @Test
    @DisplayName("Should delete all items")
    public void shouldDeleteAllItems() {
        todoItemService.deleteAllItems();

        verify(todoItemRepository).deleteAll();
    }

    @Test
    @DisplayName("Should throws DataNotFoundException if there is no item in the list")
    public void testDeleteAllItemsByListId_WhenThereIsNoItemInTheList_ThenThrowDataNotFoundException() {
        int listId = 1;
        String expectedMessage = "There is no item to delete!";

        //empty list of items to return
        List<TodoItem> expectedItems = new ArrayList<>();

        when(todoItemRepository.findAllByTodoListId(anyInt())).thenReturn(expectedItems);

        Throwable actualException = assertThrows(DataNotFoundException.class,
                () -> todoItemService.deleteAllItemsByListId(listId));

        assertTrue(actualException.getMessage().contains(expectedMessage));
    }

    @Test
    @DisplayName("Should delete all items by list id")
    public void testDeleteAllItemsByListId_ShouldDeleteAllItemsByListId() {
        int listId = 1;

        TodoItem item1 = new TodoItem();
        item1.setId(1);
        item1.setName("Item 1");

        List<TodoItem> expectedItems = new ArrayList<>();
        expectedItems.add(item1);

        when(todoItemRepository.findAllByTodoListId(anyInt())).thenReturn(expectedItems);

        todoItemService.deleteAllItemsByListId(listId);

        verify(todoItemRepository).deleteAll(expectedItems);
    }
}
package com.erdemkara.todoapp.data.entity;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "todo_lists")
public class TodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "todoListId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "todo_items", nullable = false)
    private Set<TodoItem> todoItems;

    public TodoList()
    {}

    public TodoList(int id, String name, Set<TodoItem> todoItems)
    {
        this.id = id;
        this.name = name;
        this.todoItems = todoItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("todo_items")
    public Set<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(Set<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }
}

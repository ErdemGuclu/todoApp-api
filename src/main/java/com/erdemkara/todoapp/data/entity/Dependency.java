package com.erdemkara.todoapp.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "dependencies")
public class Dependency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_item_id", nullable = false)
    //@JsonBackReference prevents infinite recursion(with @JsonManagedReference on OneToMany side in TodoItem)
    @JsonBackReference
    private TodoItem todoItem;

    @Column(name = "dependency_item_id", nullable = false)
    private int dependencyItemId;

    public Dependency()
    {}

    public Dependency(int id, TodoItem todoItem, int dependencyItemId)
    {
        this.id = id;
        this.todoItem = todoItem;
        this.dependencyItemId = dependencyItemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TodoItem getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(TodoItem todoItem) {
        this.todoItem = todoItem;
    }

    public int getDependencyItemId() {
        return dependencyItemId;
    }

    public void setDependencyItemId(int dependencyItemId) {
        this.dependencyItemId = dependencyItemId;
    }
}

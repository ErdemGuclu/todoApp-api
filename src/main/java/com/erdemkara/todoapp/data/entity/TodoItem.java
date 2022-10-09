package com.erdemkara.todoapp.data.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "todo_items")
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;
    private LocalDate deadline;

    @Column(nullable = false)
    private boolean status;

    @Column(name = "todo_list_id", nullable = false)
    private int todoListId;

    @OneToMany(mappedBy = "todoItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JsonManagedReference prevents infinite recursion(with @JsonBackReference on OneToMany side in Dependency)
    @JsonManagedReference
    private Set<Dependency> dependencies;

    public TodoItem()
    {}

    public TodoItem(int id, String name, String description, LocalDate deadline,
                    boolean status, int todoListId, Set<Dependency> dependencies)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.todoListId = todoListId;
        this.dependencies = dependencies;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @JsonGetter("todo_list_id")
    public int getTodoListId() {
        return todoListId;
    }

    public void setTodoListId(int todoListId) {
        this.todoListId = todoListId;
    }

    public Set<Dependency> getDependencies() {
        return dependencies;
        //return dependencies.stream().filter(dependency -> todoItemService.getTodoItemById(dependency.getDependencyItemId()).isPresent()).collect(Collectors.toSet());
    }

    public void setDependencies(Set<Dependency> dependencies) {
        this.dependencies = dependencies;
    }
}

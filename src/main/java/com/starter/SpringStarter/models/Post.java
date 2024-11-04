package com.starter.SpringStarter.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity  // marks a class as a database entity in the table, which will be mapped to database
@Getter  // automatically generates the getter methods, so we need not to create them manually
@Setter  // automatically generates the setter methods, so we need not to create them manually
@NoArgsConstructor  // defines a constructor without argument
public class Post {
    
    @Id // marks as primary key
    @GeneratedValue(strategy = GenerationType.AUTO) // automatically generates suitable values for the id field.
    private Long id;

    @NotEmpty(message = "Title cannot be empty!")
    private String title;

    // columns are being created without @Column, but, to define some attributes of a particular column like name
    //, type, text-type, etc, we use @Column
    @Column(columnDefinition = "TEXT") 
    @NotEmpty(message = "Body can't be empty!") 
    private String body;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne //This annotation specify that many Posts can be associated with one Account.
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = true) //account_id is the foreign key column in the Post table.
    private Account account;
}

// The Post class serves as a representation of a data entity in your 
// application, typically corresponding to a table in a database. 

// Annotations like @Entity, @Id, and @GeneratedValue are used to specify 
// how the class and its fields should be mapped to the database.

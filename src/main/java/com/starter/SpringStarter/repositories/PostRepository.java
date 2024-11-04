package com.starter.SpringStarter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starter.SpringStarter.models.Post;

@Repository // The @Repository annotation in Spring is used to indicate that a class is a 
            // Data Access Object (DAO), responsible for interacting with the database. 
            // It is typically applied to classes that provide the implementation for data access operations.
public interface PostRepository extends JpaRepository<Post, Long>{ // 'JpaRepository' provides a set of methods for basic CRUD operations
    
}
//PostModel is the type of the entity that this repository will manage. This means PostRepository will handle PostModel objects.
//Long is the type of the primary key for the PostModel entity. This tells Spring Data JPA that the primary key of PostModel is of type Long.

// Using an interface that extends JpaRepository (or another Spring Data repository interface) significantly 
// reduces the need for manual creation of CRUD operations.

// JpaRepository can be extended with an interface only, rather than a class

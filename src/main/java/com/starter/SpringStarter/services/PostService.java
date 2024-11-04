package com.starter.SpringStarter.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.starter.SpringStarter.models.Post;
import com.starter.SpringStarter.repositories.PostRepository;

@Service // marks a class as a service layer component of the project.
public class PostService { // actually performing the job when the data enters the service layer.
    
    @Autowired
    PostRepository postRepository;

    public Optional<Post> getById(Long id){ 
        return postRepository.findById(id);
    }                
    // The Optional is used to handle cases where the entity might not exist in the database,
    // preventing NullPointerException and providing a way to handle the absence of a value gracefully.

    public List<Post> getAll(){
        return postRepository.findAll();
    }
    // Uses the repository’s findAll() method to fetch all PostModel entities from the database.

    public Page<Post> getAll(int offset, int pageSize, String field){
        return postRepository.findAll(PageRequest.of(offset, pageSize).withSort(Direction.ASC, field));
    }

    public void delete(Post post){
        postRepository.delete(post);
    }
    // Uses the repository’s delete() method to remove the specified PostModel entity from the database.
    

    public Post save(Post post){
        if(post.getId() == null){
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
    // Adds logic to set the createdAt field if the post is new (i.e., has a null ID) and 
    // then uses the repository’s save() method to persist the entity.
}

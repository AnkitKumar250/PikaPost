package com.starter.SpringStarter.Controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.starter.SpringStarter.models.Account;
import com.starter.SpringStarter.models.Post;
import com.starter.SpringStarter.services.AccountService;
import com.starter.SpringStarter.services.PostService;

import jakarta.validation.Valid;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, Principal principal) { // The Model object is used to pass
                                                                                     // data from the controller to the
                                                                                     // view (e.g., a Thymeleaf template
                                                                                     // or JSP page)
        Optional<Post> optionalPost = postService.getById(id); // This line calls the getById method of PostService to
                                                               // retrieve the post with the specified ID.
        // The result is wrapped in an Optional, which helps safely handle the case
        // where the post might not be found (i.e., it might be null).
        // Without Optional, the getById(id) method would return null if a post with the
        // given ID doesn't exist.
        // You would then need to perform an explicit null check

        // The issue with this approach is that it can lead to NullPointerException if
        // you forget to check for null.
        // Optional makes the possibility of an absent value explicit and forces you to
        // handle it.

        // Optional encapsulates the concept of an "absent" value. Instead of dealing
        // directly with null, which can be
        // ambiguous and error-prone, Optional provides a clear, well-defined way to
        // handle the absence of a value.
        String authUser = "email";
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post); // The post is added to the model with the key "post", making it
                                              // accessible in the view.

            if (principal != null) {
                authUser = principal.getName();
            }
            if (authUser.equals(post.getAccount().getEmail())) {
                model.addAttribute("isOwner", true);
            } else {
                model.addAttribute("isOwner", false);
            }
            return "post_views/post";
        } else {
            return "404";
        }
    }
    // The controller retrieves data (the post) and passes it to a view template.
    // The view template then dynamically renders the post content based on the data
    // provided.

    // The controller also handles cases where a post is not found by checking the
    // Optional. If the post doesn't exist,
    // the user is shown a 404 error page, enhancing the user experience by
    // providing meaningful feedback when something goes wrong.

    // Principal gets the current logged-in user

    @GetMapping("/post/add")
    public String addPost(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_views/post_add";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/post/add")
    @PreAuthorize("isAuthenticated()")
    public String addPostHandler(@Valid @ModelAttribute Post post, Principal principal, BindingResult result) {

        if(result.hasErrors()){
            return "post_views/post_add";
        }

        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        if (post.getAccount().getEmail().compareToIgnoreCase(authUser) < 0) {
            return "redirect:/?error";
        }
        postService.save(post);
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_views/post_edit";
        } else {
            return "404";
        }
    }

    @PostMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@Valid @ModelAttribute Post post, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()){
            return "post_views/post_edit";
        }

        Optional<Post> optionalPost = postService.getById(id);
        if(optionalPost.isPresent()){
            Post existingPost = optionalPost.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            postService.save(existingPost);
        }
        return "redirect:/post/"+post.getId();
    }

    @GetMapping("/post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id){
        Optional<Post> optionalPost = postService.getById(id);
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            postService.delete(post);
            return "redirect:/";
        }else{
            return "redirect:/?error";
        }
    }
}

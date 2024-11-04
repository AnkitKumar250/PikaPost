package com.starter.SpringStarter.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.starter.SpringStarter.models.Account;
import com.starter.SpringStarter.services.AccountService;
import com.starter.SpringStarter.services.EmailService;
import com.starter.SpringStarter.util.AppUtil;
import com.starter.SpringStarter.util.email.EmailDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Value("${site.domain}")
    private String site_domain;

    // This annotation tells
    // Spring to inject a value from the application's properties file (e.g.,
    // application.properties or application.yml). The key here is
    // password.token.reset.timeout.minutes. Spring will look for this key in the
    // properties file and inject its value into the password_token_timeout
    // variable.
    @Value("${password.token.reset.timeout.minutes}")
    private int password_token_timeout;

    @GetMapping("/register")
    public String register(Model model) {

        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
    }

    // When you use @ModelAttribute on a method parameter in a controller method
    // that handles
    // form submissions (like in a @PostMapping method), Spring automatically binds
    // the form
    // data to the object. This means the values entered in the form fields will be
    // set to the
    // corresponding properties of the model object.

    // the data the user enters into a web form is automatically captured and stored
    // in a Java
    // object on the server side. This Java object is typically a simple Java class
    // with
    // properties (fields) that match the form fields.
    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account_views/register";
        }

        accountService.save(account);
        return "redirect:/"; // redirect to home page once the user clicks the Register button in the form.
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return "account_views/profile";
        } else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String post_profile(@Valid @ModelAttribute Account account, BindingResult result, Principal principal,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            return "account_views/profile";
        }
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Account account_by_id = accountService.findById(account.getId()).get();
            account_by_id.setAge(account.getAge());
            account_by_id.setDate_of_birth(account.getDate_of_birth());
            account_by_id.setFirstname(account.getFirstname());
            account_by_id.setGender(account.getGender());
            account_by_id.setLastname(account.getLastname());
            account_by_id.setPassword(account.getPassword());

            accountService.save(account_by_id);
            SecurityContextHolder.clearContext(); // clearing the security context to logout the user
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // invalidating the session to logout the user
            }
            return "redirect:/";
        } else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String update_photo(@RequestParam MultipartFile file, RedirectAttributes attributes, Principal principal) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", "No file uploaded");
            return "redirect:/profile";
        } else {
            @SuppressWarnings("null")
            // Get the original filename from the uploaded file and clean it to remove any
            // unsafe characters
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                // Define the length of the random string to generate and set flags to include
                // letters and numbers
                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = true;

                // Generate a random string for the photo filename
                @SuppressWarnings("deprecation")
                String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
                // Append the original filename to the generated string to create a unique
                // filename
                String final_photo_name = generatedString + fileName;
                // Get the absolute path where the file will be stored by combining the upload
                // path and the unique filename
                String absolute_fileLocation = AppUtil.get_upload_path(final_photo_name);

                // Create a Path object representing the file location
                Path path = Paths.get(absolute_fileLocation);
                // Copy the file content from the input stream to the target location, replacing
                // any existing file
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                attributes.addFlashAttribute("message", "Photo uploaded suzzessfuly");

                String authUser = "email";
                if (principal != null) {
                    authUser = principal.getName();
                }

                Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
                if (optionalAccount.isPresent()) {
                    Account account = optionalAccount.get();
                    Account account_by_id = accountService.findById(account.getId()).get();
                    String relative_fileLocation = "/uploads/" + final_photo_name;
                    account_by_id.setPhoto(relative_fileLocation);
                    accountService.save(account_by_id);
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
                return "redirect:/profile";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/profile?error";
    }

    @GetMapping("/forgot-password")
    public String forgot_password(Model model) {
        return "account_views/forgot_password";
    }

    @PostMapping("/reset-password")
    public String reset_password(@RequestParam("email") String _email, RedirectAttributes attributes, Model model) {
        Optional<Account> optionalAccount = accountService.findOneByEmail(_email);
        if (optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            String reset_token = UUID.randomUUID().toString();
            account.setToken(reset_token);
            account.setPassword_reset_token_expiry(LocalDateTime.now().plusMinutes(password_token_timeout)); // sets the
                                                                                                             // expiration
                                                                                                             // time to
                                                                                                             // the
                                                                                                             // current
                                                                                                             // time
                                                                                                             // plus the
                                                                                                             // timeout
                                                                                                             // period.
            accountService.save(account);
            String reset_message = "This is the reset password link : " + site_domain + "change-password?token="
                    + reset_token;
            EmailDetails emailDetails = new EmailDetails(account.getEmail(), reset_message, "Reset Password");
            if (emailService.sendSimpleEmail(emailDetails) == false) {
                attributes.addFlashAttribute("error", "Error while sending email, contact admin!");
                return "redirect:/forgot-password";
            }
            attributes.addFlashAttribute("message", "Password reset email sent!");
            return "redirect:/login";
        } else {
            attributes.addFlashAttribute("error", "No user found with the email provided!");
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/change-password")
    public String change_password(Model model, @RequestParam("token") String token, RedirectAttributes attributes){
        if(token.equals("")){
            attributes.addFlashAttribute("error", "Invalid Token!");
            return "redirect:/forgot-pasword";
        }
        Optional<Account> optionalAccount = accountService.findByToken(token);
        if(optionalAccount.isPresent()){
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(optionalAccount.get().getPassword_reset_token_expiry())){
                attributes.addFlashAttribute("error", "Token expired!");
                return "redirect:/forgot-password";
            } 
            model.addAttribute("account", account);
            return "account_views/change_password";
        }
        attributes.addFlashAttribute("error", "invalid token!");
        return "redirect:/forgot-password";
    }

   @PostMapping("/change-password")
    public String post_change_password(@ModelAttribute Account account, RedirectAttributes attributes){
        Account account_by_id = accountService.findById(account.getId()).get();
        account_by_id.setPassword(account.getPassword());
        account_by_id.setToken("");
        accountService.save(account_by_id);
        attributes.addFlashAttribute("message", "Password Updated!");
        return "redirect:/login";
    } 
}

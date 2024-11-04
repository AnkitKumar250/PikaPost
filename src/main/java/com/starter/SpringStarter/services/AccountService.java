package com.starter.SpringStarter.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.starter.SpringStarter.models.Account;
import com.starter.SpringStarter.models.Authority;
import com.starter.SpringStarter.repositories.AccountRepository;
import com.starter.SpringStarter.util.constants.Roles;

@Service
public class AccountService implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository; 

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account){ // here, we will be saving the account in the database through the account
                                        // save() method of AccountService class, which will be done by calling in the
                                        // save() method of JpaRepository through AccountRepository interface;
        account.setPassword(passwordEncoder.encode(account.getPassword()));      
        //if no role is defined for user, then, by default we will be adding a role to user
        if (account.getRole() == null || account.getRole().isEmpty()) {
            account.setRole(Roles.USER.getRole());
        }
        if(account.getPhoto() == null){

            account.setPhoto("/images/default_profile_pic.png");
        }
        return accountRepository.save(account);
    }

 @Override
 //This method is part of the UserDetailsService interface and is responsible 
 //for retrieving user details based on the username (in this case, the email). 
 //It's used by Spring Security to authenticate and authorize users.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(email);
        if(!optionalAccount.isPresent()){
            throw new UsernameNotFoundException("Account not found!");
        }

        Account account = optionalAccount.get();

        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));

        for(Authority _auth : account.getAuthorities()){
            grantedAuthority.add(new SimpleGrantedAuthority(_auth.getName()));
        }

        return new User(account.getEmail(), account.getPassword(), grantedAuthority);
    } 

    public Optional<Account> findOneByEmail(String email){
        return accountRepository.findOneByEmailIgnoreCase(email);
    }

    public Optional<Account> findById(long id){
        return accountRepository.findById(id);
    }

    public Optional<Account> findByToken(String token){
        return accountRepository.findByToken(token);
    }
    
}

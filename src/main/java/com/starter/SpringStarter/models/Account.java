package com.starter.SpringStarter.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

//import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Email(message = "Email Error!")
    @NotEmpty(message = "Email missing!")
    private String email;

    @NotEmpty(message = "Password missing!")
    private String password;

    @NotEmpty(message = "Firstname missing!")
    private String firstname;

    @NotEmpty(message = "Lastname missing!")
    private String lastname;

    private String gender;

    @Min(value = 16)
    @Max(value = 99)
    private int age;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_of_birth;

    private String photo;

    private String role;

    // This is a bidirectional one-to-many relationship between Account and Post.
    // - The Account entity is on the "one" side of the relationship and contains a
    // collection of Post entities.
    // - The Post entity is on the "many" side of the relationship and has a
    // reference to a single Account.

    @OneToMany(mappedBy = "account") // @OneToMany indicates that one Account can have multiple Post entities
                                     // associated with it.
                                     // The mappedBy attribute in JPA's @OneToMany annotation specifies which field
                                     // in the child entity
                                     // (the "many" side) owns the relationship and contains the foreign key
                                     // reference to the parent entity (the "one" side).
    private List<Post> posts;

    // The @ManyToMany annotation in JPA (Java Persistence API) is used to map a
    // many-to-many relationship
    // between two entities. This type of relationship occurs when multiple records
    // in one entity can be
    // associated with multiple records in another entity. In a database, this is
    // typically represented by a
    // join table that holds the foreign keys of the related entities.


    private String token;

    private LocalDateTime password_reset_token_expiry;


    @ManyToMany
    @JoinTable( // Defines the join table that will hold the relationship between Account and
                // Authority.
            // This table will contain two foreign keys: one for the Account and one for the
            // Authority.
            name = "account_authority",
            // Defines the foreign key column in the join table (account_authority) that
            // refers to the Account entity.
            joinColumns = { @JoinColumn(name = "account_id", referencedColumnName = "id") },
            // Defines the foreign key column in the join table (account_authority) that
            // refers to the Authority entity.
            inverseJoinColumns = { @JoinColumn(name = "authority_id", referencedColumnName = "id") })
    private Set<Authority> authorities = new HashSet<>();

    /*
     * When using this @ManyToMany relationship, JPA will generate three tables:
     * 
     * account:
     * Stores account details, such as id, email, password, etc.
     * 
     * authority:
     * Stores the authority details, such as id, name, etc.
     * 
     * account_authority (the join table):
     * This table links the account and authority entities.
     * It will have two foreign key columns: account_id (refers to the id field in
     * the account table) and authority_id (refers to the id field in the authority
     * table).
     */
}

package com.shadril.taskmanagementjava.entity;

import com.shadril.taskmanagementjava.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(unique=true, nullable=false)
    @Email
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, length=15)
    private String firstname;

    @Column(nullable=false, length=15)
    private String lastname;

    @Column(nullable=false)
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
}

package com.shadril.taskmanagementjava.entity;

import com.shadril.taskmanagementjava.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String title;

    @Column(nullable = true, length = 250)
    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private TaskStatus status;

    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
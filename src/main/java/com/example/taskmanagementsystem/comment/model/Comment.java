package com.example.taskmanagementsystem.comment.model;

import com.example.taskmanagementsystem.task.model.Task;
import com.example.taskmanagementsystem.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_task", referencedColumnName = "id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User user;

    @Column
    private String text;

    @Column
    private LocalDateTime created;
}

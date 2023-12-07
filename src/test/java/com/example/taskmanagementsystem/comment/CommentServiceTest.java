package com.example.taskmanagementsystem.comment;

import com.example.taskmanagementsystem.comment.dto.NewCommentDto;
import com.example.taskmanagementsystem.comment.dto.OutCommentDto;
import com.example.taskmanagementsystem.comment.model.Comment;
import com.example.taskmanagementsystem.comment.repository.CommentRepository;
import com.example.taskmanagementsystem.comment.service.CommentService;
import com.example.taskmanagementsystem.exception.ConflictException;
import com.example.taskmanagementsystem.exception.NotFoundException;
import com.example.taskmanagementsystem.task.model.Task;
import com.example.taskmanagementsystem.task.model.enums.Priority;
import com.example.taskmanagementsystem.task.model.enums.Status;
import com.example.taskmanagementsystem.task.repository.TaskRepository;
import com.example.taskmanagementsystem.user.model.User;
import com.example.taskmanagementsystem.user.model.enums.Roles;
import com.example.taskmanagementsystem.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class CommentServiceTest {

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TaskRepository taskRepository;

    @Autowired
    CommentService commentService;

    Task task;

    User author;

    User creator;

    Comment comment;

    OutCommentDto outCommentDto;

    NewCommentDto newCommentDto;

    OutCommentDto newComment;

    @BeforeEach
    void setUp() {
        creator = User.builder()
                .id(1L)
                .name("Bob")
                .email("bob@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
        author = User.builder()
                .id(2L)
                .name("Bobb")
                .email("bobb@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
        task = Task.builder()
                .id(1L)
                .creator(creator)
                .title("Title Title")
                .description("Description Description Description Description")
                .status(Status.IN_WAITING)
                .priority(Priority.HIGH)
                .executor(null)
                .build();
        comment = Comment.builder()
                .task(task)
                .user(author)
                .text("Comment text")
                .created(LocalDateTime.of(2020, 10, 11, 10, 20))
                .build();
        outCommentDto = OutCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
        newCommentDto = NewCommentDto.builder()
                .text("Comment text")
                .build();
        newComment = OutCommentDto.builder()
                .id(comment.getId())
                .text("adasfag")
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
    }

    @Test
    void addNewCommentToTask() {
        when(userRepository.findUserById(2L)).thenReturn(author);
        when(taskRepository.findById(1L)).thenReturn(task);
        when(commentRepository.save(any())).thenReturn(comment);

        OutCommentDto actualComment = commentService.addNewComment(1L, 2L, newCommentDto);

        Assertions.assertEquals(comment.getId(), actualComment.getId());
        Assertions.assertEquals(comment.getText(), actualComment.getText());
        Assertions.assertEquals(comment.getUser().getName(), actualComment.getAuthorName());
        Assertions.assertEquals(comment.getCreated(), actualComment.getCreated());
    }

    @Test
    void addValidCommentWithUserNotFound() {
        author = User.builder()
                .id(10L)
                .name("Bobb")
                .email("bobb@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                commentService.addNewComment(1L, author.getId(), newCommentDto));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void addValidCommentWithTaskNotFound() {
        when(userRepository.findUserById(2L)).thenReturn(author);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                commentService.addNewComment(10L, author.getId(), newCommentDto));
        Assertions.assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void updateCommentWithCommentNotFound() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                commentService.updateComment(10L, author.getId(), newCommentDto));
        Assertions.assertEquals("Комментарий не найден", exception.getMessage());
    }

    @Test
    void updateCommentWithUserNotAuthor() {
        when(commentRepository.findById(anyLong())).thenReturn(comment);

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () ->
                commentService.updateComment(1L, 10L, newCommentDto));
        Assertions.assertEquals("Пользователь с id = " + 10L + " не является автором комментария", exception.getMessage());
    }

    @Test
    void updateCommentByAuthor() {
        when(commentRepository.findById(anyLong())).thenReturn(comment);

        OutCommentDto actualComment = commentService.updateComment(1L, 2L, new NewCommentDto("adasfag"));
        Assertions.assertEquals(actualComment.getText(), newComment.getText());
    }

    @Test
    void deleteCommentByIdTest() {
        when(commentRepository.findById(1L)).thenReturn(comment);

        doNothing().when(commentRepository).deleteById(1L);
        commentService.deleteComment(1L, 2L);

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNotFoundCommentTest() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                commentService.updateComment(10L, author.getId(), newCommentDto));
        Assertions.assertEquals("Комментарий не найден", exception.getMessage());
    }
}

package com.example.taskmanagementsystem.comment;

import com.example.taskmanagementsystem.comment.dto.NewCommentDto;
import com.example.taskmanagementsystem.comment.dto.OutCommentDto;
import com.example.taskmanagementsystem.comment.service.CommentService;
import com.example.taskmanagementsystem.task.model.Task;
import com.example.taskmanagementsystem.task.model.enums.Priority;
import com.example.taskmanagementsystem.task.model.enums.Status;
import com.example.taskmanagementsystem.user.model.User;
import com.example.taskmanagementsystem.user.model.enums.Roles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    CommentService commentService;

    OutCommentDto outCommentDto;

    NewCommentDto newCommentDto;

    User creator;

    User author;

    Task task;

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
        outCommentDto = OutCommentDto.builder()
                .id(1L)
                .text("Text Text Text Text")
                .authorName(author.getName())
                .created(LocalDateTime.of(2020, 10, 11, 10, 20))
                .build();
        newCommentDto = NewCommentDto.builder()
                .text("Comment text")
                .build();
    }

    @Test
    void addComment() throws Exception {
        when(commentService.addNewComment(anyLong(), anyLong(), any())).thenReturn(outCommentDto);

        mockMvc.perform(post("/tasks/comments/1/2")
                        .with(user("bob@mail.com").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outCommentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(outCommentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(outCommentDto.getAuthorName())));
    }

    @Test
    void updateComment() throws Exception {
        OutCommentDto updatedComment = OutCommentDto.builder()
                .id(1L)
                .text("Text Text Text Text update")
                .authorName(author.getName())
                .created(LocalDateTime.of(2020, 10, 11, 10, 20))
                .build();
        NewCommentDto newComment = NewCommentDto.builder()
                .text("Text Text Text Text update")
                .build();
        when(commentService.updateComment(anyLong(), anyLong(), any())).thenReturn(updatedComment);

        mockMvc.perform(patch("/tasks/comments/1/2")
                        .with(user("bob@mail.com").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newComment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(updatedComment.getText())));
    }

    @Test
    void deleteTaskByIdTest() throws Exception {
        mockMvc.perform(delete("/tasks/comments/1/2")
                        .with(user("bob@mail.com").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(commentService, times(1)).deleteComment(anyLong(), anyLong());
    }

}
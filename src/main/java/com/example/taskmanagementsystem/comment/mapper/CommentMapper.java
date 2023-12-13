package com.example.taskmanagementsystem.comment.mapper;

import com.example.taskmanagementsystem.comment.dto.NewCommentDto;
import com.example.taskmanagementsystem.comment.dto.OutCommentDto;
import com.example.taskmanagementsystem.comment.model.Comment;
import com.example.taskmanagementsystem.task.model.Task;
import com.example.taskmanagementsystem.user.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(NewCommentDto newCommentDto, User author, Task task) {
        return Comment.builder()
                .task(task)
                .user(author)
                .text(newCommentDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public OutCommentDto toOutCommentDto(Comment comment) {
        return OutCommentDto.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
    }
}

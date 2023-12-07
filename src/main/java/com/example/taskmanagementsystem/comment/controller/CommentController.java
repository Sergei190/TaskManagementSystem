package com.example.taskmanagementsystem.comment.controller;

import com.example.taskmanagementsystem.comment.dto.NewCommentDto;
import com.example.taskmanagementsystem.comment.dto.OutCommentDto;
import com.example.taskmanagementsystem.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/tasks/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{taskId}/{userId}")
    public OutCommentDto addComment(@PathVariable long taskId,
                                    @PathVariable long userId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Запрос на добавление комментария");
        return commentService.addNewComment(taskId, userId, newCommentDto);
    }

    @PatchMapping("/{comId}/{userId}")
    public OutCommentDto updateComment(@PathVariable long comId,
                                       @PathVariable long userId,
                                       @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Запрос на изменение комментария");
        return commentService.updateComment(comId, userId, newCommentDto);
    }

    @DeleteMapping("/{comId}/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long comId, @PathVariable long userId) {
        log.info("Запрос от пользователя на удаление своего комментария");
        commentService.deleteComment(comId, userId);
    }
}

package com.example.taskmanagementsystem.comment.service;

import com.example.taskmanagementsystem.comment.dto.NewCommentDto;
import com.example.taskmanagementsystem.comment.dto.OutCommentDto;

public interface CommentService {

    OutCommentDto addNewComment(long taskId, long userId, NewCommentDto newCommentDto);

    OutCommentDto updateComment(long comId, long userId, NewCommentDto newCommentDto);

    void deleteComment(long comId, long userId);

}
package com.example.taskmanagementsystem.comment.service.impl;

import com.example.taskmanagementsystem.comment.dto.NewCommentDto;
import com.example.taskmanagementsystem.comment.dto.OutCommentDto;
import com.example.taskmanagementsystem.comment.mapper.CommentMapper;
import com.example.taskmanagementsystem.comment.model.Comment;
import com.example.taskmanagementsystem.comment.repository.CommentRepository;
import com.example.taskmanagementsystem.comment.service.CommentService;
import com.example.taskmanagementsystem.exception.ConflictException;
import com.example.taskmanagementsystem.exception.NotFoundException;
import com.example.taskmanagementsystem.task.model.Task;
import com.example.taskmanagementsystem.task.repository.TaskRepository;
import com.example.taskmanagementsystem.user.model.User;
import com.example.taskmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository,
                              TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public OutCommentDto addNewComment(long taskId, long userId, NewCommentDto newCommentDto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new NotFoundException("Задача не найдена");
        }
        Comment comment = CommentMapper.toComment(newCommentDto, user, task);
        Comment storageComment = commentRepository.save(comment);
        return CommentMapper.toOutCommentDto(storageComment);
    }

    @Override
    public OutCommentDto updateComment(long comId, long userId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findById(comId);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден");
        }
        if(comment.getUser().getId() != userId) {
            throw new ConflictException("Пользователь с id = " + userId + " не является автором комментария");
        }
        comment.setText(newCommentDto.getText());
        return CommentMapper.toOutCommentDto(comment);
    }

    @Override
    public void deleteComment(long comId, long userId) {
        Comment comment = commentRepository.findById(comId);
        if (comment == null) {
            throw new NotFoundException("Комментарий не найден");
        }
        if(comment.getUser().getId() != userId) {
            throw new ConflictException("Пользователь с id = " + userId + " не является автором комментария");
        }
        commentRepository.deleteById(comId);
    }
}

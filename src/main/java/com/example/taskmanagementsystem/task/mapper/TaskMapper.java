package com.example.taskmanagementsystem.task.mapper;


import com.example.taskmanagementsystem.comment.dto.OutCommentDto;
import com.example.taskmanagementsystem.comment.model.Comment;
import com.example.taskmanagementsystem.task.dto.FullTaskDto;
import com.example.taskmanagementsystem.task.dto.NewTaskDto;
import com.example.taskmanagementsystem.task.dto.TaskDto;
import com.example.taskmanagementsystem.task.model.Task;
import com.example.taskmanagementsystem.task.model.enums.Priority;
import com.example.taskmanagementsystem.task.model.enums.Status;
import com.example.taskmanagementsystem.user.dto.ShortUserDto;
import com.example.taskmanagementsystem.user.mapper.UserMapper;
import com.example.taskmanagementsystem.user.model.User;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TaskMapper {

    public Task toTask(NewTaskDto newTaskDto, User creator, Status status, Priority priority, User executor) {
        return Task.builder()
                .creator(creator)
                .title(newTaskDto.getTitle())
                .description(newTaskDto.getDescription())
                .status(status)
                .priority(priority)
                .executor(executor)
                .build();
    }

    public TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .creator(UserMapper.toShortUserDto(task.getCreator()))
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .executor(task.getExecutor() != null ?
                        UserMapper.toShortUserDto(task.getExecutor()) : null)
                .build();
    }

    public FullTaskDto toFullTaskDto(Task task, List<OutCommentDto> comments) {
        return FullTaskDto.builder()
                .id(task.getId())
                .creator(UserMapper.toShortUserDto(task.getCreator()))
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .executor(task.getExecutor() != null ?
                        UserMapper.toShortUserDto(task.getExecutor()) : null)
                .comments(comments)
                .build();
    }

}
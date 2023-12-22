package com.example.taskmanagementsystem.task.service;

import com.example.taskmanagementsystem.task.dto.*;
import com.example.taskmanagementsystem.task.model.enums.Priority;
import com.example.taskmanagementsystem.task.model.enums.Sort;
import com.example.taskmanagementsystem.task.model.enums.Status;

import java.util.List;

public interface TaskService {

    TaskDto createTask(NewTaskDto newTaskDto, Priority priority);

    TaskDto updateTaskByCreator(long creatorId, long taskId, UpdateTaskDto updateTaskDto);

    TaskDto updateTaskStatusByExecutor(long executorId, long taskId, Status status);

    FullTaskDto getTaskById(long id);

    void deleteTaskById(long taskId, long creatorId);

    List<FullTaskDto> getTasks(int from, int size);

    List<FullTaskDto> getTasksByCreatorId(long creatorId, Sort sort, int from, int size);

    List<FullTaskDto> getTasksByExecutorId(long executorId, Sort sort, int from, int size);

}
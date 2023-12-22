package com.example.taskmanagementsystem.task.controller;

import com.example.taskmanagementsystem.exception.BadRequestException;
import com.example.taskmanagementsystem.task.dto.*;
import com.example.taskmanagementsystem.task.model.enums.Priority;
import com.example.taskmanagementsystem.task.model.enums.Sort;
import com.example.taskmanagementsystem.task.model.enums.Status;
import com.example.taskmanagementsystem.task.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public TaskDto createTask(@Valid @RequestBody NewTaskDto newTaskDto) {
        Priority priority = Priority.from(newTaskDto.getPriority())
                .orElseThrow(() -> new BadRequestException("Неизвестный приоритет задачи: " + newTaskDto.getPriority()));
        log.info("Запрос на добавление новой задачи");
        return taskService.createTask(newTaskDto, priority);
    }

    @PatchMapping("{creatorId}/update/{taskId}")
    public TaskDto updateTaskByCreator(@PathVariable long creatorId, @PathVariable long taskId,
                              @Valid @RequestBody UpdateTaskDto updateTaskDto) {
        log.info("Запрос на обновление задачи с id = " + taskId);
        return taskService.updateTaskByCreator(creatorId, taskId, updateTaskDto);
    }

    @PatchMapping("/update/{executorId}/{taskId}")
    public TaskDto updateTaskStatusByExecutor(@PathVariable long executorId, @PathVariable long taskId,
                                              @RequestBody UpdateTaskStatusDto updateTaskStatusDto) {
        log.info("Запрос на обновление статуса задачи с id = " + taskId + " от исполнителя с id = " + executorId);
        Status status = Status.from(updateTaskStatusDto.getStatus())
                .orElseThrow(() -> new BadRequestException("Неизвестный статус задачи: " + updateTaskStatusDto.getStatus()));
        return taskService.updateTaskStatusByExecutor(executorId, taskId, status);
    }

    @GetMapping("/{taskId}")
    public FullTaskDto getTaskById(@PathVariable long taskId) {
        log.info("Запрос получение задачи с id = " + taskId);
        return taskService.getTaskById(taskId);
    }

    @DeleteMapping("/{taskId}/{creatorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskByCreator(@PathVariable long taskId, @PathVariable long creatorId) {
        log.info("Запрос на удаление задачи с id = " + taskId);
        taskService.deleteTaskById(taskId, creatorId);
    }

    @GetMapping
    public List<FullTaskDto> getTasks(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на вывод задач");
        return taskService.getTasks(from, size);
    }

    @GetMapping("/creator/{creatorId}")
    public List<FullTaskDto> getCreatorTasks(@PathVariable long creatorId,
                                             @RequestParam(defaultValue = "STATUS") String sort,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на вывод задач");
        Sort enumSort = Sort.from(sort)
                .orElseThrow(() -> new BadRequestException("Неподдерживаемый вариант сортировки: " + sort));
        return taskService.getTasksByCreatorId(creatorId, enumSort, from, size);
    }

    @GetMapping("/executor/{executorId}")
    public List<FullTaskDto> getExecutorTasks(@PathVariable long executorId,
                                             @RequestParam(defaultValue = "STATUS") String sort,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на вывод задач");
        Sort enumSort = Sort.from(sort)
                .orElseThrow(() -> new BadRequestException("Неподдерживаемый вариант сортировки: " + sort));
        return taskService.getTasksByExecutorId(executorId, enumSort, from, size);
    }

}
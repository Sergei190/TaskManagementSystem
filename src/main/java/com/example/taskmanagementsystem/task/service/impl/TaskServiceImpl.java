package com.example.taskmanagementsystem.task.service.impl;

import com.example.taskmanagementsystem.comment.dto.OutCommentDto;
import com.example.taskmanagementsystem.comment.mapper.CommentMapper;
import com.example.taskmanagementsystem.comment.model.Comment;
import com.example.taskmanagementsystem.comment.repository.CommentRepository;
import com.example.taskmanagementsystem.exception.BadRequestException;
import com.example.taskmanagementsystem.exception.ConflictException;
import com.example.taskmanagementsystem.exception.NotFoundException;
import com.example.taskmanagementsystem.task.dto.*;
import com.example.taskmanagementsystem.task.mapper.TaskMapper;
import com.example.taskmanagementsystem.task.model.Task;
import com.example.taskmanagementsystem.task.model.enums.Priority;
import com.example.taskmanagementsystem.task.model.enums.Sort;
import com.example.taskmanagementsystem.task.model.enums.Status;
import com.example.taskmanagementsystem.task.repository.TaskRepository;
import com.example.taskmanagementsystem.task.service.TaskService;
import com.example.taskmanagementsystem.user.dto.ShortUserDto;
import com.example.taskmanagementsystem.user.mapper.UserMapper;
import com.example.taskmanagementsystem.user.model.User;
import com.example.taskmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository,
                           CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public TaskDto createTask(NewTaskDto newTaskDto, Priority priority) {
        User creator = userRepository.findUserById(newTaskDto.getCreator_id());
        if(creator == null) {
            throw new NotFoundException("Пользватель с id = " + newTaskDto.getCreator_id() + " не найден.");
        }
        User executor;
        if (newTaskDto.getExecutor_id() != null) {
            executor = userRepository.findUserById(newTaskDto.getExecutor_id());
            if (executor == null) {
                throw new NotFoundException("Пользватель с id = " + newTaskDto.getExecutor_id() + " не найден.");
            }
        } else {
            executor = null;
        }
        Task task = TaskMapper.toTask(newTaskDto, creator, Status.IN_WAITING, priority, executor);
        Task taskStorage = taskRepository.save(task);
        return TaskMapper.toTaskDto(taskStorage);
    }

    @Override
    public TaskDto updateTaskByCreator(long creatorId, long taskId, UpdateTaskDto updateTaskDto) {
        checkUser(creatorId);
        Task task = checkTask(taskId);
        if (task.getCreator().getId() != creatorId) {
            throw new ConflictException("Пользователь не является создателем задачи");
        }
        if (updateTaskDto.getTitle() != null) {
            task.setTitle(updateTaskDto.getTitle());
        }
        if (updateTaskDto.getDescription() != null) {
            task.setDescription(updateTaskDto.getDescription());
        }
        if (updateTaskDto.getStatus() != null) {
            Status status = Status.from(updateTaskDto.getStatus())
                    .orElseThrow(() -> new BadRequestException("Неизвестный статус задачи: " + updateTaskDto.getStatus()));
            task.setStatus(status);
        }
        if (updateTaskDto.getPriority() != null) {
            Priority priority = Priority.from(updateTaskDto.getPriority())
                    .orElseThrow(() -> new BadRequestException("Неизвестный приоритет задачи: " + updateTaskDto.getPriority()));
            task.setPriority(priority);
        }
        if (updateTaskDto.getExecutor_id() != null) {
            User executor = userRepository.findUserById(updateTaskDto.getExecutor_id());
            if (executor == null) {
                throw new NotFoundException("Пользователь с id = " + updateTaskDto.getExecutor_id() + " не найден");
            }
            task.setExecutor(executor);
        }
        Task updatedTask = taskRepository.save(task);
        return TaskMapper.toTaskDto(updatedTask);
    }

    @Override
    public TaskDto updateTaskStatusByExecutor(long executorId, long taskId, Status status) {
        checkUser(executorId);
        Task task = checkTask(taskId);
        if (executorId != task.getExecutor().getId()) {
            throw new ConflictException("Пользователь не является исполнителем задачи");
        }
        task.setStatus(status);
        Task updateTaskStatus = taskRepository.save(task);
        return TaskMapper.toTaskDto(updateTaskStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public FullTaskDto getTaskById(long id) {
        Task task = checkTask(id);
        List<Comment> taskComments = commentRepository.findCommentsByTaskId(id);
        List<OutCommentDto> commentDtos = taskComments.stream()
                .map(CommentMapper::toOutCommentDto)
                .toList();
        return TaskMapper.toFullTaskDto(task, commentDtos);
    }

    @Override
    public void deleteTaskById(long taskId, long creatorId) {
        Task task = checkTask(taskId);
        checkUser(creatorId);
        if (task.getCreator().getId() != creatorId) {
            throw new ConflictException("Пользователь не является создателем задачи");
        }
        taskRepository.deleteById(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullTaskDto> getTasks(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<Task> listTasks = tasks.getContent();
        return getFullTaskDtos(listTasks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullTaskDto> getTasksByCreatorId(long creatorId, Sort sort, int from, int size) {
        checkUser(creatorId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Task> tasks = switch (sort) {
            case PRIORITY -> taskRepository.findTasksByCreatorIdOrderByPriorityDesc(creatorId, pageable);
            case STATUS -> taskRepository.findTasksByCreatorIdOrderByStatusDesc(creatorId, pageable);
        };
        return getFullTaskDtos(tasks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullTaskDto> getTasksByExecutorId(long executorId, Sort sort, int from, int size) {
        checkUser(executorId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Task> tasks = switch (sort) {
            case PRIORITY -> taskRepository.findTasksByExecutorIdOrderByPriorityDesc(executorId, pageable);
            case STATUS -> taskRepository.findTasksByExecutorIdOrderByStatusDesc(executorId, pageable);
        };
        return getFullTaskDtos(tasks);
    }

    private List<FullTaskDto> getFullTaskDtos(List<Task> tasks) {
        List<Long> taskIds = tasks.stream()
                .map(Task::getId)
                .toList();
        Map<Long, List<OutCommentDto>> taskCommentsMap = commentRepository.findCommentsByTaskIdIn(taskIds).stream()
                .map(CommentMapper::toOutCommentDto)
                .collect(Collectors.groupingBy(OutCommentDto::getTaskId));

        return tasks.stream()
                .map(task -> TaskMapper.toFullTaskDto(task, taskCommentsMap.getOrDefault(task.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private void checkUser(long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    private Task checkTask(long taskId) {
        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new NotFoundException("Задача с id = " + taskId + " не найдена");
        }
        return task;
    }
}

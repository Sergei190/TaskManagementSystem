package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.comment.repository.CommentRepository;
import com.example.taskmanagementsystem.exception.ConflictException;
import com.example.taskmanagementsystem.exception.NotFoundException;
import com.example.taskmanagementsystem.task.dto.FullTaskDto;
import com.example.taskmanagementsystem.task.dto.NewTaskDto;
import com.example.taskmanagementsystem.task.dto.TaskDto;
import com.example.taskmanagementsystem.task.dto.UpdateTaskDto;
import com.example.taskmanagementsystem.task.model.Task;
import com.example.taskmanagementsystem.task.model.enums.Priority;
import com.example.taskmanagementsystem.task.model.enums.Sort;
import com.example.taskmanagementsystem.task.model.enums.Status;
import com.example.taskmanagementsystem.task.repository.TaskRepository;
import com.example.taskmanagementsystem.task.service.TaskService;
import com.example.taskmanagementsystem.user.mapper.UserMapper;
import com.example.taskmanagementsystem.user.model.User;
import com.example.taskmanagementsystem.user.model.enums.Roles;
import com.example.taskmanagementsystem.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {

    @MockBean
    TaskRepository taskRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CommentRepository commentRepository;

    @Autowired
    TaskService taskService;

    TaskDto taskDto;

    NewTaskDto newTaskDto;

    Task task;

    User creator;

    User executor;

    @BeforeEach
    void setUp() {
        creator = User.builder()
                .id(1L)
                .name("Bob")
                .email("bob@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
        executor = User.builder()
                .id(2L)
                .name("Jon")
                .email("jon@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
        newTaskDto = NewTaskDto.builder()
                .creator_id(1L)
                .title("Title")
                .description("Description Description Description Description")
                .status("IN_WAITING")
                .priority("HIGH")
                .executor_id(2L)
                .build();
        task = Task.builder()
                .id(1L)
                .creator(creator)
                .title(newTaskDto.getTitle())
                .description(newTaskDto.getDescription())
                .status(Status.IN_WAITING)
                .priority(Priority.HIGH)
                .executor(executor)
                .build();
        taskDto = TaskDto.builder()
                .id(1L)
                .creator(UserMapper.toShortUserDto(creator))
                .title("Title")
                .description("Description Description Description Description")
                .status(Status.IN_WAITING)
                .priority(Priority.HIGH)
                .executor(UserMapper.toShortUserDto(executor))
                .build();
    }

    @Test
    void addValidTaskWithExecutor() {
        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findUserById(2L)).thenReturn(executor);
        when(taskRepository.save(any())).thenReturn(task);

        TaskDto actualTask = taskService.createTask(newTaskDto, Priority.HIGH);

        Assertions.assertEquals(actualTask.getTitle(), taskDto.getTitle());
        Assertions.assertEquals(actualTask.getDescription(), taskDto.getDescription());
        Assertions.assertEquals(actualTask.getCreator().getId(), taskDto.getCreator().getId());
        Assertions.assertEquals(actualTask.getExecutor().getId(), taskDto.getExecutor().getId());
        Assertions.assertEquals(actualTask.getStatus(), taskDto.getStatus());
        Assertions.assertEquals(actualTask.getPriority(), taskDto.getPriority());
    }

    @Test
    void addValidTaskWithNotFoundCreator() {
        when(taskRepository.save(any())).thenReturn(task);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                taskService.createTask(newTaskDto, Priority.HIGH));
        Assertions.assertEquals("Пользватель с id = " + newTaskDto.getCreator_id() + " не найден.", exception.getMessage());
    }

    @Test
    void addValidTaskWithNotFoundExecutor() {
        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(taskRepository.save(any())).thenReturn(task);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                taskService.createTask(newTaskDto, Priority.HIGH));
        Assertions.assertEquals("Пользватель с id = " + newTaskDto.getExecutor_id() + " не найден.", exception.getMessage());
    }

    @Test
    void addValidTaskWithoutExecutor() {
        newTaskDto.setExecutor_id(null);
        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findUserById(2L)).thenReturn(executor);
        when(taskRepository.save(any())).thenReturn(task);

        TaskDto actualTask = taskService.createTask(newTaskDto, Priority.HIGH);

        Assertions.assertEquals(actualTask.getTitle(), taskDto.getTitle());
        Assertions.assertEquals(actualTask.getDescription(), taskDto.getDescription());
        Assertions.assertEquals(actualTask.getCreator().getId(), taskDto.getCreator().getId());
        Assertions.assertEquals(actualTask.getExecutor().getId(), taskDto.getExecutor().getId());
        Assertions.assertEquals(actualTask.getStatus(), taskDto.getStatus());
        Assertions.assertEquals(actualTask.getPriority(), taskDto.getPriority());
    }

    @Test
    void updateTaskByCreatorTest() {
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .title("Title update")
                .description("Description Description Description Description update")
                .status("IN_PROGRESS")
                .priority("LOW")
                .build();
        Task updateTask = Task.builder()
                .id(1L)
                .creator(creator)
                .title("Title update")
                .description("Description Description Description Description update")
                .status(Status.IN_PROGRESS)
                .priority(Priority.LOW)
                .executor(executor)
                .build();
        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(taskRepository.findById(1L)).thenReturn(task);
        when(taskRepository.save(any())).thenReturn(updateTask);

        TaskDto updatedTask = taskService.updateTaskByCreator(1L, task.getId(), updateTaskDto);

        Assertions.assertEquals(updatedTask.getId(), task.getId());
        Assertions.assertEquals(updatedTask.getCreator().getId(), task.getCreator().getId());
        Assertions.assertEquals(updatedTask.getTitle(), task.getTitle());
        Assertions.assertEquals(updatedTask.getDescription(), task.getDescription());
        Assertions.assertEquals(updatedTask.getStatus(), task.getStatus());
        Assertions.assertEquals(updatedTask.getExecutor().getId(), task.getExecutor().getId());
        Assertions.assertEquals(updatedTask.getPriority(), task.getPriority());
    }

    @Test
    void updateTaskWithNotFoundCreator() {
        long invalidId = 10L;
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .title("Title update")
                .description("Description Description Description Description update")
                .status("IN_PROGRESS")
                .priority("LOW")
                .build();
        Task updateTask = Task.builder()
                .id(1L)
                .creator(creator)
                .title("Title update")
                .description("Description Description Description Description update")
                .status(Status.IN_PROGRESS)
                .priority(Priority.LOW)
                .executor(executor)
                .build();

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                taskService.updateTaskByCreator(invalidId, updateTask.getId(), updateTaskDto));
        Assertions.assertEquals("Пользователь с id = " + invalidId + " не найден", exception.getMessage());
    }

    @Test
    void updateTaskWithNotFoundTask() {
        long invalidId = 10L;
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .title("Title update")
                .description("Description Description Description Description update")
                .status("IN_PROGRESS")
                .priority("LOW")
                .build();

        when(userRepository.findUserById(1L)).thenReturn(creator);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                taskService.updateTaskByCreator(1L, invalidId, updateTaskDto));
        Assertions.assertEquals("Задача с id = " + invalidId + " не найдена", exception.getMessage());
    }

    @Test
    void updateTaskWhenUserIsNotCreator() {
        User user = User.builder()
                .id(10L)
                .name("Bob")
                .email("bob@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .title("Title update")
                .description("Description Description Description Description update")
                .status("IN_PROGRESS")
                .priority("LOW")
                .build();

        when(userRepository.findUserById(10L)).thenReturn(user);
        when(taskRepository.findById(1L)).thenReturn(task);

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () ->
                taskService.updateTaskByCreator(10L, 1L, updateTaskDto));
        Assertions.assertEquals("Пользователь не является создателем задачи", exception.getMessage());
    }

    @Test
    void updateTaskByCreatorWithNewExecutorTest() {
        task.setExecutor(null);
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .title("Title update")
                .description("Description Description Description Description update")
                .status("IN_PROGRESS")
                .priority("LOW")
                .executor_id(2L)
                .build();
        Task updateTask = Task.builder()
                .id(1L)
                .creator(creator)
                .title("Title update")
                .description("Description Description Description Description update")
                .status(Status.IN_PROGRESS)
                .priority(Priority.LOW)
                .executor(executor)
                .build();
        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(taskRepository.findById(1L)).thenReturn(task);
        when(taskRepository.save(any())).thenReturn(updateTask);
        when(userRepository.findUserById(2L)).thenReturn(executor);

        TaskDto updatedTask = taskService.updateTaskByCreator(1L, task.getId(), updateTaskDto);

        Assertions.assertEquals(updatedTask.getId(), task.getId());
        Assertions.assertEquals(updatedTask.getCreator().getId(), task.getCreator().getId());
        Assertions.assertEquals(updatedTask.getTitle(), task.getTitle());
        Assertions.assertEquals(updatedTask.getDescription(), task.getDescription());
        Assertions.assertEquals(updatedTask.getStatus(), task.getStatus());
        Assertions.assertEquals(updatedTask.getExecutor().getId(), task.getExecutor().getId());
        Assertions.assertEquals(updatedTask.getPriority(), task.getPriority());
    }

    @Test
    void updateTaskByCreatorWithNewExecutorIsNotFoundTest() {
        User user = User.builder()
                .id(10L)
                .name("Bob")
                .email("bob@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .title("Title update")
                .description("Description Description Description Description update")
                .status("IN_PROGRESS")
                .priority("LOW")
                .executor_id(2L)
                .build();

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(taskRepository.findById(1L)).thenReturn(task);
        when(userRepository.findUserById(10L)).thenReturn(user);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                taskService.updateTaskByCreator(1L, 1L, updateTaskDto));
        Assertions.assertEquals("Пользователь с id = " + updateTaskDto.getExecutor_id() + " не найден",
                exception.getMessage());

    }

    @Test
    void updateTaskStatusByExecutorTest() {
        Task updateTask = Task.builder()
                .id(1L)
                .creator(creator)
                .title("Title update")
                .description("Description Description Description Description update")
                .status(Status.COMPLETED)
                .priority(Priority.LOW)
                .executor(executor)
                .build();
        when(userRepository.findUserById(2L)).thenReturn(executor);
        when(taskRepository.findById(1L)).thenReturn(task);
        when(taskRepository.save(any())).thenReturn(updateTask);

        TaskDto updatedStatusTask = taskService.updateTaskStatusByExecutor(2L, 1L, Status.COMPLETED);

        Assertions.assertEquals(updatedStatusTask.getStatus(), updateTask.getStatus());
    }

    @Test
    void updateTaskStatusByUserNotExecutorTest() {
        User user = User.builder()
                .id(10L)
                .name("Bob")
                .email("bob@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();

        when(userRepository.findUserById(10L)).thenReturn(user);
        when(taskRepository.findById(1L)).thenReturn(task);

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () ->
                taskService.updateTaskStatusByExecutor(10L, 1L, Status.COMPLETED));
        Assertions.assertEquals("Пользователь не является исполнителем задачи", exception.getMessage());
    }

    @Test
    void getTaskByIdTest() {
        FullTaskDto fullTaskDto = FullTaskDto.builder()
                .id(1L)
                .creator(UserMapper.toShortUserDto(creator))
                .title("Title")
                .description("Description Description Description Description")
                .status(Status.IN_WAITING)
                .priority(Priority.HIGH)
                .executor(UserMapper.toShortUserDto(executor))
                .comments(new ArrayList<>())
                .build();
        when(taskRepository.findById(1L)).thenReturn(task);
        when(commentRepository.findCommentsByTaskId(1L)).thenReturn(new ArrayList<>());

        FullTaskDto actualTask = taskService.getTaskById(1L);

        Assertions.assertEquals(actualTask.getId(), fullTaskDto.getId());
        Assertions.assertEquals(actualTask.getCreator().getId(), fullTaskDto.getCreator().getId());
        Assertions.assertEquals(actualTask.getTitle(), fullTaskDto.getTitle());
        Assertions.assertEquals(actualTask.getDescription(), fullTaskDto.getDescription());
        Assertions.assertEquals(actualTask.getStatus(), fullTaskDto.getStatus());
        Assertions.assertEquals(actualTask.getExecutor().getId(), fullTaskDto.getExecutor().getId());
        Assertions.assertEquals(actualTask.getPriority(), fullTaskDto.getPriority());
        Assertions.assertEquals(actualTask.getComments().size(), fullTaskDto.getComments().size());
    }

    @Test
    void deleteTaskByIdTest() {
        when(taskRepository.findById(1L)).thenReturn(task);
        when(userRepository.findUserById(1L)).thenReturn(creator);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTaskById(1L, 1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTaskByIdWhenUserIsNotCreatorTest() {
        User user = User.builder()
                .id(10L)
                .name("Bob")
                .email("bob@mail.com")
                .password("111111")
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
        when(taskRepository.findById(1L)).thenReturn(task);
        when(userRepository.findUserById(10L)).thenReturn(user);

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () ->
                taskService.deleteTaskById(1L, 10L));
        Assertions.assertEquals("Пользователь не является создателем задачи", exception.getMessage());
    }

    @Test
    void getTasksTest() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(tasks));
        when(commentRepository.findCommentsByTaskIdIn(List.of(1L))).thenReturn(new ArrayList<>());

        List<FullTaskDto> fullTaskDtos = taskService.getTasks(0, 10);

        Assertions.assertEquals(1, fullTaskDtos.size());
    }

    @Test
    void getTasksByCreatorIdWithSortByPriorityTest() {
        Sort sort = Sort.PRIORITY;
        int from = 0;
        int size = 10;

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(userRepository.findUserById(1L)).thenReturn(executor);
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.findTasksByCreatorIdOrderByPriorityDesc(1L, pageable)).thenReturn(tasks);

        List<FullTaskDto> fullTaskDtos = taskService.getTasksByCreatorId(1L, sort, from, size);

        Assertions.assertEquals(1, fullTaskDtos.size());
    }

    @Test
    void getTasksByCreatorIdWithSortByStatusTest() {
        Sort sort = Sort.STATUS;
        int from = 0;
        int size = 10;

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(userRepository.findUserById(1L)).thenReturn(executor);
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.findTasksByCreatorIdOrderByStatusDesc(1L, pageable)).thenReturn(tasks);

        List<FullTaskDto> fullTaskDtos = taskService.getTasksByCreatorId(1L, sort, from, size);

        Assertions.assertEquals(1, fullTaskDtos.size());
    }

    @Test
    void getTasksByExecutorIdWithSortByPriorityTest() {
        Sort sort = Sort.PRIORITY;
        int from = 0;
        int size = 10;

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(userRepository.findUserById(1L)).thenReturn(executor);
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.findTasksByExecutorIdOrderByPriorityDesc(1L, pageable)).thenReturn(tasks);

        List<FullTaskDto> fullTaskDtos = taskService.getTasksByExecutorId(1L, sort, from, size);

        Assertions.assertEquals(1, fullTaskDtos.size());
    }

    @Test
    void getTasksByExecutorIdWithSortByStatusTest() {
        Sort sort = Sort.STATUS;
        int from = 0;
        int size = 10;

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(userRepository.findUserById(1L)).thenReturn(executor);
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.findTasksByExecutorIdOrderByStatusDesc(1L, pageable)).thenReturn(tasks);

        List<FullTaskDto> fullTaskDtos = taskService.getTasksByExecutorId(1L, sort, from, size);

        Assertions.assertEquals(1, fullTaskDtos.size());
    }
}

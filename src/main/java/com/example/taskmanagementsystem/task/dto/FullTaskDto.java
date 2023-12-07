package com.example.taskmanagementsystem.task.dto;

import com.example.taskmanagementsystem.comment.dto.OutCommentDto;
import com.example.taskmanagementsystem.task.model.enums.Priority;
import com.example.taskmanagementsystem.task.model.enums.Status;
import com.example.taskmanagementsystem.user.dto.ShortUserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullTaskDto {

    private long id;
    private ShortUserDto creator;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private ShortUserDto executor;
    private List<OutCommentDto> comments;

}

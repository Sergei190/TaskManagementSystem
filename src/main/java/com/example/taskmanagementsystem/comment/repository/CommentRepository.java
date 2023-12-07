package com.example.taskmanagementsystem.comment.repository;

import com.example.taskmanagementsystem.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findById(long comId);

    List<Comment> findCommentsByTaskId(long taskId);

    List<Comment> findCommentsByTaskIdIn(Collection<Long> id);
}

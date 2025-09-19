package com.example.forum.mapper;

import com.example.forum.repository.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectAllOrderByIdDesc();

    Comment selectCommentById(@Param("id") int id);

    void insertComment(Comment comment);

    void deleteComment(@Param("id") int id);

    void updateComment(Comment comment);
}

package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.mapper.CommentMapper;
import com.example.forum.repository.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    /*
     * 返信投稿レコード全件取得処理
     */
    public List<CommentForm> findAllComment() {
        List<Comment> results = commentMapper.selectAllOrderByIdDesc();
        List<CommentForm> reports = setCommentForm(results);
        return reports;
    }
    /*
     * DBから取得した返信データをFormに設定
     */
    private List<CommentForm> setCommentForm(List<Comment> results) {
        List<CommentForm> comments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            CommentForm comment = new CommentForm();
            Comment result = results.get(i);
            comment.setId(result.getId());
            comment.setReportId(result.getReportId());
            comment.setText(result.getText());
            comments.add(comment);
        }
        return comments;
    }
    /*
     * commentテーブルのレコード追加
     */
    public void insertComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentMapper.insertComment(saveComment);
    }
    /*
     * commentテーブルのレコード更新
     */
    public void updateComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentMapper.updateComment(saveComment);
    }
    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Comment setCommentEntity(CommentForm reqComment) {
        Comment comment = new Comment();
        comment.setId(reqComment.getId());
        comment.setReportId(reqComment.getReportId());
        comment.setText(reqComment.getText());
        return comment;
    }
    /*
     * 返信投稿の削除
     */
    public void deleteComment(Integer id) {
        commentMapper.deleteComment(id);
    }
    /*
     *　レコード1件取得
     */
    public CommentForm editComment(Integer id) {
        Comment comment = commentMapper.selectCommentById(id);
        if (comment == null) {
            return null;
        }
        List<Comment> results = new ArrayList<>();
        results.add(comment);
        List<CommentForm> reports = setCommentForm(results);
        return reports.get(0);
    }

}

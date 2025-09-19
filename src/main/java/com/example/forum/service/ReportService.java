package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    CommentRepository commentRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport(Date start, Date end) {
        //型変換
        Timestamp startTimestamp = new Timestamp(start.getTime());
        Timestamp endTimestamp = new Timestamp(end.getTime());
        //List<Report> results = reportRepository.findAllByOrderByIdDesc();
        //List<Report> results = reportRepository.findByCreatedDateBetweenOrderByCreatedDateDesc(startTimestamp, endTimestamp);
        List<Report> results = reportRepository.findByUpdatedDateBetweenOrderByUpdatedDateDesc(startTimestamp, endTimestamp);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }
    /*
     * 返信投稿レコード全件取得処理
     */
    public List<CommentForm> findAllComment() {
        List<Comment> results = commentRepository.findAllByOrderByIdDesc();
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
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        return report;
    }
    /*
     * 投稿の削除
     */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }
    /*
     * レコード1件取得
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add(reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }
    /*
     * commentテーブルのレコード追加
     */
    public void saveComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentRepository.save(saveComment);
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
        commentRepository.deleteById(id);
    }
    /*
     * レコード1件取得
     */
    public CommentForm editComment(Integer id) {
        List<Comment> results = new ArrayList<>();
        results.add(commentRepository.findById(id).orElse(null));
        List<CommentForm> reports = setCommentForm(results);
        return reports.get(0);
    }
    /*
     * UpdatedDate更新
     */
    public void updateUpdatedComment(int id, Timestamp updatedDate) {
        reportRepository.updateUpdatedDate(id, updatedDate); // ✅ OK：インスタンスから呼び出す
    }

}

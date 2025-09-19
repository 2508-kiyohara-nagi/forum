package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.repository.entity.Comment;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @Nullable
            Date start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @Nullable
            Date end
    ) {
        //時間のデータフォーマットがどうなっているか確認
        ModelAndView mav = new ModelAndView();
        if (start == null) {
            // 2020-01-01 00:00:00 に設定
            LocalDateTime startDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
            start = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        //値が入っている場合つなげる処理が必要今のままだと時間がはいっていない00:00:00
        }else{
            // start の時間を 00:00:00 に補正
            LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDateTime startDateTime = startDate.atTime(0, 0, 0);
            start = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            //date型をString型に
            String startString = new SimpleDateFormat("yyyy-MM-dd").format(start);
            mav.addObject("start", startString);

        }
        if (end == null) {
            // 今日の23:59:59に設定
            LocalDate today = LocalDate.now();
            LocalDateTime endDateTime = today.atTime(23, 59, 59);
            end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        //値が入っている場合つなげる処理が必要今のままだと時間がはいっていない23:59:59
        }else{
            LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
            //date型をString型に
            String endString = new SimpleDateFormat("yyyy-MM-dd").format(end);
            mav.addObject("end", endString);

        }

        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport(start, end);
        //返信投稿全権取得
        List<CommentForm> commentTextData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("comments", commentTextData);
        return mav;
    }
    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent () {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;

    }
    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@Valid @ModelAttribute("formModel") ReportForm reportForm, BindingResult bindingResult){
        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            // バリデーションエラーがあれば、新規投稿画面を表示しエラーを渡す
            mav.setViewName("/new");

            // エラーメッセージを1件ずつ取り出してリストに追加する
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            mav.addObject("errorMessages", errorMessages);
            return mav;
        }
        reportService.insertReport(reportForm);

        return new ModelAndView("redirect:/");
    }
    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/report/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {

        reportService.deleteReport(id);

        return new ModelAndView("redirect:/");
    }
    /*
     * 編集画面表示処理
     */
    @GetMapping("/edit/report/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();

        ReportForm report = reportService.editReport(id);
        mav.addObject("formModel", report);

        mav.setViewName("/edit");
        return mav;
    }
    /*
     * 編集処理
     */
    @PutMapping("/update/report/{id}")
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @Valid @ModelAttribute("formModel") ReportForm report,
                                       BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            // バリデーションエラーがあれば、エラーを渡す
            mav.setViewName("/edit");

            // エラーメッセージを1件ずつ取り出してリストに追加する
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            mav.addObject("errorMessages", errorMessages);
            return mav;
        }
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        // 編集した投稿を更新
        reportService.saveReport(report);

        return new ModelAndView("redirect:/");
    }
    /*
     * 新規コメント投稿画面表示処理
     */
    @GetMapping("/comment/{id}")
    public ModelAndView newText(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        Comment comment = new Comment();
        //CommentFormにreportId保存
        comment.setReportId(id);
        // 画面遷移先を指定
        mav.setViewName("/comment");
        // 準備した空のFormを保管
        mav.addObject("commentFormModel", comment);
        return mav;
    }
    /*
     * 返信投稿処理
     */
    @PostMapping("/comment/add/{reportId}")
    public ModelAndView addContent(@PathVariable("reportId") int id,
                                   @Valid @ModelAttribute("commentFormModel") CommentForm commentForm,
                                   BindingResult bindingResult){
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            // バリデーションエラーがあれば、エラーを渡す
            mav.setViewName("/comment");

            // エラーメッセージを1件ずつ取り出してリストに追加する
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            mav.addObject("errorMessages", errorMessages);
            return mav;
        }
        reportService.updateUpdatedComment(id, Timestamp.from(Instant.now()));
        commentService.insertComment(commentForm);

        return new ModelAndView("redirect:/");
    }
    /*
     * 返信投稿削除処理
     */
    @DeleteMapping("/delete/comment/{id}")
    public ModelAndView deleteText(@PathVariable Integer id) {

        commentService.deleteComment(id);

        return new ModelAndView("redirect:/");
    }
    /*
     * 編集画面表示処理
     */
    @GetMapping("/edit/comment/{id}")
    public ModelAndView editText(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();

        CommentForm comment = commentService.editComment(id);
        mav.addObject("formModel", comment);

        mav.setViewName("/editComment");
        return mav;
    }
    /*
     * 返信投稿編集処理
     */
    @PutMapping("/update/comment/{id}")
    public ModelAndView updateText (@PathVariable Integer id,
                                    @Valid @ModelAttribute("formModel") CommentForm comment,
                                    BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            // バリデーションエラーがあれば、エラーを渡す
            mav.setViewName("/editComment");

            // エラーメッセージを1件ずつ取り出してリストに追加する
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            mav.addObject("errorMessages", errorMessages);
            return mav;
        }
        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        // 編集した投稿を更新
        commentService.updateComment(comment);

        return new ModelAndView("redirect:/");
    }
}

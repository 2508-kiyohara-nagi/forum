package com.example.forum.controller.form;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class ReportForm {

    private int id;
    @NotBlank(message = "投稿内容を入力してください")
    private String content;
    private Timestamp createdDate;
    private Timestamp updatedDate;

}

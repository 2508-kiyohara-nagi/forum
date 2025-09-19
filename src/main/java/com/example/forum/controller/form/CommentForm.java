package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class CommentForm {

    private int id;
    private int reportId;
    @NotBlank(message = "コメントを入力してください")
    private String text;
    private Timestamp createdDate;
    private Timestamp updatedDate;

}


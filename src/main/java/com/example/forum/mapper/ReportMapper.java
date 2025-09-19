package com.example.forum.mapper;

import com.example.forum.repository.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;


@Mapper
public interface ReportMapper {

    List<Report> selectReportsByUpdatedDate(@Param("start") Timestamp start, @Param("end") Timestamp end);

    Report selectReportById(@Param("id") Integer id);

    void insertReport(Report report);

    void updateReport(Report report);

    void delete(@Param("id") Integer id);

    void updateReportUpdatedDate(@Param("id") int id, @Param("updatedDate") Timestamp updatedDate);

}

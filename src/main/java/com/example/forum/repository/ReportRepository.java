package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    //public List<Report> findAllByOrderByIdDesc();
    // createdDate が start ～ end の間にあるデータを取得
    //updateの降順に変更
    //public List<Report> findByCreatedDateBetweenOrderByCreatedDateDesc(Timestamp start, Timestamp end);
    public List<Report> findByUpdatedDateBetweenOrderByUpdatedDateDesc(Timestamp start, Timestamp end);
    // updated_date のみ更新
    @Modifying
    @Transactional
    @Query("UPDATE Report r SET r.updatedDate = :updatedDate WHERE r.id = :id")
    void updateUpdatedDate(@Param("id") int id, @Param("updatedDate") Timestamp updatedDate);
}








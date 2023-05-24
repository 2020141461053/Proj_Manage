package com.example.eback.dao;


import com.example.eback.entity.StockData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface StockDataDAO extends JpaRepository<StockData,Integer> {
    List<StockData> findBySid(String sid);
    Page<StockData> findAllBy(Pageable page);
    Page<StockData> findById(int id, Pageable page);

    @Modifying
    @Query(value = "SELECT * FROM `stock_data` WHERE `stock_code`= ? AND `time` >= ? AND `time` <= ?" ,nativeQuery = true)
    List<StockData> findByDate(String sid, Date start,Date end);

    boolean existsByDateAndSid(Date date,String sid);
}

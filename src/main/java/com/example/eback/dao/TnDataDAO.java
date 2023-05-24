package com.example.eback.dao;

import com.example.eback.entity.StockData;
import com.example.eback.entity.TnData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TnDataDAO extends JpaRepository<TnData,Integer> {
    TnData findByStockCodeAndStartAndEnd(String stock_code, Date start, Date end);
    boolean existsByStockCodeAndStartAndEnd(String stock_code, Date start, Date end);
}

package com.example.eback.dao;

import com.example.eback.entity.Stock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockDAO extends JpaRepository<Stock,Integer> {

    List<Stock> findByNameLike(String name);

    List<Stock> findByIdLike(String id);
    Page<Stock> findAllBy(Pageable page);
    List<Stock> findAll();
    Stock findByCode(String id);
    boolean existsByCode(String sid);


}

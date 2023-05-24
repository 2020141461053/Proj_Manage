package com.example.eback.dao;

import com.example.eback.entity.User_Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoriteDAO extends JpaRepository<User_Stock,Integer> {
    Page <User_Stock> findAllByUid(int uid, Pageable page);
    User_Stock findByUidAndSid(int uid,String sid);
    boolean existsUser_StockBySidAndUid(String sid,int uid);
    void deleteBySidAndUid(String sid,int uid);


}

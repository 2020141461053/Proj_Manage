package com.example.eback.service;

import com.example.eback.dao.FavoriteDAO;
import com.example.eback.dao.StockDAO;
import com.example.eback.dao.UserDAO;
import com.example.eback.entity.Stock;
import com.example.eback.entity.User_Stock;
import com.example.eback.result.Result;
import com.example.eback.result.ResultFactory;
import com.example.eback.util.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Slf4j
public class FavoriteService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    StockDAO stockDAO;

    @Autowired
    FavoriteDAO favoriteDAO;

    /**
     * 查看关注的股票
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public MyPage<Stock> findAll(int uid, int page, int size){
        MyPage<Stock> stocks=new MyPage<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Page<User_Stock> userStocks=favoriteDAO.findAllByUid(uid, PageRequest.of(page, size, sort));
        List<Stock> stockList =new ArrayList<>();
        for (User_Stock u_s : userStocks.getContent()){
            log.info(u_s.getSid());
            stockList.add(stockDAO.findById(u_s.getSid()));
        }
        stocks.setContent(stockList);
        stocks.setTotalElements(userStocks.getTotalElements());
        stocks.setPageNumber(userStocks.getPageable().getPageNumber());
        stocks.setPageSize(userStocks.getPageable().getPageSize());
        stocks.setNumberOfElements(userStocks.getNumberOfElements() );

        return  stocks;
    }
    /**
     * 添加关注的股票，成功添加返回1，已有返回0
     * @param uid
     * @param sid
     * @return
     */
    public Result addFavorite(int uid, String sid){
        User_Stock userStock=new User_Stock();
        if (!stockDAO.existsById(sid)){
            return  ResultFactory.buildFailResult("添加失败，已有此股票");
        }
        if (favoriteDAO.existsUser_StockBySidAndUid(sid,uid)){
            return ResultFactory.buildFailResult("添加失败，无此股票");
        }
        userStock.setSid(sid);
        userStock.setUid(uid);
        log.info("user stock: {}",userStock);
        favoriteDAO.save(userStock);
        return  ResultFactory.buildSuccessResult("添加成功");
    }

    /**
     * 删除关注的股票，成功返回1 失败（没有关注）返回0
     * @param uid
     * @param sid
     * @return
     */
    public Result deleteFavorite(int uid,String sid){
        if (!favoriteDAO.existsUser_StockBySidAndUid(sid,uid)){
            return ResultFactory.buildFailResult("删除失败，未添加此股票");
           }
        User_Stock u=favoriteDAO.findByUidAndSid(uid,sid);

        favoriteDAO.delete(u);

        return ResultFactory.buildSuccessResult("删除成功");
    }
}

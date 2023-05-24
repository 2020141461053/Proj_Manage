package com.example.eback.service;

import com.example.eback.dao.StockDAO;
import com.example.eback.entity.Stock;
import com.example.eback.util.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    @Autowired
    StockDAO stockDAO;

    public boolean exsistById(String sid) {
        return stockDAO.existsById(sid);
    }

    public MyPage<Stock> findAllByPage(int page, int size, String s) {
        Sort sort = Sort.by(Sort.Direction.DESC, s);
        return new MyPage<Stock>(stockDAO.findAllBy(PageRequest.of(page, size, sort)));
    }
    public  List<Stock> findAll(){
        return  stockDAO.findAll();
    }
    public Stock findById(String sid) {
        return stockDAO.findById(sid);
    }

    public List<Stock> findByNameLike(String sname) {
        return stockDAO.findByNameLike(sname);
    }

    public List<Stock> findByIdLike(String id) {
        return stockDAO.findByIdLike(id);
    }

    public void saveStocks(List<Stock> stocks) {
        stockDAO.saveAll(stocks);
    }
    public void saveStock(Stock stocks) {
        stockDAO.save(stocks);
    }


}

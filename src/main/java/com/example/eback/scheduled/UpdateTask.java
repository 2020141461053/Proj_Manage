package com.example.eback.scheduled;

import com.example.eback.controller.StockDataController;
import com.example.eback.entity.Stock;
import com.example.eback.listener.StockDataPublisher;
import com.example.eback.redis.RedisService;
import com.example.eback.service.StockDataService;
import com.example.eback.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateTask {
    @Autowired
    RedisService redisService;
    @Autowired
    StockService stockService;
    @Autowired
    StockDataService stockDataService;
    @Autowired
    private StockDataPublisher stockDataPublisher;
   // @Scheduled(cron = "0 1 * * * *") // 每小时执行一次
    public void setHistoryData() {
        List<String> stock_codes= getCodes();
        for( String stock_code :stock_codes){
            stockDataService.AddHistoryData(stock_code,"1");
        }

    }

   // @Scheduled(cron = "0 30 9 * * *") // 每天9：30点执行一次 获取开盘数据
    public void GetOpenData() {
        getData();
    }

   // @Scheduled(cron = "0 * 9-15 * * *") // 每分钟执行一次
    public void getData() {
        // 常规获取数据 放到 Redis 里
        StockDataController.GetAndPublish(stockService, redisService, stockDataPublisher);

    }

    private  List<String> getCodes(){
        List<Stock> stockList=stockService.findAll();
        List<String> stock_codes= new ArrayList<>();
        for( Stock stock:stockList){
            stock_codes.add(stock.getCode());
        }
        return stock_codes;
    }

}

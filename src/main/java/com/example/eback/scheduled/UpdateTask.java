package com.example.eback.scheduled;

import com.example.eback.entity.Stock;
import com.example.eback.entity.StockData;
import com.example.eback.listener.StockDataPublisher;
import com.example.eback.redis.RedisService;
import com.example.eback.service.StockDataService;
import com.example.eback.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    @Scheduled(cron = "0 1 * * * *") // 每小时执行一次
    public void setHistoryData() {
        List<String> stock_codes= getCodes();
        for( String stock_code :stock_codes){
            stockDataService.AddHistoryData(stock_code,"1");
        }

    }

    @Scheduled(cron = "0 30 9 * * *") // 每天9：30点执行一次
    public void Open() {
        getData();
    }

    @Scheduled(cron = "0 * * * * *") // 每分钟执行一次
    public void getData() {
        // 常规获取数据 放到 Redis 里
        StockData stockData;
        List<Stock> stockList=stockService.findAll();
        List<String> stock_codes= new ArrayList<>();
        for( Stock stock:stockList){
            stock_codes.add(stock.getId());
        }
        String codes = String.join(",", stock_codes);
        String new_url="http://hq.sinajs.cn/list="+codes;
        try {
            URL url = new URL("new_url");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("referer", "http://finance.sina.com.cn");
            int status = conn.getResponseCode();
            if (status == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                int i=0;
                Stock stock;
                while ((line = reader.readLine()) != null) {
                    stockData=new StockData();
                    stockData.parse(line);
                    redisService.set(stock_codes.get(i),stockData);
                    stockDataPublisher.publishStockDataEvent(stockData);
                    stock=stockList.get(i);
                    if (stock.getMax_high()<stockData.getValue()) {
                        stock.setMax_high(stockData.getValue());
                        stockService.saveStock(stock);
                    }
                    else  if (stock.getMin_low()>stockData.getValue()) {
                        stock.setMin_low(stockData.getValue());
                        stockService.saveStock(stock);
                    }
                    i++;
                }
                reader.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private  List<String> getCodes(){
        List<Stock> stockList=stockService.findAll();
        List<String> stock_codes= new ArrayList<>();
        for( Stock stock:stockList){
            stock_codes.add(stock.getId());
        }
        return stock_codes;
    }

}

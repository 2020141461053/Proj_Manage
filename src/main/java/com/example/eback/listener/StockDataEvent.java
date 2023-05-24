package com.example.eback.listener;

import com.example.eback.entity.StockData;
import org.springframework.context.ApplicationEvent;

public class StockDataEvent extends ApplicationEvent {
    private  StockData stockData;
    public StockDataEvent(Object source, StockData stockData) {
        super(source);
        this.stockData=stockData;

    }

    public StockData getStockData() {
        return stockData;
    }
}

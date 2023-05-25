package com.example.eback.listener;

import org.springframework.context.ApplicationEvent;

public class StockDataEvent extends ApplicationEvent {
    private  String stockCode;
    public StockDataEvent(Object source, String stockCode) {
        super(source);
        this.stockCode=stockCode;

    }

    public String getStockCode() {
        return stockCode;
    }
}

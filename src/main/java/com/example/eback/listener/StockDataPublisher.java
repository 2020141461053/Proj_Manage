package com.example.eback.listener;

import com.example.eback.entity.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class StockDataPublisher {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void publishStockDataEvent(String stockCode) {
        StockDataEvent event = new StockDataEvent(this,stockCode);
        eventPublisher.publishEvent(event);
    }
}

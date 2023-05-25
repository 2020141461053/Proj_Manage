package com.example.eback.listener;

import com.example.eback.service.StockDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class StockDataListener implements ApplicationListener<StockDataEvent> {
    @Autowired
    StockDataService stockDataService;
    @Override
    @EventListener(StockDataEvent.class)
    public void onApplicationEvent(StockDataEvent event) {
        try {
            stockDataService.AddHistoryData(event.getStockCode(),"1024");
        } catch (Exception e) {
            log.error("error", e);
        }

    }
}

package com.example.eback.listener;

import com.example.eback.webSocket.WebSocket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class StockDataListener implements ApplicationListener<StockDataEvent> {
    @Override
    @EventListener(StockDataEvent.class)
    public void onApplicationEvent(StockDataEvent event) {
        try {
            WebSocket.sendInfo(event.getStockData());
        } catch (Exception e) {
            log.error("error", e);
        }

    }
}

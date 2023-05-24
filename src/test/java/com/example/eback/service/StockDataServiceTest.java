package com.example.eback.service;

import com.example.eback.entity.StockData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class StockDataServiceTest {

    @Autowired
    StockDataService stockDataService;

    private static StockData stockDataExpected = new StockData();
    private static List<StockData> stockDataList = null;

    private String sid;
    @Before
    public void setUp() throws Exception {
        sid = "aaa";
        stockDataList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2023-03-09");
        stockDataExpected.setId(130);
        stockDataExpected.setHigh(1);
        stockDataExpected.setLow(0);
        stockDataExpected.setTime(date);
        stockDataExpected.setSid(sid);
        stockDataExpected.setTurnover(100);
        stockDataExpected.setValue(6);
        stockDataExpected.setVolume(123456);
        stockDataService.saveDataList(stockDataList);
        stockDataList.add(stockDataExpected);
        stockDataService.saveDataList(stockDataList);
    }

    @Test
    public void findById() {
        List<StockData> stockData = stockDataService.findById(sid);
        assertEquals(stockDataList,stockData);
    }

    @Test
    public void saveData() {
        stockDataService.saveData(stockDataExpected);
        List<StockData> dataList = stockDataService.findById(stockDataExpected.getSid());
        assertEquals(stockDataList,dataList);

    }

    @Test
    public void saveDataList() {
        stockDataService.saveDataList(stockDataList);
        for (StockData stockData : stockDataList){
            List<StockData> stockDataList1 = stockDataService.findById(stockData.getSid());
            assertTrue(stockDataList1.contains(stockData));
        }
    }
}

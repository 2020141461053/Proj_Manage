package com.example.eback.service;

import com.example.eback.entity.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import javax.validation.constraints.AssertTrue;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class StockServiceTest {

    @Autowired
    StockService stockService;

    private String sId;
    private static Stock stockExpected = new Stock();

    private List<Stock> stocksExpected = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        sId = "testStock";
        stockExpected.setId("testStock");
        stockExpected.setName("test");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2023-03-08");
        stockExpected.setCreate_date(date);
        stockExpected.setMin_low(1);
        stockExpected.setMax_high(1);
        stocksExpected.add(stockExpected);
        stockService.saveStocks(stocksExpected);
    }

    @Test
    public void exsistById() {
        boolean f = stockService.exsistById(sId);
        assertTrue(f);
        sId = "no";
        f = stockService.exsistById(sId);
        assertTrue(!f);
//        Assertions.assertArrayEquals(stocks.toArray());
    }

    @Test
    public void findAll() {

    }

    @Test
    public void findById() {
        Stock stock = stockService.findById(sId);
        assertEquals(stockExpected,stock);
    }

    @Test
    public void findByNameLike() {
        List<Stock> stocks = stockService.findByNameLike("te");
        for (Stock stock : stocks) {
            String name = stock.getName();
            assertTrue(name.contains("te"));
        }
    }

    @Test
    public void findByIdLike() {
        List<Stock> stocks = stockService.findByIdLike("te");
        for (Stock stock : stocks) {
            String id = stock.getId();
            assertTrue(id.contains("te"));
        }
    }

    @Test
    public void saveStocks() {
        stockService.saveStocks(stocksExpected);
        Stock s = stockService.findById(stockExpected.getId());
        assertEquals(stockExpected,s);
    }
}

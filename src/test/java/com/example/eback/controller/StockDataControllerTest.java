package com.example.eback.controller;

import com.alibaba.fastjson.JSON;
import com.example.eback.EBackApplication;
import com.example.eback.entity.StockData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = EBackApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class StockDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String sid;

    private StockData stockData = new StockData();

    private String jsonStr = "";

    private Model model;

    @Before
    public void setUp() throws Exception {
        this.sid = "BIDU.O";
        this.stockData.setSid("test");
        this.stockData.setValue(123);
        this.stockData.setVolume(123456);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2023-03-10");
        this.stockData.setTime(date);
        this.stockData.setLow(100);
        this.stockData.setHigh(125);
        this.stockData.setTurnover(120);
        this.stockData.setClose(124);
        this.stockData.setOpen(121);
        jsonStr = JSON.toJSONString(stockData);
    }

    @Test
    public void getBysid() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/stock_data/get",sid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void exportCSV() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/stock_data/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void uploadCsv() {
    }

    @Test
    public void add() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/stock_data/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void storeTnData() {
    }

    @Test
    public void getTnData() {
    }
}

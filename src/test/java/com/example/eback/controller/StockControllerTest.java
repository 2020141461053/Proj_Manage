package com.example.eback.controller;

import com.example.eback.EBackApplication;
import com.example.eback.redis.RedisService;
import com.example.eback.service.StockDataService;
import com.example.eback.service.StockService;
import com.example.eback.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EBackApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String sort = "";

    private int page;

    private int size;

    private Model model;

    @Before
    public void setUp() throws Exception {
        this.sort = "id";
        this.page = 0;
        this.size = 10;
    }


    @Test
    public void getAll() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/stock?pages=0&size=10&sort=id") // 根据请求方式决定调用的方法
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect( // 预判结果，类似断言
                        MockMvcResultMatchers
                                .status().isOk())
                .andDo( // 需要执行某任务
                        MockMvcResultHandlers.print()); // 打印日志
    }

    @Test
    public void getByCode() throws Exception {
        String stockCode = "testtest";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/stock/get_code")
                        .accept(MediaType.APPLICATION_JSON).param("stockCode",stockCode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getByName() throws Exception {
        String stockName = "string";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/stock/get_name")
                        .accept(MediaType.APPLICATION_JSON).param("stockName",stockName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    @Test
    public void uploadCsv() throws Exception {
//        File file = new File("C:\\Users\\34719\\Desktop\\test.csv");
//        MockMultipartFile firstFile = new MockMultipartFile("file", "test.csv",
//                MediaType.TEXT_PLAIN_VALUE, new FileInputStream(file));
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/stock/upload",model).file(firstFile)
//                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getUser() {
    }
}
package com.example.eback.controller;

import com.alibaba.fastjson.JSON;
import com.example.eback.EBackApplication;
import com.example.eback.entity.User_Stock;
import com.example.eback.service.FavoriteService;
import com.example.eback.service.UserService;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EBackApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FavoriteService favoriteService;

    @MockBean
    UserService userService;

    private User_Stock user_stock = new User_Stock();

    private String jsonStr = "";

    private int uid;

    private String sid;

    @Before
    public void setUp() throws Exception {
        this.uid = 1;
        this.sid = "test";
        this.user_stock.setId(7);
        this.user_stock.setSid("test");
        this.user_stock.setUid(1);
        jsonStr = JSON.toJSONString(user_stock);
    }

    @Test
    public void add() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/userstock/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void delete() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/userstock/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/userstock/favorite/page=0/size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Ignore
    public void getUser() {
    }
}
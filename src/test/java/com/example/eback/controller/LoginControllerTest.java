package com.example.eback.controller;

import com.alibaba.fastjson.JSON;
import com.example.eback.EBackApplication;
import com.example.eback.entity.User;
import com.example.eback.service.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EBackApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private User user = new User();

    private User userNew = new User();

    private String jsonStr = "";

    private String jsonRegister = "";
    @Before
    public void setUp() throws Exception {
        this.user.setUsername("123");
        this.user.setPassword("00000000");
        this.user.setId(0);
        this.user.setName("String");
        this.user.setRole("String");
        this.user.setSalt("String");
        this.userNew.setUsername("testRegister");
        this.userNew.setPassword("123");
        this.userNew.setId(0);
        this.userNew.setName("testRegister");
        this.userNew.setRole("String");
        this.userNew.setSalt("String");
        this.jsonStr= JSON.toJSONString(user);
        this.jsonRegister = JSON.toJSONString(userNew);
//        System.out.println(jsonStr);
    }

    @Test
    public void login() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login") // 根据请求方式决定调用的方法
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStr)
                                // 请求数据的文档类型，例如：application/json; charset=utf-8
                                //  application/x-www-form-urlencoded;charset=UTF-8
//                                .param() // 请求参数，有多个时，多次调用param()方法
                                .accept(MediaType.APPLICATION_JSON))
                // 接收的响应结果的文档类型，注意：perform()方法到此结束
                .andExpect( // 预判结果，类似断言
                        MockMvcResultMatchers
                                .status().isOk())
                .andDo( // 需要执行某任务
                        MockMvcResultHandlers.print()); // 打印日志
    }

    @Test
    public void register() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/register") // 根据请求方式决定调用的方法
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRegister)
                                .accept(MediaType.APPLICATION_JSON))
                // 接收的响应结果的文档类型，注意：perform()方法到此结束
                .andExpect( // 预判结果，类似断言
                        MockMvcResultMatchers
                                .status().isOk())
                .andDo( // 需要执行某任务
                        MockMvcResultHandlers.print()); // 打印日志
    }

    @Test
    public void logout() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/logout") // 根据请求方式决定调用的方法
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                // 接收的响应结果的文档类型，注意：perform()方法到此结束
                .andExpect( // 预判结果，类似断言
                        MockMvcResultMatchers
                                .status().isOk())
                .andDo( // 需要执行某任务
                        MockMvcResultHandlers.print()); // 打印日志

    }

    @Test
    @Ignore
    public void authentication() {
    }
}
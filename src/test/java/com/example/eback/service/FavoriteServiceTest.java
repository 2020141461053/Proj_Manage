package com.example.eback.service;

import com.example.eback.dao.FavoriteDAO;
import com.example.eback.entity.User_Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class FavoriteServiceTest {

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    FavoriteDAO favoriteDAO;

    private User_Stock user_stock = new User_Stock();

    private String sid;

    private int uid;

    @Before
    public void setUp() throws Exception {
        user_stock.setUid(2);
        user_stock.setSid("testtest");
        favoriteDAO.save(user_stock);
        sid = "testtest";
        uid = 2;
    }

    @Test
    public void addFavorite() {
        favoriteService.addFavorite(uid,sid);
        User_Stock userStock = favoriteDAO.findByUidAndSid(uid,sid);
        assertEquals(user_stock,userStock);
    }

    @Test
    public void deleteFavorite() {
        favoriteService.deleteFavorite(uid,sid);
        User_Stock userStock = favoriteDAO.findByUidAndSid(uid,sid);
        assertNull(userStock);
    }
}
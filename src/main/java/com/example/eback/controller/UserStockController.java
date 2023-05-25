package com.example.eback.controller;

import com.example.eback.entity.User;
import com.example.eback.entity.User_Stock;
import com.example.eback.result.Result;
import com.example.eback.result.ResultFactory;
import com.example.eback.service.FavoriteService;
import com.example.eback.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ApiOperation(value = "用户和股票相关的控制接口")
@RestController
public class UserStockController {
    @Autowired
    FavoriteService favoriteService;

    @Autowired
    UserService userService;

    @ApiOperation(value = "添加关注的股票", notes = "只需要填写String sid")
    @PostMapping("/api/userstock/add")
    public Result add(@RequestBody User_Stock stock) {
        String sid = stock.getSid();
        User user = getUser();
        int uid = user.getId();
        return favoriteService.addFavorite(uid, sid);
    }


    @ApiOperation(value = "删除关注的股票", notes = "只需要填写int sid")
    @PostMapping("/api/userstock/delete")
    public Result delete(@RequestBody User_Stock stock) {
        String sid = stock.getSid();
        User user = getUser();
        int uid = user.getId();
        return favoriteService.deleteFavorite(uid, sid);
    }

    @ApiOperation(value = "获取关注的股票", notes = "url设置网页和size")
    @GetMapping("/api/userstock/favorite/page={page}/size={size}")
    public Result getAll(@PathVariable("page") int pages, @PathVariable("size") int size) {
        User user = getUser();
        return ResultFactory.buildSuccessResult(favoriteService.findAll(user.getId(), pages, size));
    }

    public User getUser() {
        return userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString());
    }
    @ApiOperation(value = "用户风险类型判断" , notes = "")
    @GetMapping("/RiskType/{score}")
    public String determineRiskType(@PathVariable("score")int score) {
        if (score <= 8) {
            return "Low Risk";
        } else if (score <= 12) {
            return "Moderate Risk";
        } else if (score <= 16) {
            return "High Risk";
        } else {
            return "Very High Risk";
        }
    }


}

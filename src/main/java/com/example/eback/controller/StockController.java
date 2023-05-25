package com.example.eback.controller;

import com.example.eback.entity.Stock;
import com.example.eback.entity.StockData;
import com.example.eback.entity.User;
import com.example.eback.listener.StockDataPublisher;
import com.example.eback.redis.RedisService;
import com.example.eback.result.Result;
import com.example.eback.result.ResultFactory;
import com.example.eback.service.StockDataService;
import com.example.eback.service.StockService;
import com.example.eback.service.UserService;
import com.example.eback.util.MyPage;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@ApiOperation(value = "股票信息相关接口")
@RestController
public class StockController {
    @Autowired
    UserService userService;

    @Autowired
    StockDataService stockDataService;

    @Autowired
    StockService stockService;

    @Autowired
    RedisService redisService;

    @Autowired
    private StockDataPublisher stockDataPublisher;

    private Logger log = LogManager.getLogger("stock");

    @ApiOperation(value = "获取股票列表", notes = "")
    @PostMapping("/api/stock")
    public Result getAll(@RequestParam("pages") int pages,
                         @RequestParam("size") int size,
                         @RequestParam("sort") String sort) {

        MyPage<Stock> stockMypage = stockService.findAllByPage(pages, size, sort);
        List<Stock> stockList = stockMypage.getContent();
        StockData stockInRedis;
        List<Stock> returnList = new ArrayList<>();//返回的列表数据
        for (Stock stock : stockList) {
            //stockInRedis = (StockData) redisService.get(stock.getCode());//查询Redis里的最新数据
            /*if (stockInRedis == null) {
                //stock.setTurnover(0);
                stock.setValue(0);
                stock.setHigh(0);
                stock.setLow(0);
            } else {
               // stock.setTurnover(stockInRedis.getTurnover());
                stock.setValue(stockInRedis.getValue());
                stock.setHigh(stockInRedis.getHigh());
                stock.setLow(stockInRedis.getLow());
            }*/
            stock.setValue(0);
            stock.setHigh(0);
            stock.setLow(0);
            returnList.add(stock);//添加到列表里
        }
        stockMypage.setContent(returnList);//替换列表
        return ResultFactory.buildSuccessResult(stockMypage);
    }

    @ApiOperation(value = "上传新的股票", notes = "需要股票名称")
    @PostMapping("/api/stock/add")
    public Result add(@RequestBody Stock stock) {
        String sid = stock.getCode();
        if (stockService.exsistByCode(sid)) {
            return ResultFactory.buildFailResult("已有此股票");
        }
        stockService.saveStock(stock);
        stockDataPublisher.publishStockDataEvent(sid);

        return ResultFactory.buildSuccessResult("添加成功");
    }

    @ApiOperation(value = "根据股票代码查询", notes = "需要股票代码")
    @PostMapping("/api/stock/get_code")
    public Result  getByCode(@RequestParam("stockCode") String scode) {
        List<Stock> stocks = stockService.findByIdLike("%" + scode + "%");
        return  ResultFactory.buildSuccessResult(stocks);
    }

    @ApiOperation(value = "根据股票名称查询", notes = "需要股票名称")
    @PostMapping("/api/stock/get_name")
    public Result getByName(@RequestParam("stockName") String sname) {
        List<Stock> stocks = stockService.findByNameLike("%" + sname + "%");
        return ResultFactory.buildSuccessResult(stocks);
    }
/*
    @RequiresPermissions("admin")
    @ApiOperation(value = "批量上传新的股票", notes = "")
    @PostMapping("/api/stock/upload")
    public Result uploadCsv(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                CSVReader csvReader = new CSVReader(reader);
                // create csv bean reader
                CsvToBean<Stock> csvToBean = new CsvToBeanBuilder<Stock>(csvReader)
                        .withType(Stock.class)
                        .withSeparator(',')
                        .withQuoteChar('"')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of stocks
                List<Stock> stocks = csvToBean.parse();

                stockService.saveStocks(stocks);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }
        return ResultFactory.buildFailResult("上传成功");
    }
*/
    public User getUser() {
        User user = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString());
        return user;
    }




}

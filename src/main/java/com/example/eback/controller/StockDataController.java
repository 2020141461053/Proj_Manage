package com.example.eback.controller;

import com.example.eback.constans.TnDataCode;
import com.example.eback.listener.StockDataPublisher;
import com.example.eback.entity.Stock;
import com.example.eback.entity.StockData;
import com.example.eback.redis.RedisService;
import com.example.eback.result.Result;
import com.example.eback.result.ResultFactory;
import com.example.eback.service.StockDataService;
import com.example.eback.service.StockService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiOperation(value = "股票数据相关接口")
@RestController
public class StockDataController {

    @Autowired
    StockDataService stockDataService;
    @Autowired
    RedisService redisService;

    @Autowired
    StockService stockService;

    @Autowired
    private StockDataPublisher stockDataPublisher;

    private Logger log = LogManager.getLogger("stock");

    @ApiOperation(value = "获取某只股票的全部相关数据", notes = "只需要填写string sid")
    @GetMapping("/api/stock_data/get")
    public Result getBysid(@RequestParam("sid")String sid) {
        List<StockData> stockDatas = stockDataService.findById(sid);
        //stockDatas.add((StockData) redisService.get(sid));
        return ResultFactory.buildSuccessResult(stockDatas);
    }

    @ApiOperation(value = "导出某只股票的全部相关数据", notes = "需要该股票的String sid")
    @GetMapping("/api/stock_data/export")
    public void exportCSV(HttpServletResponse response, @RequestParam String sid) throws Exception {
        String filename = sid +".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<StockData> writer = new StatefulBeanToCsvBuilder<StockData>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //获取数据
        List<StockData> stockDatas = stockDataService.findById(sid);
        stockDatas.add((StockData) redisService.get(String.valueOf(sid)));

        writer.write(stockDatas);

    }
/*
    //@RequiresPermissions("admin")
    @ApiOperation(value = "上传股票历史详情信息", notes = "")
    @PostMapping("/api/stock_data/upload")
    public Result uploadCsv(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                CSVReader csvReader = new CSVReader(reader);
                // create csv bean reader
                CsvToBean<StockData> csvToBean = new CsvToBeanBuilder<StockData>(csvReader)
                        .withType(StockData.class)
                        .withSeparator(',')
                        .withQuoteChar('"')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of stocks
                List<StockData> stockDataList = csvToBean.parse();
                System.out.print(stockDataList);
                stockDataService.saveDataList(stockDataList);
                // TODO: save stocks in DB?

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }
        log.info("{}添加了批量上传文件", SecurityUtils.getSubject().getPrincipal().toString());

        return ResultFactory.buildFailResult("上传成功");
    }
*/

/**
   // @RequiresPermissions("admin")
    @ApiOperation(value = "更新某只股票的信息", notes = "全部填写")
    @PostMapping("/api/stock_data/add")
    public Result add(@RequestBody StockData stockData) {
        String sid = stockData.getSid();
        if (!stockService.exsistById(sid)) {
            return ResultFactory.buildFailResult("无此股票，请先登记");
        }
        Object in_redis = redisService.get(String.valueOf(sid));
        StockData last_data;

        // 更新数据库里的数据

        stockDataService.saveData(stockData);
        redisService.set(String.valueOf(sid), stockData);
        //   同步信息，使用异步的事件驱动

        stockDataPublisher.publishStockDataEvent(stockData);

        return ResultFactory.buildSuccessResult("成功");
    }
*/
    //@RequiresPermissions("admin")
    @ApiOperation(value = "保存Tn的数据", notes = "全部填写")
    @PostMapping("/api/stock_data_Tn/update")
    public Result StoreTnData(@RequestParam("stock_code") String s_code,
                              @RequestParam("endDay") Date endDay,
                              @RequestParam("day_num") int day_num) {

        return ResultFactory.buildSuccessResult(stockDataService.StoreTnData(s_code,endDay,day_num));
    }

    @ApiOperation(value = "获得Tn的数据，数据聚合功能", notes = "全部填写")
    @PostMapping("/api/stock_data_Tn/get")
    public Result GetTnData(@RequestParam("stock_code") String s_code,
                              @RequestParam("endDay") Date endDay,
                              @RequestParam("startDay") Date  startDay){
        TnDataCode tnDataCode = stockDataService.existsByStockCodeAndStartAndEnd(s_code, endDay, startDay);
        if (tnDataCode.getCode()< 0){
            return  ResultFactory.buildFailResult(tnDataCode.getMsg());
        }
        return ResultFactory.buildSuccessResult(stockDataService.getTnData(s_code, endDay, startDay));

    }

    @ApiOperation(value = "刷新全部数据", notes = "全部填写")
    @GetMapping("/api/stock_data_flash")
    public Result GetHisData(){
        // 常规获取数据 放到 Redis 里
        GetAndPublish(stockService, redisService, stockDataPublisher);
        return  ResultFactory.buildSuccessResult("");

    }

    public static void GetAndPublish(StockService stockService, RedisService redisService, StockDataPublisher stockDataPublisher) {
        StockData stockData;
        List<Stock> stockList= stockService.findAll();
        List<String> stock_codes= new ArrayList<>();
        for( Stock stock:stockList){
            stock_codes.add(stock.getCode());
        }
        String codes = String.join(",", stock_codes);
        String new_url="http://hq.sinajs.cn/list="+codes;
        if(codes.equals(""))
            return;
        try {
            URL url = new URL(new_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("referer", "http://finance.sina.com.cn");
            int status = conn.getResponseCode();
            if (status == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                int i=0;
                Stock stock;
                while ((line = reader.readLine()) != null) {
                    stockData=new StockData();
                    stockData.parse(line);
                    redisService.set(stock_codes.get(i),stockData);
                    /**
                     * 删除了websocket更新
                     */
                    //stockDataPublisher.publishStockDataEvent(stockData);
                    stock=stockList.get(i);
                    if (stock.getMax_high()<stockData.getValue()) {
                        stock.setMax_high(stockData.getValue());
                        stockService.saveStock(stock);
                    }
                    else  if (stock.getMin_low()>stockData.getValue()) {
                        stock.setMin_low(stockData.getValue());
                        stockService.saveStock(stock);
                    }
                    i++;
                }
                reader.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

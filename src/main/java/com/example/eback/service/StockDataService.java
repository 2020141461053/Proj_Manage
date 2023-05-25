package com.example.eback.service;

import com.example.eback.constans.TnDataCode;
import com.example.eback.dao.StockDataDAO;
import com.example.eback.dao.TnDataDAO;
import com.example.eback.entity.StockData;
import com.example.eback.entity.TnData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StockDataService {
    @Autowired
    StockDataDAO stockDataDAO;

    @Autowired
    TnDataDAO tnDataDAO;

    public List<StockData> findById(String sid) {
        return stockDataDAO.findBySid(sid);
    }

    public void saveData(StockData stockData) {
        stockDataDAO.save(stockData);
    }

    private  Date getDate(Date end,int day){
        Calendar cal = Calendar.getInstance();
        //设置时间
        cal.setTime(end);
        cal.add(Calendar.DATE, -day);
        return cal.getTime();

    }
    public TnDataCode StoreTnData(String id, Date point, int day) {

        Date start = getDate(point,day);

        List<StockData> stockDataList = stockDataDAO.findByDate(id, start, point);
        if (tnDataDAO.existsByStockCodeAndStartAndEnd(id, start, point)){
            return TnDataCode.IS_EXISTS;
        }

        TnData tnData = new TnData();

        float high = 0;

        float value = 0;//平均值

        int turnover = 0;//总交易数

        float low = 0;//最低

        float valueChange = 0;//
        for (StockData stockData : stockDataList) {
            if (value == 0) {
                high = stockData.getHigh();
                low = stockData.getLow();
                value = stockData.getValue();
                //turnover = stockData.getTurnover();
            } else {
                if (high < stockData.getHigh()) {
                    high = stockData.getHigh();
                }
                if (low > stockData.getLow()) {
                    low = stockData.getLow();
                }
                value += stockData.getValue();
                //turnover += stockData.getTurnover();
            }
        }
        value /= day;
        valueChange = stockDataList.get(0).getValue() - stockDataList.get(stockDataList.size() - 1).getValue();
        tnData.setHigh(high);
        tnData.setLow(low);
        tnData.setEnd(point);
        tnData.setStart(start);
        tnData.setStockCode(id);
       // tnData.setTurnover(turnover);
        tnData.setValue(value);
        tnData.setValueChange(valueChange);
        tnDataDAO.save(tnData);
        return TnDataCode.SAVE_SUCCESS;
    }


    public void saveDataList(List<StockData> stockDataList) {
        stockDataDAO.saveAll(stockDataList);
    }

    public TnDataCode existsByStockCodeAndStartAndEnd(String StockCode, Date start, Date end){
        if (tnDataDAO.existsByStockCodeAndStartAndEnd(StockCode, start, end)){
            return TnDataCode.IS_EXISTS;
        }
        else return TnDataCode.NOT_EXISTS;
    }

    public  TnData getTnData(String StockCode, Date start, Date end){

        return tnDataDAO.findByStockCodeAndStartAndEnd(StockCode, start, end);
    }
    public  String  AddHistoryData(String stock_code,String times){
        String url = "http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol="
                + stock_code
                +"&scale=60&ma=5datalen="
                +times;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            JsonParser parser = new JsonParser();
            inputLine = in.readLine();
            JsonArray jsonArray;
            try {
                 jsonArray = parser.parse(inputLine).getAsJsonArray();
            }
            catch (Exception e) {return "股票码不正确";}
            in.close();
            StockData stockData;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                // Get specific items from each object
                String day = jsonObject.get("day").getAsString();
                String open = jsonObject.get("open").getAsString();
                String high = jsonObject.get("high").getAsString();
                String low = jsonObject.get("low").getAsString();
                String close = jsonObject.get("close").getAsString();
                String volume = jsonObject.get("volume").getAsString();
                stockData=new StockData();

                try {
                    Date date = formatter.parse(day);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    if (stockDataDAO.existsByDateAndSid(date,stock_code) ||calendar.get(Calendar.HOUR_OF_DAY)==15)
                        continue;
                    stockData.setDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                stockData.setSid(stock_code);
                stockData.setHigh(Float.parseFloat(high));
                stockData.setLow(Float.parseFloat(low));
                stockData.setOpen(Float.parseFloat(open));
                stockData.setClose(Float.parseFloat(close));
                stockData.setVolume(Integer.parseInt(volume));
                stockDataDAO.save(stockData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "成功";
    }

}

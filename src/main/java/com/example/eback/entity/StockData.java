package com.example.eback.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stock_data")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
/**
 * 股票信息需要结合redis来发布，这里的信息是前一天的保留信息，仅作为寻找Key加载用
 */
public class StockData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String sid;//股票号


    private float value;//当前价格 （过往数据的计算移动平均线）

    private float low;//当前日低

    private  float high;//当前日高
    
    private Date date;//时间
    
    private int volume;//成交量

    private float open;//今日开盘价

    private  float close;//今日收盘价

    @Transient
    private  float buy_price;//竞买价
    @Transient
    private  float sell_price;//竞卖价
    @Transient
    private float last_close;//昨日收盘价



    public void parse(String apiResponse) {
        try {
            String data = apiResponse.split("=")[1].replaceAll("\"", "");
            String[] values = data.split(",");

            open = Float.parseFloat(values[1]);
            last_close = Float.parseFloat(values[2]);
            value = Float.parseFloat(values[3]);
            high = Float.parseFloat(values[4]);
            low = Float.parseFloat(values[5]);
            buy_price = Float.parseFloat(values[6]);
            sell_price = Float.parseFloat(values[7]);
            volume = Integer.parseInt(values[8]);
            close = Float.parseFloat(values[2]); // assuming close is yesterday's close price

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(values[30] + " " + values[31]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}

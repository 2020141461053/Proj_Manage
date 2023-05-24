package com.example.eback.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tNData")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class TnData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "stock_code")
    private String stockCode;

    private Date start;

    private Date end;

    private float value;//平均值

    private int turnover;//总交易数

    private float low;//最低

    private float high;//最高

    @Column(name = "value_change")
    private float valueChange;//变化值


}

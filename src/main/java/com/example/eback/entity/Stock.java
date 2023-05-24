package com.example.eback.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stock")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_code")
    private String id;

    private String name;

    private float min_low;

    private float max_high;

    @Transient
    private float low;

    @Transient
    private  float high;

    @Transient
    private float value;





}

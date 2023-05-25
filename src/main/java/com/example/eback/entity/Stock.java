package com.example.eback.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

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
    private  int id;
    private String code;

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

package com.example.eback.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * UserUtils entity.
 *
 * @author PBW
 * @date 1/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    //用户名
    @NotBlank(message = "用户名不能为空")
    private  String username;
    //密码
    @NotBlank(message = "密码不能为空")
    private String password;
    //盐
    private String salt;
    //角色
    private String role;
    //名称
    private String name;








}


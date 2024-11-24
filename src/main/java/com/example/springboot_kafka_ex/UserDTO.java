package com.example.springboot_kafka_ex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private String id;
  private String nickName;
  private String chatMsg;
  private String ststus;
  private int age;
}

package com.example.mongo.dto;
//document와 매핑될 객체를 정의 - 도메인클래스
//score컬렉션의 한 document와 매핑될 객체

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "score")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {
    @Id
    String _id;
    String id;
    String name;
    String dept;
    String addr;
    int java;
    int servlet;
}

package com.example.mongo.dao;

import com.example.mongo.dto.ScoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class ScoreMongoDAOImpl implements ScoreMongoDAO{
    //spring-data-mongodb에서 제공하는 몽고디비의 핵심클래스 - 데이터를제어할 수 있는 기능을 제공하는 클래스
    //Mybatis의 SqlSession클래스와 유사
    private final MongoTemplate mongoTemplate;
    private final ScoreRepository repository;

    @Override
    public ScoreDTO findById(String value) {//기본키 조회(_id필드값)
        return repository.findById(value).get();
    }
    @Override
    public ScoreDTO findById(String key, String value) {
        //id로 조회할 수 있도록 작업
        //search와 동일하게 조건을 적용해서 검색
        //mongoTemplate의 findOne 메소드를 이용해보기
        Criteria criteria = new Criteria(key);
        criteria.is(value);
        Query query = new Query(criteria);
        ScoreDTO dto = mongoTemplate.findOne(query,ScoreDTO.class,"score");
        return dto;
    }
    @Override
    public List<ScoreDTO> findCriteria(String key, String value) {
       //Query는 조건 정보를 담고 있는 클래스
       //1. 선택한 key(필드)에 따른 value가 정확하게 일치하는 document를 검색
        //Criteria 객체는 조건을 어떤 필드에 어떻게 적용할 것인지 정보를 담고 있는 객체
        //Criteria 객체를 이용해서 Query 객체를 만들어서 MongoTemplate의 find를 호출
//        Criteria criteria = new Criteria(key);
//        criteria.is(value);
        //2. key(필드)와 조건을 value의 조건을 함께 적용
        String[] data = key.split(",");
//        Criteria criteria = new Criteria(data[0]);
//        if(data[1].equals("is")){
//            criteria.is(value);
//        }else if(data[1].equals("gt")){
//            criteria.gt(Integer.parseInt(value));
//        } else if (data[1].equals("lt")) {
//            criteria.lt(Integer.parseInt(value));
//        }
        //3. Query클래스의 where 메소드를 이용해서 처리

//        Query query = new Query();
//        query.addCriteria(Criteria.where(data[0]).lte(Integer.parseInt(value)));
        //4. 정규표현식으로 검색
        Criteria criteria = new Criteria(data[0]);
        //^=> 해당 필드의 값 중 ^ 다음의 문자열로 시작하는 데이터 => like연산자와 동일 where dept like : '인사%'
        //dept like '%사%'
        //criteria.regex("^"+value);
        //.은 정규표현식에서 0개 이상의 임의의 문자를 나타내는 패턴 문자열
        criteria.regex(".*"+value+".*");
        Query query = new Query(criteria);

        List<ScoreDTO> list =mongoTemplate.find(query,ScoreDTO.class,"score");
        System.out.println(list);
        return list;
    }



    @Override
    public void insertDocument(ScoreDTO doc) {
        mongoTemplate.insert(doc);
    }

    @Override
    public void insertAllDocument(List<ScoreDTO> docs) {
        mongoTemplate.insertAll(docs);
    }

    @Override
    public void update(ScoreDTO document) {
        Criteria criteria = new Criteria("id");
        criteria.is(document.getId());
        Query query = new Query(criteria);

        //업데이트기능을 수행하는 객체를 생성하고 적절한 값을 셋팅
        Update update = new Update();
        update.set("dept",document.getDept());
        update.set("addr",document.getAddr());
        mongoTemplate.updateMulti(query,update,"score");
    }

    @Override
    public void test1() {

    }

    @Override
    public List<ScoreDTO> findAll() {
        System.out.println("===================================================");
        List<ScoreDTO> list = mongoTemplate.findAll(ScoreDTO.class,"score");
        System.out.println(list);
        return list;
    }

    @Override
    public List<ScoreDTO> findAll(int pageNo) {
        //PagingAndSortingRepository의 findAll메소드를 호출하면 페이징 처리가 된 객체를 전달받을 수 있다
        //PagingAndSortingRepository의 findALl메소드 내부에서 페이징처리를 자동으로 구현해준다
        //pageNo => 몇 번쨰 페이지인지
        //pageSize => 한 페이지에 출력할 도큐먼트의 개수
        //Sort객체
        Sort sort = Sort.by("property").ascending();//오름차순정렬
        Page<ScoreDTO> page = repository.findAll(PageRequest.of(pageNo,10,sort));
        //Page객체에서 한페이지에 출력할 document를 꺼내서 리턴하기
        return page.getContent();
    }
}

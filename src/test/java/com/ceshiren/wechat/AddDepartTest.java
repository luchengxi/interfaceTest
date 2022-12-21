package com.ceshiren.wechat;

import com.ceshiren.utils.FakerUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
public class AddDepartTest {
    /*
    * 添加部门
    * */

    @Test
    @DisplayName("企业微信添加部门")
    public void addDepartTest() throws IOException {
        String CreatDepartURL ="https://qyapi.weixin.qq.com/cgi-bin/department/create";
        //从json文件中获取企业微信的token
        HashMap<String,String> tokenValue=getTokenFromJsonFile();
        //读取并替换add.json的部分参数
        DocumentContext con = JsonPath.parse(new File("src/test/resources/depart/add.json"));
        con.set("$.name", FakerUtil.getDepartName());//替换部门名称
        con.set("$.order",FakerUtil.getNum(10,100));//替换order值
        String depart = con.jsonString();

        Response rep = given()
                .contentType("application/json;charset=UTF-8")
                .queryParams(tokenValue)
                .body(depart)
                .log().body()
                .log().headers()
                .when()
                .post(CreatDepartURL)
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        System.out.println(rep);
        //断言
        Integer errcode=rep.path("errcode");
        String errmsg=rep.path("errmsg");
        Integer id = rep.path("id");
        System.out.println("当前部门id为："+id);

        assertAll(
                //断言状态码和错误消息
                ()-> Assertions.assertEquals(0,errcode),
                ()->Assertions.assertEquals("created",errmsg)
        );


    }

    private static HashMap<String,String> getTokenFromJsonFile() throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
        TypeReference<HashMap<String,String>> typeReference = new TypeReference<>() {
        };
        HashMap<String, String> jsonMap = jsonMapper.readValue(new File("token.json"), typeReference);
        return jsonMap;
    }
}

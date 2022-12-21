package com.ceshiren.wechat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GetTokenTest {
    /*
    * 获取企业微信的token
    * 参数：corpid=ww56c28cf4b25c724a
    *      corpsecret=kAIeK0BhhCzQ71GO6lr5x5t_Q763EPLkzTyUyUZ2LPU
    * 获取access_token的值
    * */
    public static final Logger log = LoggerFactory.getLogger(GetTokenTest.class);
    @Test
    @DisplayName("获取企业微信的token,正向用例")
    public void getWechatToken() throws IOException {
        Response wechatResponse = given()
                .param("corpid", "ww56c28cf4b25c724a")
                .param("corpsecret", "kAIeK0BhhCzQ71GO6lr5x5t_Q763EPLkzTyUyUZ2LPU")
                .log().parameters()
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then()
                .log().all()
                .extract().response();
               // .extract().path("access_token");//使用gpath获取返回结果的access_token值

        String wechatToken=wechatResponse.path("access_token");//使用gpath获取返回结果的access_token值
        int errcode = wechatResponse.path("errcode");
        String errmsg = wechatResponse.path("errmsg");
        String tokenValue = 0==errcode?wechatToken:errmsg;
        System.out.println("企业微信token"+tokenValue);
        log.info("企业微信token"+tokenValue);

        //断言
        assertAll(
                //断言状态码
                ()-> Assertions.assertEquals(0,errcode),
                //断言msg消息
                ()->Assertions.assertEquals("ok",errmsg));

        //将token写到json文件中
        HashMap<String,String> hpToken = new HashMap<>();
        hpToken.put("access_token",tokenValue);
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.writeValue(Paths.get("token.json").toFile(),hpToken);

    }

    @Test
    @DisplayName("获取企业微信的token,传入错误的参数corpid")
    public void getWechatTokenErr(){
        Response wechatResponse = given()
                .param("corpid", "ww56c28cf4b25c724a1")
                .param("corpsecret", "kAIeK0BhhCzQ71GO6lr5x5t_Q763EPLkzTyUyUZ2LPU")
                .log().parameters()
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then()
                .log().all()
                .extract().response();
        // .extract().path("access_token");//使用gpath获取返回结果的access_token值

        String wechatToken=wechatResponse.path("access_token");//使用gpath获取返回结果的access_token值
        int errcode = wechatResponse.path("errcode");
        String errmsg = wechatResponse.path("errmsg");
        String tokenValue = 0==errcode?wechatToken:errmsg;
        System.out.println("企业微信token"+tokenValue);

        //断言
        assertAll(
                //断言状态码
                ()-> Assertions.assertEquals(40013,errcode),
                //断言msg消息,hamcrest第三方高级断言
                ()->assertThat(errmsg,is(containsString(errmsg))));
    }

}

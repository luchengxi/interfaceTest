package com.ceshiren.inter;


import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.HashMap;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.specification.ProxySpecification.host;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.path.json.JsonPath.from;

public class restAssured {
    /*
     * 1、get请求
     * 2、post请求
     * */
    @Test
    public void getTest() {
        //get请求
        given()
                .param("userName", "seven")//设置参数
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .log().all() //打印日志
                .body("args.userName", equalTo("seven"))//响应体断言
                .statusCode(200);//简单的断言,状态码响应断言

    }

    @Test
    public void postTest() {
        // 定义请求体数据：json序列化后的字符串
        String jsonParam = "{\"userName\":\"seven\"}";
        //post请求,发送json格式参数
        given()
                .contentType("application/json")//设置请求体参数类型
                .body(jsonParam)
                .log().headers()//打印请求头信息
                .when()
                .post("https://httpbin.ceshiren.com/post")//发送post请求
                .then()
                .statusCode(200)//状态码断言
                .log().all();


        //定义HashMap集合作为参数
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "hellen");
        //post请求,发送json格式参数
        given()
                .contentType("application/json")//设置请求体参数类型
                .body(hashMap)
                .log().headers()//打印请求头信息
                .when()
                .post("https://httpbin.ceshiren.com/post")//发送post请求
                .then()
                .log().all();


    }

    /*
     * form表单请求
     * */
    @Test
    public void formTest() {
        //单个表单数据提交formParam()
        given()
                .formParam("user", "zhangSan")
                .log().headers()//打印请求头信息
                .when()
                .post("https://httpbin.ceshiren.com/post")//发送post请求
                .then()
                .statusCode(200)//状态码断言
                .log().all();

        //多个表单数据提交formParams()
        given()
                .formParams("user", "WingWu", "pwd", "123")
                .log().headers()//打印请求头信息
                .when()
                .post("https://httpbin.ceshiren.com/post")//发送post请求
                .then()
                .statusCode(200)//状态码断言
                .log().all();
    }

    /*
     * 复杂断言
     * 1、rest-assured内置断言
     * 2、json-path断言
     * 3、json-schema-validator断言
     * */
    @Test
    public void test() {

        //get请求
        String str = given()
                .param("userName", "seven")//设置参数
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .log().all() //打印日志
                .extract().response().asString();
        //1、rest-assured内置断言
        String name = from(str).getString("args.userName");
        //断言
        Assertions.assertEquals("seven", name);


        //get请求
        String st = given()
                .param("userName", "zhangSan")//设置参数
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .log().all() //打印日志
                .extract().body().path("args.userName");//直接使用path获取body体的内容
        //断言
        Assertions.assertEquals("zhangSan", st);

    }

    @Test
    public void test1() {

        //get请求
        String str = given()
                .param("userName", "seven")//设置参数
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .log().all() //打印日志
                .extract().response().asString();
        //2、json-path断言
        String name = JsonPath.read(str, "$.args.userName");
        //断言
        Assertions.assertEquals("seven", name);

    }

    @Test
    public void jsonSchemaTest() {
        //3、json-schema-validator断言
        //get请求
        given()
                .param("userName", "seven")//设置参数
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("jsonSchema.json"));//JSON-schema断言


    }


    /*
     * 设置请求的cookie
     * */
    @Test
    public void testCookie() {
        //方式一、通过header()方法添加cookie
        given()
                .header("Cookie", "myCookie=asd123")
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .log().all() //打印日志
                .statusCode(200);//简单的断言,状态码响应断言
    }

    @Test
    public void testCookieOne() {
        //方式二、通过cookie()方法添加cookie
        given()
                .cookie("myCookieOne", "asd456")
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .log().all() //打印日志
                .statusCode(200);//简单的断言,状态码响应断言
    }

    /*
     * 文件上传
     * */
    @Test
    public void fileTest() {
        //单独上传文件
        File file = new File("src/test/resources/uplodFile.txt");
        given()
                .multiPart("upFile", file)
                .log().headers()//打印请求头信息
                .when()
                .post("https://httpbin.ceshiren.com/post")//发送post请求
                .then()
                .statusCode(200)//状态码断言
                .log().all();
    }

    @Test
    public void fileTestOne() {
        //上传文件+json数据
        File file = new File("src/test/resources/uplodFile.txt");
        given()
                .multiPart("upFile", file)
                .multiPart("one", "{\"user\":\"zhangSan\"}", "application/json")
                .log().headers()//打印请求头信息
                .when()
                .post("https://httpbin.ceshiren.com/post")//发送post请求
                .then()
                .statusCode(200)//状态码断言
                .log().all();
    }

    /*
    * 使用代理请求https,配置代理后,请求会被代理拦截(此处使用charles做代理),再由代理发送给服务器,服务器响应给代理,代理再把响应给客户端
    * */
    @Test
    public void testProxy(){
        //配置全局代理
        RestAssured.proxy = host("127.0.0.1").withPort(8888);
        given()
                .relaxedHTTPSValidation()//或略Https校验
                .param("userName", "seven")//设置参数
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .log().all() //打印日志
                .statusCode(200);//简单的断言,状态码响应断言

    }
    @Test
    public void testProxyOne(){
       //配置单个请求的代理
        given()
                .proxy(8888)//配置代理
                .relaxedHTTPSValidation()//或略Https校验
                .param("userName", "seven")//设置参数
                .when()
                .get("https://httpbin.ceshiren.com/get")//发起get请求
                .then()
                .log().all() //打印日志
                .statusCode(200);//简单的断言,状态码响应断言

    }

}

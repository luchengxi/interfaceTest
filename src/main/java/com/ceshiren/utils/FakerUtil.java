package com.ceshiren.utils;

import com.apifan.common.random.source.OtherSource;
import com.apifan.common.random.source.PersonInfoSource;
import com.apifan.common.random.util.PinyinUtils;


public class FakerUtil {
    //部门名字
    public static String getDepartName(){
        //随机生成1个公司部门名称
        String department = OtherSource.getInstance().randomCompanyDepartment();
        return department;
    }
    public static String getName(){
        ///生成1个随机中文人名(性别随机)
        String k = PersonInfoSource.getInstance().randomChineseName();
        return k;
    }
    public static String getPinYin(String pinyin){
        return PinyinUtils.toPinyin(pinyin, true);
    }

    public static String getPhone(){
        //生成1个随机中国大陆手机号
        String m = PersonInfoSource.getInstance().randomChineseMobile();
        //返回手机号
        return m;
    }

    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
    public static String getChinese(int num) {
        //生成4个随机汉字
        String j = OtherSource.getInstance().randomChinese(num);
        return j;
    }
}
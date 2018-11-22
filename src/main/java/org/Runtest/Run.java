package org.Runtest;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Run {
    public String test(String s) throws Exception {
//        String s = "{\"mac\":\"\",\"dockerId\":\"96a5183f307f\",\"date\":{\"path\":\"/home/super/temp/test.py\",\"alterPath\":\"/home/test.py\"}}";
        JSONObject json = JSONObject.parseObject(s);
        JSONObject object = json.getJSONObject("date");
        String dockerId = json.getString("dockerId");
        String path = object.getString("path");//主函数路径
        String alterPath = object.getString("alterPath");//修改过的路径

        Runtime.getRuntime().exec(new String[]{"/home/super/Runtest/start.sh",dockerId});
        Process pro = Runtime.getRuntime().exec(new String[]{"/home/super/Runtest/run.sh",dockerId,alterPath});
        pro.waitFor();

        InputStream is = pro.getInputStream();
        InputStream ise = pro.getErrorStream();
        BufferedReader br ;
        BufferedReader bre ;

        br = new BufferedReader(new InputStreamReader(is));
        bre = new BufferedReader(new InputStreamReader(ise));

        String result = "";

        String line = "";
        while ((line = br.readLine())!=null){
            result = result + line + "\n";
        }

        String te1 = "";
        while ((te1 = bre.readLine())!=null){
            result = result + te1 + "\n";
        }

        return result;
    }
}

package org.Runtest;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HintList {
//    String test = "{\"path\":\"/home/super/temp\"}";

    List<String> list = new ArrayList<String>();

    public void get(){
        String test = "{\"path\":\"/home/super/temp\"}";
        JSONObject json = JSONObject.parseObject(test);
        String path = json.getString("path");

        //获取所有py文件的module名
        re(path);

        //新建一个py文件用来获取所有的module中的函数方法
        String fileName = path+"/HintList.py";
        File f = new File(fileName);
        if(!f.exists()){
            try {
                f.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    public void re(String path){
        File file = new File(path);
        String[] fileList = file.list();

        for(int i=0; i<fileList.length; i++){
            if((new File(path+"/"+fileList[i])).isDirectory()){
                re(path+"/"+fileList[i]);
            }
            if((new File(path+"/"+fileList[i])).isFile()){
                list.add(fileList[i]);
            }
        }
    }

    public void writeFile(String filePath){
        FileOutputStream fos = null;
        String data = "";
        for(int i=0; i<list.size(); i++){
            data = "import"+list.get(i)+"/n" + "print(dir(" + list.get(i) + "))"+"/n";
        }
    }
}

package org.Tips;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

//wheelMethod
public class PackageList {

    List<String> list = new ArrayList<>();

    public void MethodList() throws IOException, InterruptedException {
        Data data = new Data();
        data.setData("{\"wheel\":\"json\"}");

        JSONObject json = JSONObject.parseObject(data.getData());
        String wheel = json.getString("wheel");
        String mac = data.getMac();

        readFile(mac,wheel);

        Map<String, List<String>> map = new HashMap<>();
        map.put(wheel,list);
        JSONObject json1 = JSONObject.parseObject(JSON.toJSONString(map));
//        return json1;

    }

    public void readFile(String mac, String wheel) throws IOException, InterruptedException {
//        String path = "/home/"+mac;
        //数据卷路径
        String path = "/home/super/temp";
        String pyPath = path+"/MyWheel.py";
        File pyFile = new File(pyPath);
        if(!pyFile.exists()){
            pyFile.createNewFile();
//            writeFile(pyPath,wheel);
        }else {
            pyFile.delete();
            pyFile.createNewFile();
//            writeFile(pyPath,wheel);
        }

        String shellPath = path+"/Myshell.sh";
        File shellFile = new File(shellPath);
        if(!shellFile.exists()){
            shellFile.createNewFile();
        }else {
            shellFile.delete();
            shellFile.createNewFile();
        }

        writeFile(wheel,pyPath,shellPath);

        runShell(pyPath,shellPath);

    }

    public void writeFile(String wheel,String pyPath,String shellPath) throws IOException {
        FileOutputStream fos = null;
        String data = "import "+wheel+"\n";
        String data1 = "print(dir(" + wheel + "))"+"\n";

        fos = new FileOutputStream(pyPath,true);
        fos.write(data.getBytes());
        fos.write(data1.getBytes());

        String data2 = "#! /bin/bash"+"\n";
        String data3 = "python $1";
        fos = new FileOutputStream(shellPath,true);
        fos.write(data2.getBytes());
        fos.write(data3.getBytes());

        fos.close();
    }

    public void runShell(String pyPath,String shellPath) throws IOException, InterruptedException {
        Process pro = Runtime.getRuntime().exec(new String[]{"sh",shellPath,pyPath});
        pro.waitFor();

        InputStream is = pro.getInputStream();
        BufferedReader br ;

        br = new BufferedReader(new InputStreamReader(is));

        String brLine = "";
        while ((brLine = br.readLine()) != null){
            System.out.println(brLine);
            Pattern p = Pattern.compile("\\[*\\]");
            if(p.matcher(brLine).find()){
                brLine = brLine.replaceAll("\\[|'__.*?__', | |'|\\]|","");
                String[] strs = brLine.split(",");
                for(String s : strs){
                    System.out.println(s);
                    list.add(s);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PackageList t = new PackageList();
        t.MethodList();
    }
}

package org.Tips;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class MethodList {
    //文件名list
    List<String> list = new ArrayList<String>();
    //返回前端结构
    Map<String, Map<String, List<String>>> returnMap = new HashMap<>();

    public void get() throws IOException, InterruptedException {
        String test = "{\"path\":[\"/home/super/temp/1/11/test1.py\",\"/home/super/temp/2/21/test2.py\",\"/home/super/temp/2/21/test1.py\"]}";
        JSONObject json = JSONObject.parseObject(test);
        JSONArray jsonArr = json.getJSONArray("path");

        //获取所有py文件的module名
        readFile(jsonArr);

        JSONObject json1 = JSONObject.parseObject(JSON.toJSONString(returnMap));
        System.out.println(json1.toJSONString());
//        return json1.toJSONString();

    }

    private void readFile(JSONArray array) throws IOException, InterruptedException {
        String path = "";
        String filePath = "";
        Set<String> set = new HashSet<>();

        for(int i=0; i<array.size(); i++){

            path = array.getString(i);
            File file = new File(path);
            //获取py文件父路径
            filePath = file.getParent();

            String fileName = file.getName();
            String qianzhui = fileName.substring(0, fileName.lastIndexOf("."));
            String houzhui = fileName.substring(fileName.lastIndexOf(".")+1);
            if(houzhui.equals("py") && !qianzhui.equals("HintList")) {
                list.add(qianzhui);
            }

            //写py文件
            String pyPath = filePath +  "/HintList.py";
            set.add(pyPath);

            File newFile = new File(pyPath);

            if(!newFile.exists()){
                newFile.createNewFile();
                writeFile(pyPath,qianzhui);
            }else {
                writeFile(pyPath,qianzhui);
            }
        }

        int i=0;
        for(String execPath : set){
            //用shell运行python
            runShell(execPath);
        }

    }


    private void writeFile(String filePath,String qianzhui) throws IOException {
        FileOutputStream fos = null;
        String data = "import "+qianzhui+"\n";
        String data1 = "str=\'name:%s\' %("+qianzhui+".__name__)"+"\n";
        String data2 = "print(dir(" + qianzhui + "),str)"+"\n";

        fos = new FileOutputStream(filePath,true);
        fos.write(data.getBytes());
        fos.write(data1.getBytes());
        fos.write(data2.getBytes());

        fos.close();
    }

    private String writeFile2(String filePath,String name,String method) throws IOException, InterruptedException {

        File f = new File(filePath);
        f.delete();

        FileOutputStream fos = null;
        String data = "import "+name+"\n";
        String data2 = "print(dir(" + name + "." + method + "))"+"\n";

        fos = new FileOutputStream(filePath,true);
        fos.write(data.getBytes());
        fos.write(data2.getBytes());

        fos.close();

        Process pro = Runtime.getRuntime().exec(new String[]{"/home/super/temp/HintList.sh",filePath});
        pro.waitFor();
        InputStream is = pro.getInputStream();
        BufferedReader br ;

        br = new BufferedReader(new InputStreamReader(is));

        String brLine = "";

        while ((brLine = br.readLine()) != null){
            Pattern p = Pattern.compile("func_.*");
            if (!p.matcher(brLine).find()){
                brLine = brLine.replaceAll("\\[|'__.*?__', | |'|\\]|.*_.*","");
                System.out.println(brLine);
                method = method + "." + brLine;
            }
        }
        return method;
    }

    private void runShell(String execPath) throws IOException, InterruptedException {

        Process pro = Runtime.getRuntime().exec(new String[]{"/home/super/temp/HintList.sh",execPath});
        pro.waitFor();
        InputStream is = pro.getInputStream();
        BufferedReader br ;

        br = new BufferedReader(new InputStreamReader(is));

        String brLine = "";

        Map<String, List<String>> methodMap = new HashMap<>();
        while ((brLine = br.readLine()) != null){

            Pattern p = Pattern.compile("\\(*\\)");
            List<String> methodList = new ArrayList<>();
            if(p.matcher(brLine).find()){
//                ([^>]+?)
                brLine = brLine.replaceAll("\\[|'__.*?__', | |'|\\]|\\(|\\)","");
                String[] strs = brLine.split(",");
                String name = (brLine.split(",")[strs.length-1]).split(":")[1];
                for(String s : strs){
                    Pattern p1 = Pattern.compile("name:");
                    if(!p1.matcher(s).find()){
                        String me = writeFile2(execPath,name,s);
                        if (s.equals(me)) {
                            methodList.add(me);
                        }else {
                            methodList.add(s);
                            methodList.add(me);
                        }
                    }else {
                        s = s.replaceAll("name:","");
                        methodMap.put(s,methodList);
                    }


                }
            }
        }

        File f = new File(execPath);
        String filePath = f.getParent();

        returnMap.put(filePath,methodMap);

        f.delete();

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MethodList t = new MethodList();
        t.get();
    }

}

package org.Tips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OriginList {
    public static void main(String[] args) {
        Map<String , List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();

        String s1 = "abs() dict() help() min() setattr() " +
                "all() dir() hex() next() slice() " +
                "any() divmod() id() object() sorted() " +
                "ascii() enumerate() input() oct() staticmethod() " +
                "bin() eval() int() open() str() " +
                "bool() exec() isinstance() ord() sum() " +
                "bytearray() filter() issubclass() pow() super() " +
                "bytes() float() iter() print() tuple() " +
                "callable() format() len() property() type() " +
                "chr() frozenset() list() range() vars() " +
                "classmethod() getattr() locals() repr() zip() " +
                "compile() globals() map() reversed() __import__() " +
                "complex() hasattr() max() round() " +
                "delattr() hash() memoryview() set()";
        String[] ss1 = s1.split(" ");
        for(String s : ss1){
            list.add(s);
        }

        String s2 = "async ,assert ,and ,bool ,bytearray ,bytes ,classmethod ,complex ,class ,def ,del ,dict ,ellipsis ,enumerate ,exec ,float ,for ,from ,frozenset ,function ,import ,if ,in ,int ,is ,list ,lambda ,not ,object ,or ,pass ,property ,raise ,range ,set ,slice ,staticmethod ,super ,try: ,tuple ,type ,while ,with ";
        String[] ss2 = s2.split(",");
        for(String s : ss2){
            list.add(s);
        }

        for(String s : list){
            System.out.println(s);
        }

        map.put("Built-in",list);
    }
}


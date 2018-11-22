package org.Runtest;

import java.io.IOException;

public class RunThread extends Thread{

    public void run(String dockerId){
        try {
            Runtime.getRuntime().exec(new String[]{"/home/super/Runtest/stop.sh",dockerId});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

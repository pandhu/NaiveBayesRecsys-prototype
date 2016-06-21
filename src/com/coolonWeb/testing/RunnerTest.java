package com.coolonWeb.testing;

import com.coolonWeb.Main;

/**
 * Created by root on 08/06/16.
 */
public class RunnerTest implements Runnable {
    public String method;
    public RunnerTest(String method){
        this.method = method;
    }
    @Override
    public void run() {
        if(method.equals("memory"))
            Main.memoryBasedTest();
        else if(method.equals("model"))
            Main.modelBasedTest();
    }
}

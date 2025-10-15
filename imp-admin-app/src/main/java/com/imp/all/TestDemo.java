package com.imp.all;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Daniel
 * @Date 2025/9/11/周四 19:00
 * @Description TODO
 **/
public class TestDemo {


    public static void main(String[] args) {

        List<WorkerDemo> maxList = new ArrayList<>();
        maxList.add(new WorkerDemo(0, 5000, 0.0F));
        maxList.add(new WorkerDemo(5000, 8000, 0.03F));
        maxList.add(new WorkerDemo(8000, 17000,0.1F));
        maxList.add(new WorkerDemo(17000, 30000, 0.2F));
        maxList.add(new WorkerDemo(30000, 40000, 0.25F));
        maxList.add(new WorkerDemo(40000, 60000, 0.3F));
        maxList.add(new WorkerDemo(60000, 85000, 0.35F));
        maxList.add(new WorkerDemo(85000, Integer.MAX_VALUE, 0.45F));


        Integer current = 20000;

        Float reduce = 0.0F;
        for (int i = 0; i < maxList.size(); i++) {
            WorkerDemo workerDemo = maxList.get(i);
            if (current > workerDemo.getMinCount()) {
                reduce = reduce + workerDemo.getReduceCount(current);
            }
            else {
                break;
            }
        }

        System.out.println(reduce);
    }

}

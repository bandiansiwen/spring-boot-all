package com.imp.all.transport;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@CrossOrigin
public class Test {

    @RequestMapping("/add")
    public ArrayList<String> a(String type, String num, String weight, String a, String b, String c,
                               boolean priority, int time, int loadNum,
                               int loadTime, String transTime, int day) throws Exception {
        if (type.equals("吨") || type.equals("箱")) {
            if (num.equals("one")) {
                return Transport.SingleDistance(Integer.parseInt(weight), Integer.parseInt(a), Integer.parseInt(b), Integer.parseInt(c), priority, time, loadNum, loadTime, Integer.parseInt(transTime), day);
            } else {
                int[] weights = Arrays.stream(weight.split(",")).mapToInt(Integer::parseInt).toArray();
                int[] transTimes = Arrays.stream(transTime.split(",")).mapToInt(Integer::parseInt).toArray();
                return Transport.DoubleDistance(weights, Integer.parseInt(a), Integer.parseInt(b), Integer.parseInt(c), priority, time, loadNum, loadTime, transTimes, day);
            }
        } else if (type.equals("车")) {
            if (num.equals("one")) {
                return Transport.trainNumberSingle(Integer.parseInt(a), Integer.parseInt(b), Integer.parseInt(c), time, loadNum, loadTime, Integer.parseInt(transTime), day);
            } else {
                int[] as = Arrays.stream(a.split(",")).mapToInt(Integer::parseInt).toArray();
                int[] bs = Arrays.stream(b.split(",")).mapToInt(Integer::parseInt).toArray();
                int[] cs = Arrays.stream(c.split(",")).mapToInt(Integer::parseInt).toArray();
                int[] transTimes = Arrays.stream(transTime.split(",")).mapToInt(Integer::parseInt).toArray();
                return Transport.trainNumberDouble(as, bs, cs, time, loadNum, loadTime, transTimes, day);
            }
        } else {
            return new ArrayList<>();
        }

    }


}

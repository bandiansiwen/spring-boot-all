package com.imp.all.demos.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Longlin
 * @date 2022/5/13 19:56
 * @description
 */
public class SortMain {
    public static void main(String[] args) {
        List<Integer> cArray = new ArrayList<>();
//        List<Integer> aArray = Arrays.asList(1, 3, 5, 7, 9);
//        List<Integer> bArray = Arrays.asList(2, 4, 6, 8, 10, 12, 14);
        Integer[] aArray = {1, 3, 5, 7, 9};
        Integer[] bArray = {2, 4, 6, 8, 10, 12, 14};

        int i=0;
        int j=0;
        int temp = 0;
        while (i < aArray.length && j<bArray.length) {
            Integer a = aArray[i];
            Integer b = bArray[j];
            if (a<b) {
                temp = a;
                i=i+1;  // a小  则 取 a数据的下一个
            }
            else {
                temp = b;
                j = j+1; // b小  则 取 b数据的下一个
            }
            cArray.add(temp);
        }
        if (i < aArray.length) {
            for (int k = i; k < aArray.length; k++) {
                cArray.add(aArray[k]);
            }
        }
        if (j < bArray.length) {
            for (int k = j; k < bArray.length; k++) {
                cArray.add(bArray[k]);
            }
        }
//        cArray.addAll(aArray);
//        cArray.addAll(bArray);
//
//        Collections.sort(cArray);
//
//        cArray = cArray.stream().sorted(Integer::compareTo).collect(Collectors.toList());
        System.out.println(cArray);
    }
}

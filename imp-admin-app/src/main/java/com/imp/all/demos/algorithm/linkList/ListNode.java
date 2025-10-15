package com.imp.all.demos.algorithm.linkList;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Longlin
 * @date 2023/4/12 14:13
 * @description
 */
@AllArgsConstructor
public class ListNode {

    public Integer val;
    public ListNode next;

    /**
     * 打印
     */
    public String valToString() {
        List<Integer> values = new ArrayList<>();
        ListNode node = this;
        values.add(node.val);
        while((node = node.next) != null){
            values.add(node.val);
        }
        return values.toString();
    }
}

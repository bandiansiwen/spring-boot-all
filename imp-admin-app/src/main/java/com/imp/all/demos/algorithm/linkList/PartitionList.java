package com.imp.all.demos.algorithm.linkList;

/**
 * @author Longlin
 * @date 2023/4/14 9:09
 * @description
 */
public class PartitionList {

    /**
     * 力扣第 86 题「 分隔链表」
     * <a href="https://leetcode.cn/problems/partition-list/">...</a>
     */
    public static ListNode partition(ListNode node, Integer x) {
        // 定义两个虚拟链表分发数据
        ListNode lessList = new ListNode(-1, null);
        ListNode moreList = new ListNode(-1, null);
        ListNode lessP = lessList;
        ListNode moreP = moreList;
        // 定义p遍历链表
        ListNode p = node;
        while (p != null) {
            if (p.val < x) {
                lessP.next = p;
                lessP = lessP.next;
            }
            else {
                moreP.next = p;
                moreP = moreP.next;
            }
            // 断开原链表中的每个节点的 next 指针
            ListNode temp = p.next;
            p.next = null;
            p = temp;
        }
        // 连接两个链表
        lessP.next = moreList.next;

        return lessList.next;
    }

    public static void main(String[] args) {
        ListNode end = new ListNode(2, null);
        ListNode e = new ListNode(5, end);
        ListNode d = new ListNode(2, e);
        ListNode c = new ListNode(3, d);
        ListNode b = new ListNode(4, c);
        ListNode a = new ListNode(1, b);

        ListNode partition = partition(a, 3);
        System.out.println(partition.valToString());
    }
}

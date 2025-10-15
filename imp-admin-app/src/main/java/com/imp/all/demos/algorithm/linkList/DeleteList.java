package com.imp.all.demos.algorithm.linkList;

import java.util.Objects;

/**
 * @author Longlin
 * @date 2023/4/18 14:13
 * @description
 */
public class DeleteList {

    /**
     * 力扣第 83 题「 删除排序链表中的重复元素」
     * <a href="https://leetcode.cn/problems/remove-duplicates-from-sorted-list/">...</a>
     * 给定一个已排序的链表的头 head ， 删除所有重复的元素，使每个元素只出现一次 。返回 已排序的链表 。
     */
    public static ListNode deleteDuplicates(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode slow = head, fast = head;
        while (fast != null) {
            if (!Objects.equals(fast.val, slow.val)) {
                slow.next = fast;
                slow = slow.next;
            }
            fast = fast.next;
        }
        // 断开与后面重复元素的连接
        slow.next = null;
        return head;
    }

    public static ListNode deleteDuplicates2 (ListNode head) {
        //空链表
        if(head == null) {
            return null;
        }
        //遍历指针
        ListNode cur = head;
        //指针当前和下一位不为空
        while(cur != null && cur.next != null){
            //如果当前与下一位相等则忽略下一位
            if(cur.val.equals(cur.next.val)) {
                cur.next = cur.next.next;
            }
            //否则指针正常遍历
            else {
                cur = cur.next;
            }
        }
        return head;
    }

    /**
     *  删除链表的倒数第n个节点
     */
    public static ListNode removeNthFromEnd (ListNode head, int n) {
        // write code here
        ListNode preSlow=head, slow=head, fast = head;
        for(int i=0;i<n;i++) {
            if(fast != null) {
                fast = fast.next;
            }
            else{
                return null;
            }
        }
        boolean flag = false;
        while(fast!=null) {
            fast = fast.next;
            slow = slow.next;
            if(flag) {
                preSlow = preSlow.next;
            }
            else{
                flag = true;
            }
        }
        if(!flag) {
            return slow.next;
        }
        else{
            preSlow.next = slow.next;
            return head;
        }
    }

    public static void main(String[] args) {
        ListNode end = new ListNode(3, null);
        ListNode e = new ListNode(3, end);
        ListNode d = new ListNode(2, e);
        ListNode c = new ListNode(2, d);
        ListNode b = new ListNode(2, c);
        ListNode a = new ListNode(1, b);

        ListNode listNode = deleteDuplicates(a);
        System.out.println(listNode.valToString());

        ListNode x = new ListNode(2, null);
        ListNode y = new ListNode(1, x);
        ListNode node = removeNthFromEnd(y, 2);
        assert node != null;
        System.out.println(node.valToString());
    }
}

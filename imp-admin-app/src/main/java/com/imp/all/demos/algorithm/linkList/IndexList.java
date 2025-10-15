package com.imp.all.demos.algorithm.linkList;

/**
 * @author Longlin
 * @date 2023/4/18 10:03
 * @description
 */
public class IndexList {

    /**
     * 力扣第 19 题「 删除链表的倒数第 N 个结点」
     * <a href="https://leetcode.cn/problems/remove-nth-node-from-end-of-list/">...</a>
     */
    // 主函数
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        // 虚拟头结点
        ListNode dummy = new ListNode(-1, null);
        dummy.next = head;
        // 删除倒数第 n 个，要先找倒数第 n + 1 个节点
        ListNode x = findFromEnd(dummy, n + 1);
        if (x!= null) {
            // 删掉倒数第 n 个节点
            x.next = x.next.next;
        }
        return dummy.next;
    }

    /**
     * 单链表的倒数第 k 个节点
     */
    public static ListNode findFromEnd(ListNode head, int k) {

        ListNode p1 = head;
        // p1 先走 k 步
        for (int i = 0; i < k; i++) {
            if (p1 == null) {
                return null;
            }
            p1 = p1.next;
        }
        ListNode p2 = head;
        // p1 和 p2 同时走 n-k 步
        while (p1 != null) {
            p2 = p2.next;
            p1 = p1.next;
        }
        return p2;
    }

    /**
     * 单链表的中点
     */
    public static ListNode middleNode(ListNode head) {
        // 快慢指针初始化指向 head
        ListNode slow = head, fast = head;
        // 快指针走到末尾时停止
        while (fast != null && fast.next != null) {
            // 慢指针走一步，快指针走两步
            slow = slow.next;
            fast = fast.next.next;
        }
        // 慢指针指向中点
        return slow;
    }




    public static void main(String[] args) {
        ListNode end = new ListNode(5, null);
        ListNode d = new ListNode(4, end);
        ListNode c = new ListNode(3, d);
        ListNode b = new ListNode(2, c);
        ListNode a = new ListNode(1, b);

        ListNode node = findFromEnd(a, 2);
        if (node != null) {
            System.out.println(node.valToString());
        }

        ListNode n = new ListNode(0, a);
        ListNode listNode = middleNode(n);
        System.out.println(listNode.valToString());
    }
}

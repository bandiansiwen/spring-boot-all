package com.imp.all.demos.algorithm.linkList;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author Longlin
 * @date 2023/4/13 10:02
 * @description
 */
public class MergeList {

    /**
     * 力扣第 21 题「 合并两个有序链表」
     * <a href="https://leetcode.cn/problems/merge-two-sorted-lists/">...</a>
     */
    public static ListNode mergeTwoList(ListNode node1, ListNode node2) {

        // 虚拟头结点
        ListNode dummy = new ListNode(-1, null);
        // 标记头结点
        ListNode p = dummy;
        ListNode p1 = node1, p2 = node2;
        while (p1!=null && p2!=null) {
            if (p1.val > p2.val) {
                p.next = p2;
                p2 = p2.next;
            }
            else {
                p.next = p1;
                p1 = p1.next;
            }
            p = p.next;
        }

        if (p1 != null) {
            p.next = p1;
        }
        if (p2 != null) {
            p.next = p2;
        }
        // 返回真正的头结点
        return dummy.next;
    }

    /**
     * 力扣第 23 题「 合并K个升序链表」
     * <a href="https://leetcode.cn/problems/merge-k-sorted-lists/">...</a>
     */
    public static ListNode mergeKLists(ListNode[] listNodes) {
        if (listNodes.length == 0) {
            return null;
        }
        // 虚拟头结点
        ListNode dummy = new ListNode(-1, null);
        ListNode p = dummy;
        // 优先级队列，最小堆
        PriorityQueue<ListNode> pq = new PriorityQueue<>(listNodes.length, Comparator.comparingInt(a -> a.val));
        // 将 k 个链表的头结点加入最小堆
        for (ListNode head : listNodes) {
            if (head != null) {
                pq.add(head);
            }
        }

        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            p.next = node;
            if (node.next != null) {
                pq.add(node.next);
            }
            // 指针不断前进
            p = p.next;
        }
        return dummy.next;
    }

    /**
     * 单链表的排序
     */
    public static ListNode sortInList (ListNode head) {
        // write code here
        if (head == null || head.next == null) {
            return head;
        }
        ListNode fast = head.next, slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode tmp = slow.next;
        slow.next = null;

        ListNode left = sortInList(head);
        ListNode right = sortInList(tmp);

        ListNode h = new ListNode(0, null);
        ListNode res = h;

        while (left != null && right != null) {
            if (left.val < right.val) {
                h.next = left;
                left = left.next;
            } else {
                h.next = right;
                right = right.next;
            }
            h = h.next;
        }
        h.next = left != null ? left : right;
        return res.next;
    }

    public static void main(String[] args) {
        {
            ListNode end = new ListNode(5, null);
            ListNode c = new ListNode(3, end);
            ListNode a = new ListNode(1, c);

            ListNode d = new ListNode(4, null);
            ListNode b = new ListNode(2, d);

//        ListNode listNode = mergeTwoList(a, b);
//        System.out.println(listNode.valToString());

            ListNode f = new ListNode(6, null);
            ListNode e = new ListNode(3, f);

            ListNode[] list = new ListNode[3];
            list[0] = a;
            list[1] = b;
            list[2] = e;
            ListNode x = mergeKLists(list);
            if (x != null) {
                System.out.println(x.valToString());
            }
        }
        {
            ListNode e = new ListNode(5, null);
            ListNode d = new ListNode(4, e);
            ListNode c = new ListNode(2, d);
            ListNode b = new ListNode(3, c);
            ListNode a = new ListNode(1, b);
            ListNode listNode = sortInList(a);
            System.out.println(listNode.valToString());
        }
    }
}

package com.imp.all.demos.algorithm.linkList;

/**
 * @author Longlin
 * @date 2023/4/18 14:09
 * @description
 */
public class CycleList {

    /**
     * 判断链表是否包含环
     */
    public static boolean hasCycle(ListNode head) {
        // 快慢指针初始化指向 head
        ListNode slow = head, fast = head;
        // 快指针走到末尾时停止
        while (fast != null && fast.next != null) {
            // 慢指针走一步，快指针走两步
            slow = slow.next;
            fast = fast.next.next;
            // 快慢指针相遇，说明含有环
            if (slow == fast) {
                return true;
            }
        }
        // 不包含环
        return false;
    }

    /**
     * 寻找环起点
     */
    public static ListNode detectCycle(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) break;
        }
        // 上面的代码类似 hasCycle 函数
        if (fast == null || fast.next == null) {
            // fast 遇到空指针说明没有环
            return null;
        }
        // 重新指向头结点
        slow = head;
        // 快慢指针同步前进，相交点就是环起点
        while (slow != fast) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * 力扣第 160 题「 相交链表」
     * <a href="https://leetcode.cn/problems/intersection-of-two-linked-lists/">...</a>
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // p1 指向 A 链表头结点，p2 指向 B 链表头结点
        ListNode p1 = headA, p2 = headB;
        while (p1 != p2) {
            // p1 走一步，如果走到 A 链表末尾，转到 B 链表
            if (p1 == null) p1 = headB;
            else            p1 = p1.next;
            // p2 走一步，如果走到 B 链表末尾，转到 A 链表
            if (p2 == null) p2 = headA;
            else            p2 = p2.next;
        }
        return p1;
    }

    public static void main(String[] args) {
        ListNode end = new ListNode(5, null);
        ListNode d = new ListNode(4, end);
        ListNode c = new ListNode(3, d);
        ListNode b = new ListNode(2, c);
        ListNode a = new ListNode(1, b);

        ListNode n = new ListNode(0, a);
        System.out.println(hasCycle(n));
//        end.next = b;
//        System.out.println(hasCycle(n));

        ListNode detectCycle = detectCycle(n);
        if (detectCycle != null) {
            System.out.println(detectCycle.val);
        }

        ListNode k = new ListNode(8, c);
        ListNode intersectionNode = getIntersectionNode(n, k);
        System.out.println(intersectionNode.val);
    }
}

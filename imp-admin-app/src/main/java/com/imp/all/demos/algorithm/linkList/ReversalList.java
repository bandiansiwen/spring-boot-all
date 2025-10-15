package com.imp.all.demos.algorithm.linkList;

/**
 * @author Longlin
 * @date 2023/4/12 14:02
 * @description
 */
public class ReversalList {

    /**
     * 将给出的链表中的节点每k个一组翻转，返回翻转后的链表
     */
    public static ListNode reverseGroup(ListNode head, Integer k) {

        // 定义一个尾结点，确定翻转的位置
        ListNode tail = head;
        // 判断尾部是否还有 k 个，没有则返回头结点
        for (int i = 0; i < k; i++) {
            if (tail == null) {
                return head;
            }
            tail = tail.next;
        }

        // 定义前驱节点和单点节点
        ListNode pre = null;
        ListNode cur = head;
        // 当前节点到达尾结点为止
        while (cur != tail) {
            ListNode temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        // 进行下一组翻转
        head.next = reverseGroup(tail, k);

        return pre;
    }

    /**
     * 翻转链表
     */
    public static ListNode reverseLink(ListNode head) {
        ListNode cur = head;
        ListNode pre = null;
        while (cur != null) {
            ListNode temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        return pre;
    }

    public static ListNode reverseBetween (ListNode head, int m, int n) {
        // write code here
        if (m>=n) {
            return head;
        }
        ListNode pre = null;
        ListNode cur = head;

        ListNode nNode = null;
        ListNode mNode = null;
        ListNode n_1Node = null;
        ListNode m_1Node = null;
        int i = 1;
        while(cur != null) {
            if (i<m) {
                pre = cur;
                cur = cur.next;
            }
            else if(i>=m && i<=n) {
                if (i==m) {
                    mNode = cur;
                    m_1Node = pre;
                }
                if(i==n) {
                    nNode = cur;
                    n_1Node = cur.next;
                }
                ListNode temp;
                temp = cur.next;
                if (i==m) {
                    cur.next = null;
                }
                else{
                    cur.next = pre;
                }
                pre = cur;
                cur = temp;
            }
            else {
                break;
            }
            i++;
        }
        mNode.next = n_1Node;
        if (m_1Node!=null) {
            m_1Node.next = nNode;
            return head;
        }
        return nNode;
    }

    public static ListNode addInList (ListNode head1, ListNode head2) {
        // write code here
        ListNode x = reverseLink(head1);
        ListNode y = reverseLink(head2);
        ListNode dummp = null;
        int n=0;
        while (x!=null || y!=null) {
            int sum = n;
            if(x!=null) {
                sum = sum+x.val;
                x = x.next;
            }
            if(y!=null) {
                sum = sum+y.val;
                y=y.next;
            }
            n=sum/10;
            ListNode temp = new ListNode(sum%10, null);
            if (dummp == null) {
                dummp = temp;
            }
            else{
                temp.next = dummp;
                dummp = temp;
            }
        }
        if(n>0) {
            ListNode temp = new ListNode(n, null);
            temp.next = dummp;
            dummp = temp;
        }
        return dummp;
    }

    public static void main(String[] args) {
        ListNode end = new ListNode(5, null);
        ListNode d = new ListNode(4, end);
        ListNode c = new ListNode(3, d);
        ListNode b = new ListNode(2, c);
        ListNode a = new ListNode(1, b);

//        ListNode listNode = reverseBetween(a, 1, 5);

//        System.out.println(listNode.valToString());
//
        ListNode rA = reverseGroup(a, 2);
        System.out.println(rA.valToString());

        ListNode j = new ListNode(7, null);
        ListNode i = new ListNode(3, j);
        ListNode h = new ListNode(9, i);

        ListNode n = new ListNode(3, null);
        ListNode m = new ListNode(6, n);

        ListNode listNode = addInList(h, m);
        System.out.println(listNode.valToString());
    }
}

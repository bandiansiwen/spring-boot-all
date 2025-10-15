package com.imp.all.demos.algorithm.tree.binary;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Longlin
 * @date 2023/4/12 23:27
 * @description
 */
@Slf4j
public class ErgodicBinaryTree {

    /**
     * 前序遍历: 根左右
     * 递归方式：递归
     */
    public static List<Integer> preOrder(TreeNode root) {
        List<Integer> values = new ArrayList<>();
         preOrder(root, values);
         return values;
    }
    public static void preOrder(TreeNode root, List<Integer> values) {
        if (root != null) {
            values.add(root.val);
            preOrder(root.left, values);
            preOrder(root.right, values);
        }
    }
    /**
     * 中序遍历: 根左右
     * 递归方式：循环
     */
    public static List<Integer> preOrderTraversalWithLoop(TreeNode root) {
        List<Integer> res = new ArrayList<>();

        Deque<TreeNode> stack = new LinkedList<>();
        while (root!=null || !stack.isEmpty()) {
            while (root!=null) {
                res.add(root.val);
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            root = root.right;
        }

        return res;
    }

    /**
     * 中序遍历: 左根右
     * 递归方式：递归
     */
    public static List<Integer> middleOrder(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        middleOrder(root, values);
        return values;
    }
    public static void middleOrder(TreeNode root, List<Integer> values) {
        if (root != null) {
            middleOrder(root.left, values);
            values.add(root.val);
            middleOrder(root.right, values);
        }
    }
    /**
     * 中序遍历: 左根右
     * 递归方式：循环
     */
    public static List<Integer> middleOrderTraversalWithLoop(TreeNode root) {
        List<Integer> res = new ArrayList<>();

        Deque<TreeNode> stack = new LinkedList<>();
        while (root!=null || !stack.isEmpty()) {
            while (root!=null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            res.add(root.val);
            root = root.right;
        }

        return res;
    }

    /**
     * 后序遍历: 左右根
     * 递归方式：递归
     */
    public static List<Integer> postOrder(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        postOrder(root, values);
        return values;
    }
    public static void postOrder(TreeNode root, List<Integer> values) {
        if (root != null) {
            postOrder(root.left, values);
            postOrder(root.right, values);
            values.add(root.val);
        }
    }
    /**
     * 后序遍历: 左右根
     * 递归方式：循环
     */
    public static List<Integer> postOrderTraversalWithLoop(TreeNode root) {
        List<Integer> res = new ArrayList<>();

        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode preAccess = null;
        while (root!=null || !stack.isEmpty()) {
            while (root!=null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (root.right==null || root.right==preAccess) {
                res.add(root.val);
                preAccess = root;
                root = null;
            }
            else {
                stack.push(root);
                root = root.right;
            }
        }

        return res;
    }

    /**
     * 层序遍历
     * 输入一棵二叉树的根节点，层序遍历这棵二叉树
     * 层序遍历的步骤如下：
     * 1.如果树为空，结束遍历；否则，将根节点入队。
     * 2.当队列不为空时，取出队头元素：
     *      访问该节点；
     *      如果该节点有左子节点，则将其左子节点入队；
     *      如果该节点有右子节点，则将其右子节点入队。
     * 3.重复步骤2直到队列为空，遍历完成。
     */
    public static void levelTraverse(TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        // 从上到下遍历二叉树的每一层
        while (!q.isEmpty()) {
            int sz = q.size();
            // 从左到右遍历每一层的每个节点
            for (int i = 0; i < sz; i++) {
                TreeNode cur = q.poll();
                log.info("值: {}", cur.val);
                // 将下一层节点放入队列
                if (cur.left != null) {
                    q.offer(cur.left);
                }
                if (cur.right != null) {
                    q.offer(cur.right);
                }
            }
        }
    }

    /**
     * 层序遍历结果
     */
    public static List<List<Integer>> res = new ArrayList<>();
    public static List<List<Integer>> levelTraverse2(TreeNode root) {
        if (root == null) {
            return res;
        }
        // root 视为第 0 层
        traverse(root, 0);
        return res;
    }

    public static void traverse(TreeNode root, int depth) {
        if (root == null) {
            return;
        }
        // 前序位置，看看是否已经存储 depth 层的节点了
        if (res.size() <= depth) {
            // 第一次进入 depth 层
            res.add(new LinkedList<>());
        }
        // 前序位置，在 depth 层添加 root 节点的值
        res.get(depth).add(root.val);
        traverse(root.left, depth + 1);
        traverse(root.right, depth + 1);
    }

    /**
     * 广度遍历
     */
    public static void wide(TreeNode root) {
        ArrayList<Integer> lists=new ArrayList<>();
        if (root == null) {
            System.out.println(lists);
        }
        Queue<TreeNode> queue=new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
            lists.add(node.val);
        }
        System.out.println(lists);
    }

    public static void main(String[] args) {
        TreeNode node7 = new TreeNode(7, null, null);
        TreeNode node6 = new TreeNode(6, null, null);
        TreeNode node5 = new TreeNode(5, node6, node7);
        TreeNode node4 = new TreeNode(4, null, null);
        TreeNode node3 = new TreeNode(3, null, null);
        TreeNode node2 = new TreeNode(2, node4, node5);
        TreeNode node1 = new TreeNode(1, node2, node3);

//        List<Integer> preVals = preOrder(node1);
//        System.out.println(preVals);
//        List<Integer> preVals2 = preOrderTraversalWithLoop(node1);
//        System.out.println(preVals2);
//
//        List<Integer> midVals = middleOrder(node1);
//        System.out.println(midVals);
//        List<Integer> midVals2 = middleOrderTraversalWithLoop(node1);
//        System.out.println(midVals2);
//
//        List<Integer> postVals = postOrder(node1);
//        System.out.println(postVals);
//        List<Integer> postVals2 = postOrderTraversalWithLoop(node1);
//        System.out.println(postVals2);
//
//        levelTraverse(node1);
//        List<List<Integer>> lists = levelTraverse2(node1);
//        System.out.println(lists);

        wide(node1);
    }
}

package com.imp.all.demos.algorithm.tree.binary;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Longlin
 * @date 2023/4/20 9:54
 * @description
 */
@Slf4j
public class BinaryTree {

    /**
     * 力扣第 104 题「 二叉树的最大深度」
     * <a href="https://leetcode.cn/problems/maximum-depth-of-binary-tree/">...</a>
     * 分解问题计算出答案
     *
     * 方法一：递归（DFS 深度优先搜索）
     */
    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        // 利用定义，计算左右子树的最大深度
        int leftMax = maxDepth(root.left);
        int rightMax = maxDepth(root.right);

        // 整棵树的最大深度等于左右子树的最大深度取最大值，
        // 然后再加上根节点自己
        return Math.max(leftMax, rightMax) + 1;
    }

    /**
     * 方法二：迭代（BFS 层序遍历）
     * 思路：通过队列逐层遍历二叉树，每处理完一层，深度加 1。最终遍历完所有层时，深度即为最大深度。
     */
    public static int maxDepth1(TreeNode root) {
        if (root == null) return 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            depth++;
        }
        return depth;
    }

    /**
     * 如果把根节点看做第 1 层，如何打印出每一个节点所在的层数
     * 一个节点在第几层，你从根节点遍历过来的过程就能顺带记录，用递归函数的参数就能传递下去
     */
    public static void traverseTree(TreeNode root, int level) {
        if (root == null) {
            return;
        }
        // 前序位置
        System.out.println("节点" + root.val + " 在第 " + level + " 层");
        traverseTree(root.left, level + 1);
        traverseTree(root.right, level + 1);
    }

    /**
     * 输入一棵二叉树，返回这棵二叉树的节点总数
     * 一个节点为根的整棵子树有多少个节点，你需要遍历完子树之后才能数清楚，然后通过递归函数的返回值拿到答案
     */
    public static int count(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftCount = count(root.left);
        int rightCount = count(root.right);

        // 后序位置
        log.info("节点 {} 的左子树有 {} 个节点，右子树有 {} 个节点",
                root.val, leftCount, rightCount);

        return leftCount + rightCount + 1;
    }

    /**
     * 力扣第 543 题「 二叉树的直径」
     * <a href="https://leetcode.cn/problems/diameter-of-binary-tree/">...</a>
     */
    // 记录最大直径的长度
    public static int maxDiameter = 0;
    public static int diameterOfBinaryTree(TreeNode root) {
//        traverseTreeSolution(root);
        maxDepthSolution(root);
        return maxDiameter;
    }

    //traverseTreeSolution 遍历每个节点的时候还会调用递归函数 maxDepth，而 maxDepth 是要遍历子树的所有节点的，所以最坏时间复杂度是 O(N^2)。
    public static void traverseTreeSolution(TreeNode root) {
        if (root == null) {
            return;
        }
        int leftMax = maxDepth(root.left);
        int rightMax = maxDepth(root.right);
        // 每一条二叉树的「直径」长度，就是一个节点的左右子树的最大深度之和。
        int myDiameter = leftMax + rightMax + 1;
        // 更新全局最大直径
        maxDiameter = Math.max(maxDiameter, myDiameter);
        traverseTreeSolution(root.left);
        traverseTreeSolution(root.right);
    }

    // 时间复杂度只有 maxDepthSolution 函数的 O(N)
    public static int maxDepthSolution(TreeNode root) {
        if (root == null)   return 0;

        int leftMax = maxDepthSolution(root.left);
        int rightMax = maxDepthSolution(root.right);

        // 后序位置，顺便计算最大直径
        int myDiameter = leftMax + rightMax + 1;
        maxDiameter = Math.max(maxDiameter, myDiameter);
        return 1 + Math.max(leftMax, rightMax);
    }

    public static void main(String[] args) {

        TreeNode node7 = new TreeNode(7, null, null);
        TreeNode node6 = new TreeNode(6, null, null);
        TreeNode node5 = new TreeNode(5, node6, node7);
        TreeNode node4 = new TreeNode(4, null, null);
        TreeNode node3 = new TreeNode(3, null, null);
        TreeNode node2 = new TreeNode(2, node4, node5);
        TreeNode node1 = new TreeNode(1, node2, node3);

        int i = maxDepth(node1);
        System.out.println(i);

        int i1 = maxDepth1(node1);
        System.out.println(i1);

        traverseTree(node1, 1);

        int count = count(node1);
        System.out.println(count);

        TreeNode n6 = new TreeNode(5, null, null);
        TreeNode n5 = new TreeNode(4, null, null);
        TreeNode n4 = new TreeNode(3, null, null);
        TreeNode n3 = new TreeNode(2, n5, n6);
        TreeNode n2 = new TreeNode(1, n3, n4);
        TreeNode n1 = new TreeNode(9, n2, null);
        int i2 = diameterOfBinaryTree(n1);
        System.out.println(i2);
    }
}

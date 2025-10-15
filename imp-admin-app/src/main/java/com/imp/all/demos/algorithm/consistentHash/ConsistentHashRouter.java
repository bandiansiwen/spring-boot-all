package com.imp.all.demos.algorithm.consistentHash;


import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性哈希的二次分表实现
 * 1. 核心原理
 * 哈希环构建：将分片节点（物理表）映射到一个虚拟的哈希环上（范围通常为 0~2^32-1）。
 * 数据路由：数据键（如 userID）通过哈希函数映射到环上，顺时针找到最近的节点作为目标分片。
 * 虚拟节点：为解决数据倾斜，每个物理节点对应多个虚拟节点（如 100 个），提升分布均匀性。
 * 2. 二次分表操作步骤
 * 场景：原分片键为 userID，现需扩容至 200 张表（原 100 张）。
 *
 * 操作流程
 *
 * 扩容准备：
 * 新增物理节点（如 table_100 ~ table_199）。
 * 调用 addNode 方法将新节点加入哈希环。
 * 数据迁移：
 * 遍历原分片节点的数据，重新计算哈希值，迁移至新节点。
 * 通过双写策略同步新旧节点数据，避免业务中断。
 * 流量切换：
 * 逐步将查询请求路由至新节点，验证数据一致性。
 * 完成后下线旧节点。
 * 3. 优缺点
 * 优点：
 * 扩容影响小：仅迁移相邻节点数据（约 1/N）。
 * 负载均衡：虚拟节点减少数据倾斜。
 * 缺点：
 * 实现复杂度高：需维护哈希环和虚拟节点映射。
 * 范围查询效率低：跨节点查询需合并结果。
 */
// 一致性哈希实现（简化版）
public class ConsistentHashRouter {
    private final SortedMap<Integer, String> ring = new TreeMap<>();
    private static final int VIRTUAL_NODES = 100; // 每个物理节点对应 100 虚拟节点

    // 添加物理节点
    public void addNode(String physicalNode) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNode = physicalNode + "_" + i;
            int hash = getHash(virtualNode);
            ring.put(hash, physicalNode);
        }
    }

    // 移除物理节点
    public void removeNode(String physicalNode) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNode = physicalNode + "_" + i;
            int hash = getHash(virtualNode);
            ring.remove(hash);
        }
    }

    // 数据路由
    public String route(String shardKey) {
        int hash = getHash(shardKey);
        SortedMap<Integer, String> tailMap = ring.tailMap(hash);
        Map.Entry<Integer, String> firstEntry = ring.entrySet().iterator().next();

        return tailMap.isEmpty() ? firstEntry.getValue() : tailMap.entrySet().iterator().next().getValue();
    }

    private int getHash(String key) {
        // 使用 MD5 或 CRC32 计算哈希值
        return Math.abs(key.hashCode()); 
    }
}
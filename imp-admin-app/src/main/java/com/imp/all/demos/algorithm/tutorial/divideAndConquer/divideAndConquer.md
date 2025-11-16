# 分治算法

### 什么是分治算法？

分治算法（Divide and Conquer）是一种重要的算法设计范式，它将一个大问题分解成若干个相同或相似的子问题，递归地解决子问题，最后将子问题的解合并得到原问题的解。

### 分治算法三步骤

1. 分解（Divide）：将原问题分解为若干个子问题 
2. 解决（Conquer）：递归地解决各个子问题 
3. 合并（Combine）：将子问题的解合并为原问题的解

### 分治算法关键要点

1. 递归终止条件：必须明确，防止无限递归 
2. 子问题独立性：子问题之间应该相互独立 
3. 合并策略：如何有效合并子问题的解 
4. 时间复杂度：通常用主定理分析

### 分治算法时间复杂度

- 归并排序：O(n log n)
- 快速排序：平均 O(n log n)，最坏 O(n²)
- 二分查找：O(log n)
- 快速幂：O(log n)

### 分治算法模板

```java
public ResultType divideConquer(Problem problem) {
    // 1. 递归终止条件
    if (problem is small enough) {
        return solveDirectly(problem);
    }
    
    // 2. 分解：将问题分解为子问题
    SubProblem[] subProblems = divide(problem);
    
    // 3. 解决：递归解决子问题
    ResultType[] subResults = new ResultType[subProblems.length];
    for (int i = 0; i < subProblems.length; i++) {
        subResults[i] = divideConquer(subProblems[i]);
    }
    
    // 4. 合并：合并子问题的解
    return combine(subResults);
}
```









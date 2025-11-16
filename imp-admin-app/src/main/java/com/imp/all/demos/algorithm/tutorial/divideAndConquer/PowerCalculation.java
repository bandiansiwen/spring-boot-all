package com.imp.all.demos.algorithm.tutorial.divideAndConquer;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 计算x的n次幂
 **/
public class PowerCalculation {
    public double myPow(double x, int n) {
        // 处理负指数
        long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        return fastPow(x, N);
    }
    
    private double fastPow(double x, long n) {
        // 分解：递归终止条件
        if (n == 0) {
            return 1.0;
        }
        
        // 解决：递归计算一半的幂
        double half = fastPow(x, n / 2);
        
        // 合并：根据n的奇偶性合并结果
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
    
    public static void main(String[] args) {
        PowerCalculation calculator = new PowerCalculation();
        System.out.println("2^10 = " + calculator.myPow(2, 10));   // 1024.0
        System.out.println("2^-2 = " + calculator.myPow(2, -2));   // 0.25
    }
}
package com.imp.all;

/**
 * @Author Daniel
 * @Date 2025/9/11/周四 19:01
 * @Description TODO
 **/
public class WorkerDemo {

    private Integer maxCount;
    private Integer minCount;
    private float temp;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getMinCount() {
        return minCount;
    }

    public void setMinCount(Integer minCount) {
        this.minCount = minCount;
    }

    public WorkerDemo(Integer minCount, Integer maxCount, float temp) {
        this.maxCount = maxCount;
        this.minCount = minCount;
        this.temp = temp;
    }

    public Float getReduceCount(Integer all) {
        if (all >= maxCount) {
            return ((maxCount-minCount) * temp);
        }
        else {
            return ((all-minCount) * temp);
        }
    }
}

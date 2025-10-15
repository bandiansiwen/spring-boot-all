package com.imp.all.transport;

import java.util.ArrayList;
import java.util.Arrays;

public class Transport {
    /**
     * 吨数的单线路
     *
     * @param weight    发货总吨数
     * @param a         A类型的车可装吨数
     * @param b         B类型的车可装吨数
     * @param c         C类型的车可装吨数
     * @param priority  true表示时间优先，false表示装载量优先
     * @param time      厂区工作时间(小时)
     * @param loadNum   一次能同时给多少台车装货、平均等货时间(分钟)
     * @param loadTime  平均等货时间（分钟）
     * @param transTime 线路所需时间（小时）
     * @param day       客户要求几天内送达
     * @return
     */
    public static ArrayList<String> SingleDistance(int weight, int a, int b, int c,
                                                   boolean priority, int time, int loadNum,
                                                   int loadTime, int transTime, int day) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        int[] arr = new int[]{a, b, c};
        int aNum = 0;
        int bNum = 0;
        int cNum = 0;
        int needTime = 0;
        int needDay = 0;
        int temp = 0; //记录余数
        transTime = transTime * 60;
        time = time * 60;

        Arrays.sort(arr);
        a = arr[0];
        b = arr[1];
        c = arr[2];

        if (weight % c == 0) {
            cNum = weight / c;
            if (cNum % loadNum == 0) {
                needTime = cNum / loadNum;
            } else {
                needTime = cNum / loadNum + 1;
            }
            needTime = needTime * loadTime; //装车所需要的时间
            if (needTime % time == 0) {
                needDay = needTime / time; //将装车时间按天计算
            } else {
                needDay = needTime / time;
                temp = needTime % time;
            }
            temp += transTime;
            if (temp >= 24) {
                temp = temp % 24;
                needDay += temp / 24;
            }
            if (needDay < day || (needDay == day && temp == 0)) {
                String str = "";
                for (int i = 1; i <= day; i++) {
                    if (cNum > time / loadTime * loadNum) {
                        str = "第" + i + "天" + c + "吨的车" + (time / loadTime * loadNum) + "台装" +
                            (time / loadTime * loadNum * c) + "吨。";
                        result.add(str);
                        cNum -= time / loadTime * loadNum;
                    } else {
                        if (cNum == 0) {
                            break;
                        }
                        str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                            (cNum * c) + "吨。";
                        cNum = 0;
                        result.add(str);
                    }
                }
                return result;
            } else {
                throw new Exception("最早也需要第" + needDay + "天" + temp + "点最后一车货才能送达。");
            }
        }
        cNum = weight / c;
        temp = weight % c;
        for (int i = 0; i < cNum; i++) {
            cNum--;
            temp += c;
            if (temp % b == 0) {
                bNum += temp / b;
                break;
            } else {
                int temp1 = temp % b;
                bNum += temp / b;
                temp = temp1;

                if (temp % a == 0) {
                    aNum = temp / a;
                    temp = 0;
                    break;
                }
            }
        }
        if ((cNum + aNum + bNum) % loadNum == 0) {
            needTime = (cNum + aNum + bNum) / loadNum;
        } else {
            needTime = (cNum + aNum + bNum) / loadNum + 1;
        }
        needTime = needTime * loadTime; //装车所需要的时间
        if (needTime % time == 0) {
            needDay = needTime / time; //将装车时间按天计算
        } else {
            needDay = needTime / time;
            temp = needTime % time;
        }
        temp += transTime;
        if (temp >= 24) {
            temp = temp % 24;
            needDay += temp / 24;
        }

        if (!priority) {
            if (needDay < day || (needDay == day && temp == 0)) {
                String str = "";
                for (int i = 1; i <= day; i++) {
                    if (cNum > time / loadTime * loadNum) {
                        str = "第" + i + "天" + c + "吨的车" + (time / loadTime * loadNum) + "台装" +
                            (time / loadTime * loadNum * c) + "吨。";
                        result.add(str);
                        cNum -= time / loadTime * loadNum;
                    } else {
                        if (cNum == 0 && bNum == 0 && aNum == 0) {
                            break;
                        }
                        if (bNum + aNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                                (cNum * c) + "吨，" + b + "吨的车" + bNum + "台装" +
                                (bNum * b) + "吨，" + a + "吨的车" + aNum + "台装" + (aNum * a) + "吨。";
                            result.add(str);
                            aNum = 0;
                            bNum = 0;
                        } else {
                            if (bNum <= time / loadTime * loadNum) {
                                str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                                    (cNum * c) + "吨，" + b + "吨的车" + bNum + "台装" +
                                    (bNum * b) + "吨，" + a + "吨的车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                    + "台装" + ((aNum - (aNum + bNum) - time / loadTime * loadNum) * a) + "吨。";
                                b = 0;
                                aNum = (aNum + bNum) - time / loadTime * loadNum;
                            }
                        }
                        cNum = 0;
                        result.add(str);
                    }
                }
                return result;
            } else {
                temp = weight % c;
                if (temp > a) {
                    throw new Exception("最后一车满载" + b + "吨，最后一车只剩下" + temp + "吨货。");
                } else {
                    throw new Exception("最后一车满载" + a + "吨，最后一车只剩下" + temp + "吨货。");
                }
            }
        } else {
            String str = "";
            for (int i = 1; i <= day; i++) {
                if (cNum > time / loadTime * loadNum) {
                    str = "第" + i + "天" + c + "吨的车" + (time / loadTime * loadNum) + "台装" +
                        (time / loadTime * loadNum * c) + "吨。";
                    result.add(str);
                    cNum -= time / loadTime * loadNum;
                } else {
                    if (cNum == 0 && bNum == 0 && aNum == 0) {
                        break;
                    }
                    if (bNum + aNum <= time / loadTime * loadNum) {
                        str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                            (cNum * c) + "吨，" + b + "吨的车" + bNum + "台装" +
                            (bNum * b) + "吨，" + a + "吨的车" + aNum + "台装" + (aNum * a) + "吨。";
                        result.add(str);
                        aNum = 0;
                        bNum = 0;
                    } else {
                        if (bNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                                (cNum * c) + "吨，" + b + "吨的车" + bNum + "台装" +
                                (bNum * b) + "吨，" + a + "吨的车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                + "台装" + ((aNum - (aNum + bNum) - time / loadTime * loadNum) * a) + "吨。";
                            b = 0;
                            aNum = (aNum + bNum) - time / loadTime * loadNum;
                            result.add(str);
                        }
                    }
                    cNum = 0;
                }
            }
            return result;
        }
    }

    /**
     * 箱数的单线路
     *
     * @param weight    发货总箱数
     * @param a         A类型的车可装箱数
     * @param b         B类型的车可装箱数
     * @param c         C类型的车可装箱数
     * @param priority  true表示时间优先，false表示装载量优先
     * @param time      厂区工作时间(小时)
     * @param loadNum   一次能同时给多少台车装货、平均等货时间(分钟)
     * @param loadTime  平均等货时间（分钟）
     * @param transTime 线路所需时间（小时）
     * @param day       客户要求几天内送达
     * @return
     */
    public static ArrayList<String> SingleDistance1(int weight, int a, int b, int c,
                                                    boolean priority, int time, int loadNum,
                                                    int loadTime, int transTime, int day) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        int[] arr = new int[]{a, b, c};
        int aNum = 0;
        int bNum = 0;
        int cNum = 0;
        int needTime = 0;
        int needDay = 0;
        int temp = 0; //记录余数
        transTime = transTime * 60;
        time = time * 60;

        Arrays.sort(arr);
        a = arr[0];
        b = arr[1];
        c = arr[2];

        if (weight % c == 0) {
            cNum = weight / c;
            if (cNum % loadNum == 0) {
                needTime = cNum / loadNum;
            } else {
                needTime = cNum / loadNum + 1;
            }
            needTime = needTime * loadTime; //装车所需要的时间
            if (needTime % time == 0) {
                needDay = needTime / time; //将装车时间按天计算
            } else {
                needDay = needTime / time;
                temp = needTime % time;
            }
            temp += transTime;
            if (temp >= 24) {
                temp = temp % 24;
                needDay += temp / 24;
            }
            if (needDay < day || (needDay == day && temp == 0)) {
                String str = "";
                for (int i = 1; i <= day; i++) {
                    if (cNum > time / loadTime * loadNum) {
                        str = "第" + i + "天" + c + "箱的车" + (time / loadTime * loadNum) + "台装" +
                            (time / loadTime * loadNum * c) + "箱。";
                        result.add(str);
                        cNum -= time / loadTime * loadNum;
                    } else {
                        if (cNum == 0) {
                            break;
                        }
                        str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                            (cNum * c) + "箱。";
                        cNum = 0;
                        result.add(str);
                    }
                }
                return result;
            } else {
                throw new Exception("最早也需要第" + needDay + "天" + temp + "点最后一车货才能送达。");
            }
        }
        cNum = weight / c;
        temp = weight % c;
        for (int i = 0; i < cNum; i++) {
            cNum--;
            temp += c;
            if (temp % b == 0) {
                bNum += temp / b;
                break;
            } else {
                int temp1 = temp % b;
                bNum += temp / b;
                temp = temp1;

                if (temp % a == 0) {
                    aNum = temp / a;
                    temp = 0;
                    break;
                }
            }
        }
        if ((cNum + aNum + bNum) % loadNum == 0) {
            needTime = (cNum + aNum + bNum) / loadNum;
        } else {
            needTime = (cNum + aNum + bNum) / loadNum + 1;
        }
        needTime = needTime * loadTime; //装车所需要的时间
        if (needTime % time == 0) {
            needDay = needTime / time; //将装车时间按天计算
        } else {
            needDay = needTime / time;
            temp = needTime % time;
        }
        temp += transTime;
        if (temp >= 24) {
            temp = temp % 24;
            needDay += temp / 24;
        }

        if (!priority) {
            if (needDay < day || (needDay == day && temp == 0)) {
                String str = "";
                for (int i = 1; i <= day; i++) {
                    if (cNum > time / loadTime * loadNum) {
                        str = "第" + i + "天" + c + "箱的车" + (time / loadTime * loadNum) + "台装" +
                            (time / loadTime * loadNum * c) + "箱。";
                        result.add(str);
                        cNum -= time / loadTime * loadNum;
                    } else {
                        if (cNum == 0 && bNum == 0 && aNum == 0) {
                            break;
                        }
                        if (bNum + aNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                                (cNum * c) + "箱，" + b + "箱的车" + bNum + "台装" +
                                (bNum * b) + "箱，" + a + "箱的车" + aNum + "台装" + (aNum * a) + "箱。";
                            result.add(str);
                            aNum = 0;
                            bNum = 0;
                        } else {
                            if (bNum <= time / loadTime * loadNum) {
                                str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                                    (cNum * c) + "箱，" + b + "箱的车" + bNum + "台装" +
                                    (bNum * b) + "箱，" + a + "箱的车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                    + "台装" + ((aNum - (aNum + bNum) - time / loadTime * loadNum) * a) + "箱。";
                                b = 0;
                                aNum = (aNum + bNum) - time / loadTime * loadNum;
                            }
                        }
                        cNum = 0;
                        result.add(str);
                    }
                }
                return result;
            } else {
                temp = weight % c;
                if (temp > a) {
                    throw new Exception("最后一车满载" + b + "箱，最后一车只剩下" + temp + "箱货。");
                } else {
                    throw new Exception("最后一车满载" + a + "箱，最后一车只剩下" + temp + "箱货。");
                }
            }
        } else {
            String str = "";
            for (int i = 1; i <= day; i++) {
                if (cNum > time / loadTime * loadNum) {
                    str = "第" + i + "天" + c + "箱的车" + (time / loadTime * loadNum) + "台装" +
                        (time / loadTime * loadNum * c) + "箱。";
                    result.add(str);
                    cNum -= time / loadTime * loadNum;
                } else {
                    if (cNum == 0 && bNum == 0 && aNum == 0) {
                        break;
                    }
                    if (bNum + aNum <= time / loadTime * loadNum) {
                        str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                            (cNum * c) + "箱，" + b + "箱的车" + bNum + "台装" +
                            (bNum * b) + "箱，" + a + "箱的车" + aNum + "台装" + (aNum * a) + "箱。";
                        result.add(str);
                        aNum = 0;
                        bNum = 0;
                    } else {
                        if (bNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                                (cNum * c) + "箱，" + b + "箱的车" + bNum + "台装" +
                                (bNum * b) + "箱，" + a + "箱的车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                + "台装" + ((aNum - (aNum + bNum) - time / loadTime * loadNum) * a) + "箱。";
                            b = 0;
                            aNum = (aNum + bNum) - time / loadTime * loadNum;
                            result.add(str);
                        }
                    }
                    cNum = 0;
                }
            }
            return result;
        }
    }

    /**
     * 吨数的多线路
     *
     * @param weight1    发货总吨数(发货吨数与发货时间相对应)
     * @param a          A类型的车可装吨数
     * @param b          B类型的车可装吨数
     * @param c          C类型的车可装吨数
     * @param priority   true表示时间优先，false表示装载量优先
     * @param time       厂区工作时间(小时)
     * @param loadNum    一次能同时给多少台车装货、平均等货时间(分钟)
     * @param loadTime   平均等货时间（分钟）
     * @param transTime1 线路所需时间（小时）
     * @param day        客户要求几天内送达
     * @return
     */
    public static ArrayList<String> DoubleDistance(int[] weight1, int a, int b, int c,
                                                   boolean priority, int time, int loadNum,
                                                   int loadTime, int[] transTime1, int day) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        int[] arr = new int[]{a, b, c};
        int weight = 0;
        int aNum = 0;
        int bNum = 0;
        int cNum = 0;
        int needTime = 0;
        int needDay = 0;
        int temp = 0; //记录余数
        time = time * 60;

        Arrays.sort(arr);
        Arrays.sort(transTime1);
        a = arr[0];
        b = arr[1];
        c = arr[2];
        int transTime = transTime1[transTime1.length - 1] * 60;

        for (int i = 0; i < weight1.length; i++) {
            weight += weight1[i];
        }

        if (weight % c == 0) {
            cNum = weight / c;
            if (cNum % loadNum == 0) {
                needTime = cNum / loadNum;
            } else {
                needTime = cNum / loadNum + 1;
            }
            needTime = needTime * loadTime; //装车所需要的时间
            if (needTime % time == 0) {
                needDay = needTime / time; //将装车时间按天计算
            } else {
                needDay = needTime / time;
                temp = needTime % time;
            }
            temp += transTime;
            if (temp >= 24) {
                temp = temp % 24;
                needDay += temp / 24;
            }
            if (needDay < day || (needDay == day && temp == 0)) {

            } else {
                throw new Exception("最早也需要第" + needDay + "天" + temp + "点最后一车货才能送达。");
            }
        }

        int[][] count = countNum(weight1, a, b, c);
        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[0].length; j++) {
                if (j == 0) {
                    cNum += count[i][j];
                } else if (j == 1) {
                    bNum += count[i][j];
                } else if (j == 2) {
                    aNum += count[i][j];
                }
            }
        }
        if ((cNum + aNum + bNum) % loadNum == 0) {
            needTime = (cNum + aNum + bNum) / loadNum;
        } else {
            needTime = (cNum + aNum + bNum) / loadNum + 1;
        }
        needTime = needTime * loadTime; //装车所需要的时间
        if (needTime % time == 0) {
            needDay = needTime / time; //将装车时间按天计算
        } else {
            needDay = needTime / time;
            temp = needTime % time;
        }
        temp += transTime;
        if (temp >= 24) {
            temp = temp % 24;
            needDay += temp / 24;
        }

        if (!priority) {
            if (needDay < day || (needDay == day && temp == 0)) {
                String str = "";
                for (int i = 1; i <= day; i++) {
                    if (cNum > time / loadTime * loadNum) {
                        str = "第" + i + "天" + c + "吨的车" + (time / loadTime * loadNum) + "台装" +
                            (time / loadTime * loadNum * c) + "吨。";
                        result.add(str);
                        cNum -= time / loadTime * loadNum;
                    } else {
                        if (cNum == 0 && bNum == 0 && aNum == 0) {
                            break;
                        }
                        if (bNum + aNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                                (cNum * c) + "吨，" + b + "吨的车" + bNum + "台装" +
                                (bNum * b) + "吨，" + a + "吨的车" + aNum + "台装" + (aNum * a) + "吨。";
                            result.add(str);
                            aNum = 0;
                            bNum = 0;
                        } else {
                            if (bNum <= time / loadTime * loadNum) {
                                str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                                    (cNum * c) + "吨，" + b + "吨的车" + bNum + "台装" +
                                    (bNum * b) + "吨，" + a + "吨的车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                    + "台装" + ((aNum - (aNum + bNum) - time / loadTime * loadNum) * a) + "吨。";
                                b = 0;
                                aNum = (aNum + bNum) - time / loadTime * loadNum;
                            }
                        }
                        cNum = 0;
                        result.add(str);
                    }
                }
                return result;
            } else {
                temp = weight % c;
                if (temp > a) {
                    throw new Exception("最后一车满载" + b + "吨，最后一车只剩下" + temp + "吨货。");
                } else {
                    throw new Exception("最后一车满载" + a + "吨，最后一车只剩下" + temp + "吨货。");
                }
            }
        } else {
            String str = "";
            for (int i = 1; i <= day; i++) {
                if (cNum > time / loadTime * loadNum) {
                    str = "第" + i + "天" + c + "吨的车" + (time / loadTime * loadNum) + "台装" +
                        (time / loadTime * loadNum * c) + "吨。";
                    result.add(str);
                    cNum -= time / loadTime * loadNum;
                } else {
                    if (cNum == 0 && bNum == 0 && aNum == 0) {
                        break;
                    }
                    if (bNum + aNum <= time / loadTime * loadNum) {
                        str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                            (cNum * c) + "吨，" + b + "吨的车" + bNum + "台装" +
                            (bNum * b) + "吨，" + a + "吨的车" + aNum + "台装" + (aNum * a) + "吨。";
                        result.add(str);
                        aNum = 0;
                        bNum = 0;
                    } else {
                        if (bNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，" + c + "吨的车" + cNum + "台装" +
                                (cNum * c) + "吨，" + b + "吨的车" + bNum + "台装" +
                                (bNum * b) + "吨，" + a + "吨的车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                + "台装" + ((aNum - (aNum + bNum) - time / loadTime * loadNum) * a) + "吨。";
                            b = 0;
                            aNum = (aNum + bNum) - time / loadTime * loadNum;
                            result.add(str);
                        }
                    }
                    cNum = 0;
                }
            }
            return result;
        }
    }

    public static int[][] countNum(int[] weight, int a, int b, int c) {
        int[][] count = new int[weight.length][3];
        int aNum = 0;
        int bNum = 0;
        int cNum = 0;
        int temp;
        for (int m = 0; m < weight.length; m++) {
            cNum = weight[m] / c;
            temp = weight[m] % c;
            for (int i = 0; i < cNum; i++) {
                cNum--;
                temp += c;
                if (temp % b == 0) {
                    bNum += temp / b;
                    break;
                } else {
                    int temp1 = temp % b;
                    bNum += temp / b;
                    temp = temp1;

                    if (temp % a == 0) {
                        aNum = temp / a;
                        temp = 0;
                        break;
                    }
                }
            }
            count[m][0] = cNum;
            count[m][1] = bNum;
            count[m][2] = aNum;
        }
        return count;
    }

    /**
     * 箱数的多线路
     *
     * @param weight1    发货总箱数(发货箱数与发货时间相对应)
     * @param a          A类型的车可装箱数
     * @param b          B类型的车可装箱数
     * @param c          C类型的车可装箱数
     * @param priority   true表示时间优先，false表示装载量优先
     * @param time       厂区工作时间(小时)
     * @param loadNum    一次能同时给多少台车装货、平均等货时间(分钟)
     * @param loadTime   平均等货时间（分钟）
     * @param transTime1 线路所需时间（小时）
     * @param day        客户要求几天内送达
     * @return
     */
    public static ArrayList<String> DoubleDistance1(int[] weight1, int a, int b, int c,
                                                    boolean priority, int time, int loadNum,
                                                    int loadTime, int[] transTime1, int day) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        int[] arr = new int[]{a, b, c};
        int weight = 0;
        int aNum = 0;
        int bNum = 0;
        int cNum = 0;
        int needTime = 0;
        int needDay = 0;
        int temp = 0; //记录余数
        time = time * 60;

        Arrays.sort(arr);
        Arrays.sort(transTime1);
        a = arr[0];
        b = arr[1];
        c = arr[2];
        int transTime = transTime1[transTime1.length - 1] * 60;

        for (int i = 0; i < weight1.length; i++) {
            weight += weight1[i];
        }

        if (weight % c == 0) {
            cNum = weight / c;
            if (cNum % loadNum == 0) {
                needTime = cNum / loadNum;
            } else {
                needTime = cNum / loadNum + 1;
            }
            needTime = needTime * loadTime; //装车所需要的时间
            if (needTime % time == 0) {
                needDay = needTime / time; //将装车时间按天计算
            } else {
                needDay = needTime / time;
                temp = needTime % time;
            }
            temp += transTime;
            if (temp >= 24) {
                temp = temp % 24;
                needDay += temp / 24;
            }
            if (needDay < day || (needDay == day && temp == 0)) {

            } else {
                throw new Exception("最早也需要第" + needDay + "天" + temp + "点最后一车货才能送达。");
            }
        }

        int[][] count = countNum(weight1, a, b, c);
        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[0].length; j++) {
                if (j == 0) {
                    cNum += count[i][j];
                } else if (j == 1) {
                    bNum += count[i][j];
                } else if (j == 2) {
                    aNum += count[i][j];
                }
            }
        }
        if ((cNum + aNum + bNum) % loadNum == 0) {
            needTime = (cNum + aNum + bNum) / loadNum;
        } else {
            needTime = (cNum + aNum + bNum) / loadNum + 1;
        }
        needTime = needTime * loadTime; //装车所需要的时间
        if (needTime % time == 0) {
            needDay = needTime / time; //将装车时间按天计算
        } else {
            needDay = needTime / time;
            temp = needTime % time;
        }
        temp += transTime;
        if (temp >= 24) {
            temp = temp % 24;
            needDay += temp / 24;
        }

        if (!priority) {
            if (needDay < day || (needDay == day && temp == 0)) {
                String str = "";
                for (int i = 1; i <= day; i++) {
                    if (cNum > time / loadTime * loadNum) {
                        str = "第" + i + "天" + c + "箱的车" + (time / loadTime * loadNum) + "台装" +
                            (time / loadTime * loadNum * c) + "箱。";
                        result.add(str);
                        cNum -= time / loadTime * loadNum;
                    } else {
                        if (cNum == 0 && bNum == 0 && aNum == 0) {
                            break;
                        }
                        if (bNum + aNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                                (cNum * c) + "箱，" + b + "箱的车" + bNum + "台装" +
                                (bNum * b) + "箱，" + a + "箱的车" + aNum + "台装" + (aNum * a) + "箱。";
                            result.add(str);
                            aNum = 0;
                            bNum = 0;
                        } else {
                            if (bNum <= time / loadTime * loadNum) {
                                str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                                    (cNum * c) + "箱，" + b + "箱的车" + bNum + "台装" +
                                    (bNum * b) + "箱，" + a + "箱的车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                    + "台装" + ((aNum - (aNum + bNum) - time / loadTime * loadNum) * a) + "箱。";
                                b = 0;
                                aNum = (aNum + bNum) - time / loadTime * loadNum;
                            }
                        }
                        cNum = 0;
                        result.add(str);
                    }
                }
                return result;
            } else {
                temp = weight % c;
                if (temp > a) {
                    throw new Exception("最后一车满载" + b + "吨，最后一车只剩下" + temp + "箱货。");
                } else {
                    throw new Exception("最后一车满载" + a + "吨，最后一车只剩下" + temp + "箱货。");
                }
            }
        } else {
            String str = "";
            for (int i = 1; i <= day; i++) {
                if (cNum > time / loadTime * loadNum) {
                    str = "第" + i + "天" + c + "箱的车" + (time / loadTime * loadNum) + "台装" +
                        (time / loadTime * loadNum * c) + "箱。";
                    result.add(str);
                    cNum -= time / loadTime * loadNum;
                } else {
                    if (cNum == 0 && bNum == 0 && aNum == 0) {
                        break;
                    }
                    if (bNum + aNum <= time / loadTime * loadNum) {
                        str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                            (cNum * c) + "箱，" + b + "箱的车" + bNum + "台装" +
                            (bNum * b) + "箱，" + a + "箱的车" + aNum + "台装" + (aNum * a) + "箱。";
                        result.add(str);
                        aNum = 0;
                        bNum = 0;
                    } else {
                        if (bNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，" + c + "箱的车" + cNum + "台装" +
                                (cNum * c) + "箱，" + b + "箱的车" + bNum + "台装" +
                                (bNum * b) + "箱，" + a + "箱的车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                + "台装" + ((aNum - (aNum + bNum) - time / loadTime * loadNum) * a) + "箱。";
                            b = 0;
                            aNum = (aNum + bNum) - time / loadTime * loadNum;
                            result.add(str);
                        }
                    }
                    cNum = 0;
                }
            }
            return result;
        }
    }

    /**
     * 车次的单线路
     *
     * @param aNum          A类型的车数
     * @param bNum          B类型的车数
     * @param cNum          C类型的车数
     * @param time       厂区工作时间(小时)
     * @param loadNum    一次能同时给多少台车装货、平均等货时间(分钟)
     * @param loadTime   平均等货时间（分钟）
     * @param transTime 线路所需时间（小时）
     * @param day        客户要求几天内送达
     * @return
     */
    public static ArrayList<String> trainNumberSingle(int aNum, int bNum, int cNum,
                                                 int time, int loadNum,
                                                int loadTime, int transTime, int day) throws Exception {
        ArrayList<String> result = new ArrayList<>();

        int needTime = 0;
        int needDay = 0;
        int temp = 0; //记录余数
        time = time * 60;


        transTime = transTime * 60;

        if ((cNum + aNum + bNum) % loadNum == 0) {
            needTime = (cNum + aNum + bNum) / loadNum;
        } else {
            needTime = (cNum + aNum + bNum) / loadNum + 1;
        }
        needTime = needTime * loadTime; //装车所需要的时间
        if (needTime % time == 0) {
            needDay = needTime / time; //将装车时间按天计算
        } else {
            needDay = needTime / time;
            temp = needTime % time;
        }
        temp += transTime;
        if (temp >= 24) {
            temp = temp % 24;
            needDay += temp / 24;
        }

        if (needDay < day || (needDay == day && temp == 0)) {
            String str = "";
            for (int i = 1; i <= day; i++) {
                if (cNum > time / loadTime * loadNum) {
                    str = "第" + i + "天C车" + (time / loadTime * loadNum) + "台。";
                    result.add(str);
                    cNum -= time / loadTime * loadNum;
                } else {
                    if (cNum == 0 && bNum == 0 && aNum == 0) {
                        break;
                    }
                    if (cNum + bNum + aNum <= time / loadTime * loadNum) {
                        str = "第" + i + "天，C车" + cNum + "台，B车" + bNum + "台，A车" + aNum + "台。";
                        result.add(str);
                        aNum = 0;
                        bNum = 0;
                    } else {
                        if (bNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，C车" + cNum + "台。" +
                                "B车" + bNum + "台，A车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                + "台。";
                            bNum = 0;
                            aNum = (aNum + bNum) - time / loadTime * loadNum;
                        }
                    }
                    cNum = 0;
                }
            }
            return result;
        } else {
            throw new Exception("至少需要" + needDay + "天" + temp + "小时才能送达！");
        }
    }

    /**
     * 车次的单线路
     *
     * @param aNum1          A类型的车数(与线路对应)
     * @param bNum1          B类型的车数(与线路对应)
     * @param cNum1          C类型的车数(与线路对应)
     * @param time       厂区工作时间(小时)
     * @param loadNum    一次能同时给多少台车装货、平均等货时间(分钟)
     * @param loadTime   平均等货时间（分钟）
     * @param transTime1 线路所需时间（小时）
     * @param day        客户要求几天内送达
     * @return
     */
    public static ArrayList<String> trainNumberDouble(int[] aNum1, int[] bNum1, int[] cNum1,
                                                      int time, int loadNum,
                                                      int loadTime, int[] transTime1, int day) throws Exception {
        ArrayList<String> result = new ArrayList<>();

        int needTime = 0;
        int needDay = 0;
        int temp = 0; //记录余数
        time = time * 60;
        int aNum = 0;
        int bNum = 0;
        int cNum = 0;

        Arrays.sort(transTime1);
        int transTime = transTime1[transTime1.length-1] * 60;

        for (int i = 0; i < transTime1.length; i++){
            aNum += aNum1[i];
            bNum += bNum1[i];
            cNum += cNum1[i];
        }

        if ((cNum + aNum + bNum) % loadNum == 0) needTime = (cNum + aNum + bNum) / loadNum;
        else needTime = (cNum + aNum + bNum) / loadNum + 1;
        needTime = needTime * loadTime; //装车所需要的时间
        if (needTime % time == 0) needDay = needTime / time; //将装车时间按天计算
        else {
            needDay = needTime / time;
            temp = needTime % time;
        }
        temp += transTime;
        if (temp >= 24) {
            temp = temp % 24;
            needDay += temp / 24;
        }

        if (needDay < day || (needDay == day && temp == 0)) {
            String str = "";
            for (int i = 1; i <= day; i++) {
                if (cNum > time / loadTime * loadNum) {
                    str = "第" + i + "天C车" + (time / loadTime * loadNum) + "台。";
                    result.add(str);
                    cNum -= time / loadTime * loadNum;
                } else {
                    if (cNum == 0 && bNum == 0 && aNum == 0) {
                        break;
                    }
                    if (cNum + bNum + aNum <= time / loadTime * loadNum) {
                        str = "第" + i + "天，C车" + cNum + "台，B车" + bNum + "台，A车" + aNum + "台。";
                        result.add(str);
                        aNum = 0;
                        bNum = 0;
                    } else {
                        if (bNum <= time / loadTime * loadNum) {
                            str = "第" + i + "天，C车" + cNum + "台。" +
                                "B车" + bNum + "台，A车" + (aNum - (aNum + bNum) - time / loadTime * loadNum)
                                + "台。";
                            bNum = 0;
                            aNum = (aNum + bNum) - time / loadTime * loadNum;
                        }
                    }
                    cNum = 0;
                }
            }
            return result;
        } else {
            throw new Exception("至少需要" + needDay + "天" + temp + "小时才能送达！");
        }
    }
    public static void main(String[] args) throws Exception {
        ArrayList<String> arrayList = trainNumberDouble(new int[]{3,3},new int[]{3,4},new int[]{4,4},8,3,30,new int[]{8,16},3);
        System.out.println(Arrays.asList(arrayList));

    }
}

package com.limb.limbpluginproject.algorithm;

import java.util.Random;

/**
 * 算法测试入口
 * @author 86137
 *
 */
public class AlgorithmTest {
    public static void main(String[] args) {
        int randomNum = new Random().nextInt(20);
        int[] arr = new int[randomNum];
        for (int i = 0; i < randomNum; i++) {
            arr[i] = new Random().nextInt(20);
        }

        // 冒泡排序
        AlgorithmUtils.bubblingSortAlgorithm(arr);
        // 插入排序
        AlgorithmUtils.insertSortAlgorithm(arr);
    }
}

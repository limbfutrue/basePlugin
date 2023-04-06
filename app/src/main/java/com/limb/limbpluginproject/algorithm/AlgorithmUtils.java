package com.limb.limbpluginproject.algorithm;

import java.util.Arrays;

public class AlgorithmUtils {
    static {
        logPrintStyle();
    }

    /**
     * 冒泡排序算法
     * 从下到大排序
     * 冒泡排序（Bubble Sort）是一种简单的排序算法，它重复地遍历要排序的数列，每次比较相邻的两个元素，如果它们的顺序错误就交换位置，直到没有任何一对数字需要交换为止。下面是 Java 冒泡排序的详细算法：
     *
     * 首先，我们定义了一个 `bubblingSortAlgorithm` 函数，它接收一个整数数组作为参数。在函数内部，我们获取数组的长度，并使用两个嵌套的 `for` 循环来进行排序。外部循环控制排序的轮数，内部循环控制每轮比较的次数。
     * 在每一轮比较中，我们比较相邻的两个元素，如果当前元素比下一个元素大，则交换它们的位置。这样，每一轮比较之后，最大的元素都会被移到数组的末尾。通过重复这个过程，我们最终能够得到一个有序的数组。
     * 需要注意的是，在每一轮比较中，我们只需要比较前面未排序的元素，因为已经排好序的元素不需要再次比较。
     *
     * @param srcData 无序数组
     */
    public static void bubblingSortAlgorithm(int[] srcData) {
        if (srcData == null) {
            return;
        }
        System.out.println("冒泡排序原数据：" + Arrays.toString(srcData));
        for (int i = 0; i < srcData.length - 1; i++) {
            for (int j = 0; j < srcData.length - i - 1; j++) {
                if (srcData[j] > srcData[j + 1]) {
                    int temp = srcData[j];
                    srcData[j] = srcData[j + 1];
                    srcData[j + 1] = temp;
                }
            }
        }
        System.out.println("冒泡排序结果：" + Arrays.toString(srcData));
    }


    /**
     * 插入排序算法
     * 插入排序算法的基本思想是将未排序的元素一个个插入到已排序的序列中，从而得到一个新的有序序列。其具体实现如下：
     * <p>
     * 1. 从第一个元素开始，我们认为它已经被排序过了，所以可以直接跳过。
     * 2. 取出下一个元素，与已经排好序的元素逐个比较，找到合适的插入位置，将该元素插入到排序序列中。
     * 3. 重复步骤 2 直到所有元素都被插入到排序序列中。
     * <p>
     * 在这段代码中，我们使用变量 `currentSortValue` 存储当前要插入的元素，然后使用变量 `j` 在已排序的元素中找到合适的插入位置。如果当前元素大于已排序的元素，将已排序的元素后移一位，直到找到了合适的位置，然后再将当前元素插入到已排序的元素中。
     * <p>
     * 插入排序算法的时间复杂度为 O(n^2)，空间复杂度为 O(1)。虽然插入排序算法不如快速排序算法等高级排序算法快，但对于小规模数组或基本有序的数组，插入排序算法是一个简单而有效的选择。
     *
     * @param srcData 无序数组
     */
    public static void insertSortAlgorithm(int[] srcData) {
        if (srcData == null) {
            return;
        }
        System.out.println("插入排序原数据：" + Arrays.toString(srcData));
        for (int i = 1; i < srcData.length; i++) {
            int currentSortValue = srcData[i];
            int j = i - 1;
            while (j >= 0 && srcData[j] > currentSortValue) {
                srcData[j + 1] = srcData[j];
                j -= 1;
            }
            srcData[j + 1] = currentSortValue;
        }
        System.out.println("插入排序结果：" + Arrays.toString(srcData));
    }


    private static void logPrintStyle(){
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("-----------                                                                                     --------");
        System.out.println("-----------                                 算法测试开始                                          --------");
        System.out.println("-----------                                                                                     --------");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }
}

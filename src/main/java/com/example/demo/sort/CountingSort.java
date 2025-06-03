package com.example.demo.sort;

import java.util.Arrays;

/*计数排序适用于范围较小的整数排序，通过计数数组记录每个元素出现的次数。*/
public class CountingSort {
    public static void countingSort(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        int[] count = new int[max + 1];
        for (int num : arr) {
            count[num]++;
        }
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            while (count[i]-- > 0) {
                arr[index++] = i;
            }
        }
    }
}


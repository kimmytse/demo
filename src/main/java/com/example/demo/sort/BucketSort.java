package com.example.demo.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*桶排序将数组元素分配到多个桶中，每个桶单独排序，然后合并结果。*/
public class BucketSort {
    public static void bucketSort(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        int min = Arrays.stream(arr).min().getAsInt();
        int bucketCount = (max - min) / arr.length + 1;
        ArrayList<Integer>[] buckets = new ArrayList[bucketCount];
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<>();
        }
        for (int num : arr) {
            buckets[(num - min) / arr.length].add(num);
        }
        int index = 0;
        for (ArrayList<Integer> bucket : buckets) {
            Collections.sort(bucket);
            for (int num : bucket) {
                arr[index++] = num;
            }
        }
    }
}


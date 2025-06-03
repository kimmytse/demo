package com.example.demo.sort;

import java.util.Arrays;

public class RadixSort {
    public static void radixSort(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        int exp = 1;
        while (max / exp > 0) {
            countingSortByDigit(arr, exp);
            exp *= 10;
        }
    }

    private static void countingSortByDigit(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        for (int num : arr) {
            count[(num / exp) % 10]++;
        }
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            int num = arr[i];
            output[count[(num / exp) % 10] - 1] = num;
            count[(num / exp) % 10]--;
        }
        System.arraycopy(output, 0, arr, 0, n);
    }

    public static void main(String[] args) {
        int[] arr = {170, 45, 75, 90, 802, 24, 2, 66};
        System.out.println("排序前数组: " + Arrays.toString(arr));
        radixSort(arr);
        System.out.println("排序后数组: " + Arrays.toString(arr));
    }
}

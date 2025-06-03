package com.example.demo.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//回溯算法，n个数，一个target，找出所有和为target的组合，不能重复选同一个数
public class CombinationSum2 {
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates); // 排序以便于后续处理
        backtrack(result, new ArrayList<>(), candidates, target, 0);
        return result;
    }

    private static void backtrack(List<List<Integer>> result, List<Integer> tempList, int[] candidates, int remain, int start) {
        if (remain < 0) return; // 如果剩余值小于0，直接返回
        else if (remain == 0) result.add(new ArrayList<>(tempList)); // 如果剩余值为0，找到一个组合
        else {
            for (int i = start; i < candidates.length; i++) {
                if (i > start && candidates[i] == candidates[i - 1]) continue; // 跳过重复元素
                tempList.add(candidates[i]);
                backtrack(result, tempList, candidates, remain - candidates[i], i + 1); // i + 1 确保不重复选择同一个数
                tempList.remove(tempList.size() - 1); // 回溯
            }
        }
    }

    public static void main(String[] args) {
        int[] candidates = {1, 2, 4, 3, 6, 7};
        int target = 7;
        List<List<Integer>> combinations = combinationSum(candidates, target);
        for (List<Integer> combination : combinations) {
            System.out.println(combination);
        }
    }
}






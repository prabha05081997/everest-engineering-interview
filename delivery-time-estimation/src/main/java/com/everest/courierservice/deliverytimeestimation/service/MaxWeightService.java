package com.everest.courierservice.deliverytimeestimation.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MaxWeightService {

    private static MaxWeightService maxWeightService;
    private boolean[][] dp;
    private static List<List<Integer>> maxWeights;

    private MaxWeightService() {

    }

    public static MaxWeightService getInstance() {
        if (maxWeightService == null) {
            synchronized (MaxWeightService.class) {
                if (maxWeightService == null) {
                    log.info("creating an instance of max weight service");
                    maxWeightService = new MaxWeightService();
                }
            }
        }
        return maxWeightService;
    }

    public List<Integer> findMaxWeights(List<Integer> weights, int maxSum) {
        int[] weightsArray = weights.stream().mapToInt(Integer::intValue).toArray();
        List<List<Integer>> maxWeightsList = getAllMaxSubsetsWeight(weightsArray, weights.size(), maxSum);
        return maxWeightsList.size() > 0 ? maxWeightsList.get(0) : new ArrayList<>();
    }

    private void getMaxSubsetsRec(int[] arr, int i, int sum, List<Integer> p) {
        if (i == 0 && sum != 0 && dp[0][sum]) {
            p.add(arr[i]);
            maxWeights.add(p);
            System.out.println("maxWeights while display" + maxWeights);
            return;
        }

        if (i == 0 && sum == 0) {
            maxWeights.add(p);
            System.out.println("maxWeights while display" + maxWeights);
            return;
        }

        if (dp[i-1][sum]) {
            ArrayList<Integer> b = new ArrayList<>(p);
            getMaxSubsetsRec(arr, i-1, sum, b);
        }

        if (sum >= arr[i] && dp[i-1][sum-arr[i]]) {
            p.add(arr[i]);
            getMaxSubsetsRec(arr, i-1, sum-arr[i], p);
        }
    }

    private List<List<Integer>> getAllMaxSubsetsWeight(int[] arr, int n, int sum) {
        maxWeights = new ArrayList<>();
        if (n == 0 || sum < 0)
            return maxWeights;

        dp = new boolean[n][sum + 1];
        for (int i=0; i<n; ++i) {
            dp[i][0] = true;
        }

        if (arr[0] <= sum) dp[0][arr[0]] = true;

        for (int i = 1; i < n; ++i) {
            for (int j = 0; j < sum + 1; ++j) {
                dp[i][j] = (arr[i] <= j) ? (dp[i - 1][j] || dp[i - 1][j - arr[i]]) : dp[i - 1][j];
            }
        }

        int index_i = 0, index_j = 0;
        for (int i = dp.length - 1; i >= 0; i--) {
            for (int j = dp[i].length - 1; j >= 0; j--) {
                if (dp[i][j]) {
                    index_i = i;
                    index_j = j;
                    break;
                }
            }
            if(index_i != 0 || index_j != 0) {
                break;
            }
        }

        if(!dp[index_i][index_j]) {
            System.out.println("There are no subsets with sum "+ sum);
            return maxWeights;
        }
        List<Integer> p = new ArrayList<>();
        getMaxSubsetsRec(arr, index_i, index_j, p);
        log.info("maxWeights {}", maxWeights);
        return maxWeights;
    }
}

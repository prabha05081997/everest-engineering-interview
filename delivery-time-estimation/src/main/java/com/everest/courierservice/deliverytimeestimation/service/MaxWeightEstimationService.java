package com.everest.courierservice.deliverytimeestimation.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class MaxWeightEstimationService {

    private static MaxWeightEstimationService maxWeightEstimationService;
    private boolean[][] dp;
    private static List<List<Integer>> maxWeights;

    private MaxWeightEstimationService() {

    }

    public static MaxWeightEstimationService getInstance() {
        if (maxWeightEstimationService == null) {
            synchronized (MaxWeightEstimationService.class) {
                if (maxWeightEstimationService == null) {
                    log.info("creatinng an instance of max weight service");
                    maxWeightEstimationService = new MaxWeightEstimationService();
                }
            }
        }
        return maxWeightEstimationService;
    }

    /**
     *
     * This method will delegate the calculation for max assignable package weight for vehicle
     *
     * @param weights - List of weights
     * @param maxSum - max assignable weight for vehicle
     * @return - List of Max assignable weights
     *
     */
    public List<Integer> findMaxWeights(List<Integer> weights, int maxSum) {
        int[] weightsArray = weights.stream().mapToInt(Integer::intValue).toArray();
        List<List<Integer>> maxWeightsList = getAllMaxSubsetsWeight(weightsArray, weights.size(), maxSum);
        log.info("max weigts list {}", maxWeightsList);
        return maxWeightsList.size() > 0 ? sortListByValue(maxWeightsList).get(0) : new ArrayList<>();
    }

    private void getMaxSubsetsRec(int[] arr, int i, int sum, List<Integer> p) {
        if (i == 0 && sum != 0 && dp[0][sum]) {
            p.add(arr[i]);
            maxWeights.add(p);
            log.info("maxWeights while display {}", maxWeights);
            return;
        }

        if (i == 0 && sum == 0) {
            maxWeights.add(p);
            log.info("maxWeights while display {}", maxWeights);
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

    /**
     *
     * This method will calculate max assignable package weight for vehicle
     *
     * @param arr - Array of weights
     * @param n - size of array
     * @param sum - max assignable weight for vehicle
     * @return - List of Max assignable weights
     *
     */
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
            log.info("There are no subsets with sum "+ sum);
            return maxWeights;
        }
        List<Integer> p = new ArrayList<>();
        getMaxSubsetsRec(arr, index_i, index_j, p);
        log.info("maxWeights {}", maxWeights);
        return maxWeights;
    }

    /**
     *
     * This method will sort the package weights based on number of packages
     *
     * @param list - List of weights of list
     * @param <T> - List of weights
     * @return - sorted by desc list
     */
    public static <T> List<? extends List<T>> sortListByValue(List<? extends List<T>> list) {
        if(list.size() == 1) return list;
        list.sort((Comparator<List<T>>) (o1, o2) -> Integer.compare(o2.size(), o1.size()));
        return list;
    }
}

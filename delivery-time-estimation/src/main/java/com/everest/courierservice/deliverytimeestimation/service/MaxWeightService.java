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
        List<List<Integer>> maxWeightsList = printAllSubsets(weightsArray, weights.size(), maxSum);
        return maxWeightsList.size() > 0 ? maxWeightsList.get(0) : new ArrayList<>();
    }

    private void display(List<Integer> v) {
        System.out.println(v);
//        maxWeights.add(v);
    }

    // A recursive function to print all subsets with the
    // help of dp[][]. Vector p[] stores current subset.
    private void printSubsetsRec(int[] arr, int i, int sum, List<Integer> p) {
        // If we reached end and sum is non-zero. We print
        // p[] only if arr[0] is equal to sum OR dp[0][sum]
        // is true.
        if (i == 0 && sum != 0 && dp[0][sum]) {
            p.add(arr[i]);
            display(p);
            maxWeights.add(p);
            System.out.println("maxWeights while display" + maxWeights);
//            p.clear();
            return;
        }

        // If sum becomes 0
        if (i == 0 && sum == 0) {
            display(p);
            maxWeights.add(p);
            System.out.println("maxWeights while display" + maxWeights);
//            p.clear();
            return;
        }

        // If given sum can be achieved after ignoring
        // current element.
        if (dp[i-1][sum]) {
            // Create a new vector to store path
            ArrayList<Integer> b = new ArrayList<>(p);
            printSubsetsRec(arr, i-1, sum, b);
        }

        // If given sum can be achieved after considering
        // current element.
        if (sum >= arr[i] && dp[i-1][sum-arr[i]]) {
            p.add(arr[i]);
            printSubsetsRec(arr, i-1, sum-arr[i], p);
        }
    }

    // Prints all subsets of arr[0..n-1] with sum 0.
    private List<List<Integer>> printAllSubsets(int[] arr, int n, int sum) {
        maxWeights = new ArrayList<>();
        if (n == 0 || sum < 0)
            return maxWeights;

        // Sum 0 can always be achieved with 0 elements
        dp = new boolean[n][sum + 1];
        for (int i=0; i<n; ++i) {
            dp[i][0] = true;
        }

        // Sum arr[0] can be achieved with single element
        if (arr[0] <= sum)
            dp[0][arr[0]] = true;

        // Fill rest of the entries in dp[][]
        for (int i = 1; i < n; ++i)
            for (int j = 0; j < sum + 1; ++j)
                dp[i][j] = (arr[i] <= j) ? (dp[i-1][j] ||
                        dp[i-1][j-arr[i]])
                        : dp[i - 1][j];

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
        System.out.println("subset end sum " + dp[index_i][index_j]);
        if(!dp[index_i][index_j]) {
            System.out.println("There are no subsets with" +
                    " sum "+ sum);
            return maxWeights;
        }
        List<Integer> p = new ArrayList<>();
        printSubsetsRec(arr, index_i, index_j, p);
        System.out.println("maxWeights " + maxWeights);
        return maxWeights;
    }
}
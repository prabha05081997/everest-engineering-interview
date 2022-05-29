package com.everest.courierservice.deliverytimeestimation.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class MaxWeightEstimationServiceImpl implements MaxWeightEstimationService {

    private static MaxWeightEstimationServiceImpl maxWeightEstimationServiceImpl;
    private boolean[][] maxWeightEstimationArray; // two dimensional array to store sub values to calculate max weights in dynamic programming approach
    private static List<List<Integer>> maxWeights;

    private MaxWeightEstimationServiceImpl() {

    }

    public static MaxWeightEstimationServiceImpl getInstance() {
        if (maxWeightEstimationServiceImpl == null) {
            synchronized (MaxWeightEstimationServiceImpl.class) {
                if (maxWeightEstimationServiceImpl == null) {
                    log.info("creatinng an instance of max weight service");
                    maxWeightEstimationServiceImpl = new MaxWeightEstimationServiceImpl();
                }
            }
        }
        return maxWeightEstimationServiceImpl;
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
    @Override
    public List<Integer> findMaxWeights(List<Integer> weights, int maxSum) {
        int[] weightsArray = weights.stream().mapToInt(Integer::intValue).toArray();
        List<List<Integer>> maxWeightsList = getAllMaxSubsetsWeight(weightsArray, weights.size(), maxSum);
        log.info("max weigts list {}", maxWeightsList);
        return maxWeightsList.size() > 0 ? sortListByValue(maxWeightsList).get(0) : new ArrayList<>();
    }

    private void getMaxSubsetsRec(int[] arr, int i, int sum, List<Integer> p) {
        if (i == 0 && sum != 0 && maxWeightEstimationArray[0][sum]) {
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

        if (maxWeightEstimationArray[i-1][sum]) {
            ArrayList<Integer> b = new ArrayList<>(p);
            getMaxSubsetsRec(arr, i-1, sum, b);
        }

        if (sum >= arr[i] && maxWeightEstimationArray[i-1][sum-arr[i]]) {
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

        maxWeightEstimationArray = new boolean[n][sum + 1];
        for (int i=0; i<n; ++i) {
            maxWeightEstimationArray[i][0] = true;
        }

        if (arr[0] <= sum) maxWeightEstimationArray[0][arr[0]] = true;

        for (int i = 1; i < n; ++i) {
            for (int j = 0; j < sum + 1; ++j) {
                maxWeightEstimationArray[i][j] = (arr[i] <= j) ? (maxWeightEstimationArray[i - 1][j] || maxWeightEstimationArray[i - 1][j - arr[i]]) : maxWeightEstimationArray[i - 1][j];
            }
        }

        int index_i = 0, index_j = 0;
        for (int i = maxWeightEstimationArray.length - 1; i >= 0; i--) {
            for (int j = maxWeightEstimationArray[i].length - 1; j >= 0; j--) {
                if (maxWeightEstimationArray[i][j]) {
                    index_i = i;
                    index_j = j;
                    break;
                }
            }
            if(index_i != 0 || index_j != 0) {
                break;
            }
        }

        if(!maxWeightEstimationArray[index_i][index_j]) {
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
     * @param <T> - List of integer weights
     * @return - sorted by desc list
     */
    public <T> List<? extends List<T>> sortListByValue(List<? extends List<T>> list) {
        if(list.size() == 1) return list;
        list.sort((Comparator<List<T>>) (o1, o2) -> Integer.compare(o2.size(), o1.size()));
        return list;
    }
}

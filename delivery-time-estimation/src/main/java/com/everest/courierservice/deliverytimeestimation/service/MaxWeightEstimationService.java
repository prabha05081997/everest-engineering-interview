package com.everest.courierservice.deliverytimeestimation.service;

import java.util.List;

public interface MaxWeightEstimationService {

    List<Integer> findMaxWeights(List<Integer> weights, int maxSum);

}

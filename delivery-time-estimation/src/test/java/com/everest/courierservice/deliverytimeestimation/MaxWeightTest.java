package com.everest.courierservice.deliverytimeestimation;

import com.everest.courierservice.deliverytimeestimation.service.MaxWeightEstimationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
public class MaxWeightTest {

    @Test
    public void findMaxWeights() {
        int maxCarriableWeightInKg = 200;
        List<Integer> unassignedWeights = List.of(50, 75, 175, 110, 155);
        List<Integer> expectedMaxWeights = List.of(110, 75);
        List<Integer> resultMaxWeights = MaxWeightEstimationService.getInstance().findMaxWeights(unassignedWeights, maxCarriableWeightInKg);
        assertEquals(expectedMaxWeights, resultMaxWeights);
    }
}

package com.everest.courierservice.deliverytimeestimation;

import com.everest.courierservice.deliverytimeestimation.service.MaxWeightEstimationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void testSortListByValue() {
        List<Integer> integerList = new ArrayList<>();
        List<List<Integer>> listOfIntegers = new ArrayList<>();
        integerList.add(150);
        integerList.add(50);
        listOfIntegers.add(integerList);
        integerList = new ArrayList<>();
        integerList.add(50);
        integerList.add(150);
        listOfIntegers.add(integerList);
        integerList = new ArrayList<>();
        integerList.add(50);
        integerList.add(50);
        integerList.add(100);
        listOfIntegers.add(integerList);
        assertEquals(List.of( List.of(50, 50, 100), List.of(150, 50), List.of(50, 150)),
                MaxWeightEstimationService.getInstance().sortListByValue(listOfIntegers));
    }
}

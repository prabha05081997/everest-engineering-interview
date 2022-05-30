package com.everest.courierservice.deliverytimeestimation;

import com.everest.courierservice.deliverytimeestimation.service.MaxWeightEstimationServiceImpl;
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
        List<Integer> resultMaxWeights = MaxWeightEstimationServiceImpl.getInstance().findMaxWeights(unassignedWeights, maxCarriableWeightInKg);
        assertEquals(expectedMaxWeights, resultMaxWeights);

        unassignedWeights = List.of(175, 110, 155);
        expectedMaxWeights = List.of(175);
        resultMaxWeights = MaxWeightEstimationServiceImpl.getInstance().findMaxWeights(unassignedWeights, maxCarriableWeightInKg);
        assertEquals(expectedMaxWeights, resultMaxWeights);
    }

    @Test
    public void testSortListByValue() {
        List<List<Integer>> listOfIntegers = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>(List.of(150, 50));
        listOfIntegers.add(integerList);
        integerList = new ArrayList<>(List.of(50, 150));
        listOfIntegers.add(integerList);
        integerList = new ArrayList<>(List.of(50, 50, 100));
        listOfIntegers.add(integerList);
        assertEquals(List.of( List.of(50, 50, 100), List.of(150, 50), List.of(50, 150)),
                MaxWeightEstimationServiceImpl.getInstance().sortListByValue(listOfIntegers));

        listOfIntegers = new ArrayList<>();
        integerList = new ArrayList<>(List.of(75, 84));
        listOfIntegers.add(integerList);
        integerList = new ArrayList<>(List.of(79, 25));
        listOfIntegers.add(integerList);
        integerList = new ArrayList<>(List.of(83, 23, 56));
        listOfIntegers.add(integerList);
        assertEquals(List.of( List.of(83, 23, 56), List.of(75, 84), List.of(79, 25)),
                MaxWeightEstimationServiceImpl.getInstance().sortListByValue(listOfIntegers));
    }
}

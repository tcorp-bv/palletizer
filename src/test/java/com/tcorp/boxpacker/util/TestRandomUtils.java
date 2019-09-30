package com.tcorp.boxpacker.util;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestRandomUtils {
    @Test
    public void getRandomPermutationSameSizeAsInput(){
        int n = 200;
        Integer[] permutedArr = RandomUtils.getRandomPermutation(n);
        assertEquals(n, permutedArr.length);
    }
    @Test
    public void getRandomPermutationAllUniques(){
        int n = 200;
        Integer[] permutedArr = RandomUtils.getRandomPermutation(n);
        List<Integer> permuted = new ArrayList<Integer>(permutedArr.length);
        for(int e: permutedArr)
            permuted.add(e);
        Map<Integer, Long> counts = permuted.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        assertEquals(counts.size(), n);
    }

}

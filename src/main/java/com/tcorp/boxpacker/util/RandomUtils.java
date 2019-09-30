package com.tcorp.boxpacker.util;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.*;

public class RandomUtils {
    private static final Random r = new Random();
    public static Integer[] getRandomPermutation (int length){
        // initialize array and fill it with {0,1,2...}
        Integer[] array = new Integer[length];
        for(int i = 0; i < array.length; i++)
            array[i] = i;

        for(int i = 0; i < length; i++){

            // randomly chosen position in array whose element
            // will be swapped with the element in position i
            // note that when i = 0, any position can chosen (0 thru length-1)
            // when i = 1, only positions 1 through length -1
            // NOTE: r is an instance of java.util.Random
            int ran = i + r.nextInt (length-i);

            // perform swap
            int temp = array[i];
            array[i] = array[ran];
            array[ran] = temp;
        }
        return array;
    }

    /**
     * NOT EFFICIENT WHEN SAMPLESIZE -> ITEMSSIZE
     * @param length
     * @param sampleSize
     * @return
     */
    public static List<Integer> getPermutationSample (int length, int sampleSize){
        if(sampleSize > length)
            throw new RuntimeException("Sample size should never be larger then item size!");
        List<Integer> result = new ArrayList<>(length);
        Set<Integer> sampleSet = new HashSet<>();

        for(int i = 0; i < sampleSize; i++) {
            int randomElement = -1;
            do {
                randomElement = r.nextInt(length);
            } while (sampleSet.contains(randomElement));
            result.add(randomElement);
            sampleSet.add(randomElement);
        }
       return result;
    }
    public static double getRandomWithMean(RandomGenerator r, double average){
        double beta = 1;
        double alpha = beta * average / (1 - average);
        BetaDistribution betaDistribution = new BetaDistribution(r, alpha, beta);
        return betaDistribution.sample();
    }
}

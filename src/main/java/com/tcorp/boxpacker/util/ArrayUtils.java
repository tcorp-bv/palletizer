package com.tcorp.boxpacker.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtils {
    public static <T> T[] concatenate(T[]... arrays) {
        Integer[] lengths = Arrays.stream(arrays).map(array -> array.length).toArray(Integer[]::new);
        int totalLength = Arrays.stream(lengths).reduce(0, Integer::sum);

        @SuppressWarnings("unchecked")
        T[] concatenated = (T[]) Array.newInstance(arrays[0].getClass().getComponentType(), totalLength);
        int destPos = 0;
        for (int i = 0; i < arrays.length; i++) {
            System.arraycopy(arrays[i], 0, concatenated, destPos, lengths[i]);
            destPos += lengths[i];
        }
        return concatenated;
    }
}

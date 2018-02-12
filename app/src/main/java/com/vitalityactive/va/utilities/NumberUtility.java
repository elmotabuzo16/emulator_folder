package com.vitalityactive.va.utilities;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by peter.ian.t.betos on 02/02/2018.
 */

public class NumberUtility {


    public static int[] convertToUniqueIntArray(int[] initValue) {
        Set<Integer> numbersSet = new HashSet<>();

        for (int num : initValue) {
            numbersSet.add(num);
        }

        int[] finalArray = new int[numbersSet.size()];

        int index = 0;

        for (Integer i : numbersSet) {
            finalArray[index++] = i;
        }

        return finalArray;
    }
}

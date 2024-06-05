package org.example.dev;

import java.util.ArrayList;
import java.util.Collections;

public class MedianFinder {
    public static double findMedian(ArrayList<Integer> list) {
        Collections.sort(list);
        int size = list.size();
        if (size % 2 == 1) {
            return list.get(size / 2);
        } else {
            return (list.get(size / 2 - 1) + list.get(size / 2)) / 2.0;
        }
    }
}

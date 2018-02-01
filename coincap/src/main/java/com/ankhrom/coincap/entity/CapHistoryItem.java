package com.ankhrom.coincap.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R' on 2/1/2018.
 */

public class CapHistoryItem extends ArrayList<List<Double>> {

    public void iterate(Iterator iterator) {

        iterator.iterate(this);
    }

    public static abstract class Iterator {

        protected abstract void onNext(double timestamp, double value);

        public void iterate(CapHistoryItem items) {

            for (List<Double> item : items) {
                onNext(item.get(0), item.get(1));
            }
        }
    }
}

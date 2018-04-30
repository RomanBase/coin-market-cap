package com.ankhrom.base.common.statics;

import android.support.annotation.NonNull;

import com.ankhrom.base.interfaces.ObjectConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ObjectHelper {

    public static boolean equals(Object o, Object t) {

        return (o == null) ? (t == null) : o.equals(t);
    }

    @NonNull
    public static <T, E> List<T> convert(List<E> items, ObjectConverter<T, E> converter) {

        if (items == null) {
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>(items.size());

        for (E item : items) {
            T obj = converter.convert(item);
            if (obj != null) {
                list.add(obj);
            }
        }

        return list;
    }

    public static <T, E> List<T> sortThenConvert(Comparator<E> comparator, List<E> items, ObjectConverter<T, E> converter) {

        Collections.sort(items, comparator);

        List<T> list = new ArrayList<>(items.size());

        for (E item : items) {
            T obj = converter.convert(item);
            if (obj != null) {
                list.add(obj);
            }
        }

        return list;
    }
}

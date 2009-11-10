package org.joda.time.gwt.util;



public class Arrays {
    private Arrays() {
    }

    public static int[] copyOf(int[] original, int newLength) {
        int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }

    public static void copy(Object[] original, Object[] dest) {
        //Cannot use copyOf similar to the one in java.util.Arrays; cannot generically create an array in GWT
        //Also use Object[] iso T[] because joda supports 1.3
        System.arraycopy(original, 0, dest, 0,
                         Math.min(original.length, dest.length));
    }

}

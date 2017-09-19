package com.mw.components.map.circle.utils;

import java.util.Random;

/**
 * Created by yuli.he on 2017/9/11.
 */

public class RandomUtils {

    private long seed = 0;
    private static RandomUtils instance = null;
    private Random random;

    public static RandomUtils getInstance() {
        if (instance == null) {
            instance = new RandomUtils();
        }
        return instance;
    }

    private static Random getRandom() {
        return RandomUtils.getInstance().random;
    }

    public static int nextInt(int max) {
        return getRandom().nextInt(max);
    }


    /**
     * Gets a random int between min and max (half-inclusive).
     */
    public static int nextInt(int min, int max) {
        return nextInt(max - min) + min;
    }

    /**
     * Gets a random int between 0 and max (inclusive).
     */
    public static int nextIntInclude(int max) {
        return getRandom().nextInt(max + 1);
    }

    /**
     * Gets a random int between min and max (inclusive).
     */
    public static int nextIntInclude(int min, int max) {
        return nextIntInclude(max - min) + min;
    }

    private RandomUtils() {
        random = new Random();
        random.setSeed(seed);
    }

    public static long getSeed() {
        return RandomUtils.getInstance().seed;
    }

    public static void setSeed(long seed) {
        RandomUtils.getInstance().seed = seed;
        RandomUtils.getInstance().random.setSeed(seed);
    }
}

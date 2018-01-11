package com.cs2105.swump.core.generator;

import java.util.Random;

public class RandomGenerator
{
    static Random random = new Random();

    public static int getRandomValue()
    {
        int value = random.nextInt(9) + 1;
        return value;
    }

    public static int getRandomRow()
    {
        int numRow = random.nextInt(9);
        return numRow;
    }

    public static int getRandomCol()
    {
        int numCol = random.nextInt(9);
        return numCol;
    }

    public static int getRandomPowerUpVal()
    {
        int numPowerUpID = random.nextInt(4);
        return numPowerUpID;
    }
}

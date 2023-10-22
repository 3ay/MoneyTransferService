package ru.netology.util;

import java.util.concurrent.atomic.AtomicLong;

public class UniqueIdGenerator {
    private static AtomicLong uniqueId = new AtomicLong(System.currentTimeMillis() * 1000);

    public static long generateUniqueId() {
        long result = uniqueId.getAndIncrement();
        if (result % 1000000000000000000L > 0)
        {
            return  result / 10;
        }
        else
            return result;
    }
    public static long generateUniqueOperationId() {
        long result = uniqueId.getAndAdd(15);
        if (result % 1000000000000000000L > 0)
        {
            return  result / 10;
        }
        else
            return result;
    }
}

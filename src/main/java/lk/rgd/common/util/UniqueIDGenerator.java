package lk.rgd.common.util;

/**
 * @author Mahesha
 */
public class UniqueIDGenerator {
    static long current = System.currentTimeMillis();

    static public synchronized long get() {
        return current++;
    }
}

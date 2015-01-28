package at.spengergasse.moe15300.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Timer {
    private static Map<Object, Date> checkpoints = new HashMap<>();

    private Timer() {
        // no instantiation allowed
    }

    public static void start(Object id) {
        checkpoints.put(id, new Date());
    }

    public static long getTime(Object id) {
        return new Date().getTime() - checkpoints.get(id).getTime();
    }
}

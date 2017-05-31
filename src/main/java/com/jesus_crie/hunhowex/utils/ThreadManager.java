package com.jesus_crie.hunhowex.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadManager {

    private static List<Timer> timers = new ArrayList<>();

    public static void startTimer(Runnable action, long delay) {
        timers.add(new Timer());
        timers.get(timers.size() - 1).schedule(new TimerTask() {
            @Override
            public void run() {
                action.run();
            }
        }, delay, delay);
    }

    public static void cancelAll() {
        timers.forEach(t -> t.cancel());
    }
}

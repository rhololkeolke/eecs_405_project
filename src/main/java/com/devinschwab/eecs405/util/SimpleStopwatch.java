package com.devinschwab.eecs405.util;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by Devin on 4/15/15.
 */
public class SimpleStopwatch {

    private Instant startTime = null;
    private Instant stopTime = null;

    public void start() {
        startTime = Instant.now();
    }

    public void stop() {
        stopTime = Instant.now();
    }

    public void reset() {
        startTime = null;
        stopTime = null;
    }

    public boolean isRunning() {
        return startTime != null && stopTime == null;
    }

    @Override
    public String toString() {
        if (startTime == null) {
            return "0";
        }

        if (stopTime == null) {
            return Duration.between(startTime, Instant.now()).toString();
        }

        return Duration.between(startTime, stopTime).toString();
    }
}

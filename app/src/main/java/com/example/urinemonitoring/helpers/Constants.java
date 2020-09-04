package com.example.urinemonitoring.helpers;

import com.example.urinemonitoring.models.Option;

public class Constants {
    public static final int[] intervalMillis = {
            10 * 1000,      // 10 seconds
            30 * 1000,      // 30 seconds
            60 * 1000,      // 1 minute
            5 * 60 * 1000,  // 5 minutes
            30 * 60 * 1000, // 30 minutes
            60 * 60 * 1000, // 1 hour
    };

    public static Option defaultOption = new Option("1", 0, "https://72ea3862-f049-4cfe-b4fc-02d93e59efaf.mock.pstmn.io/json", "data");
}

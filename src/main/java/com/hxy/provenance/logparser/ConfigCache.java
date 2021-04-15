package com.hxy.provenance.logparser;

public class ConfigCache {
    private static int JobConfig = 0;

    public static void update(int config) {
        JobConfig = config;
    }

    public static int getJobConfig(){
        return JobConfig;
    }
}

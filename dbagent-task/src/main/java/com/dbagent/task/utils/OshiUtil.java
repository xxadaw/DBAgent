package com.dbagent.task.utils;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Classname OshiUtil
 * @Description TODO
 * @Date 2025/8/12 23:54
 * @Created by xxx
 */
@Slf4j
public class OshiUtil {
    public static double getCpuLoad() {
        SystemInfo si = new SystemInfo();
        CentralProcessor processor = si.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error("获取CPU负载失败", e);
        }
        return processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
    }
    public static double getMemoryUsage() {
        SystemInfo si = new SystemInfo();
        GlobalMemory memory = si.getHardware().getMemory();
        double totalMemory = memory.getTotal();
        double availableMemory = memory.getAvailable();
        return (totalMemory - availableMemory) / totalMemory * 100;
    }
}

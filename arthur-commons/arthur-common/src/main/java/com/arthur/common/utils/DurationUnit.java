package com.arthur.common.utils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Java Duration解析工具类
 * <p>
 * 解析字符串支持枚举定义的类型，比如：10s、15h
 *
 * @author DearYang
 * @date 2022-08-18
 * @since 1.0
 */
@SuppressWarnings("unused")
public enum DurationUnit {

    NANOS("ns,nano,nanos,nanosecond,nanoseconds", ChronoUnit.NANOS),
    MICROS("us,micro,micros,microsecond,microseconds", ChronoUnit.MICROS),
    MILLIS("ms,milli,millis,millsecond,milliseconds", ChronoUnit.MILLIS),
    SECONDS("s,second,seconds", ChronoUnit.SECONDS),
    MINUTES("m,minute,minutes", ChronoUnit.MINUTES),
    HOURS("h,hour,hours", ChronoUnit.HOURS),
    DAYS("d,day,days", ChronoUnit.DAYS);

    private final String[] descriptions;
    private final ChronoUnit timeUnit;

    DurationUnit(String descriptions, ChronoUnit timeUnit) {
        this.descriptions = descriptions.split(",");
        this.timeUnit = timeUnit;
    }

    ChronoUnit getTimeUnit() {
        return this.timeUnit;
    }

    public static Duration getDuration(String time) {
        String value = time.trim();
        TemporalUnit temporalUnit = ChronoUnit.MILLIS;
        long timeVal = 0;
        for (DurationUnit unit : values()) {
            for (String suffix : unit.descriptions) {
                if (value.endsWith(suffix)) {
                    temporalUnit = unit.timeUnit;
                    timeVal = Long.parseLong(value.substring(0, value.length() - suffix.length()));
                }
            }
        }
        return Duration.of(timeVal, temporalUnit);
    }

}

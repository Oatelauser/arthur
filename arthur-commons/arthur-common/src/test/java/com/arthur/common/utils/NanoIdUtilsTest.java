package com.arthur.common.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

class NanoIdUtilsTest {

    @Test
    public void t1() {
        for (int i = 0; i < 100; i++) {
            System.out.println(NanoIdUtils.randomNanoId(ThreadLocalRandom.current()));
        }
    }

}

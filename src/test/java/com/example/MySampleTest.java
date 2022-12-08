package com.example;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class MySampleTest {

    @Test
    public void testNativeImage(){
        assertThat("hi").isEqualTo("hi");
    }
}

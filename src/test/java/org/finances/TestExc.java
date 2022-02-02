package org.finances;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestExc {

    @Test
    public void testExcercise_Success() {
        assertEquals(Exc.getPi(), Math.PI);
    }
    
}

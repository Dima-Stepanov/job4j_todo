package ru.job4j.todo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test trigger from codecov.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
public class TriggerTest {

    @Test
    public void getInt() {
        Trigger trigger = new Trigger();
        int result = trigger.getInt();
        int expected = 1;
        assertEquals(result, expected);
    }
}
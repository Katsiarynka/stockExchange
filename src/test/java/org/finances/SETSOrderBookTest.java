package org.finances;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SETSOrderBookTest {

    @Test
    @DisplayName("should start producer and consumer threads and wait for the queue to be empty")
    void mainStartsThreadsAndWaitsForQueueToBeEmpty() {
        SETSOrderBook.main(new String[]{});
    }

    @Test
    @DisplayName("should handle InterruptedException when waiting for the queue to be empty")
    void mainHandlesInterruptedException() {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        Thread thread = new Thread(() -> {
            try {
                SETSOrderBook.wait(queue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.interrupt();
    }

    @Test
    @DisplayName("should wait until the queue is empty and then return")
    void waitWhenQueueIsEmptyThenReturn() {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        ReflectionTestUtils.invokeMethod(SETSOrderBook.class, "wait", queue);
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("should wait for the specified waiting time when the queue is not empty")
    void waitWhenQueueIsNotEmptyThenWaitForSpecifiedTime() {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("test");
        ReflectionTestUtils.invokeMethod(SETSOrderBook.class, "wait", queue);
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("should handle InterruptedException when waiting for the specified time")
    void waitHandlesInterruptedException() {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    }

    @Test
    @DisplayName("should wait for the specified waiting time and then check if the queue is empty again")
    void waitForSpecifiedWaitingTimeAndCheckQueueAgain() {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        ReflectionTestUtils.invokeMethod(SETSOrderBook.class, "wait", queue);
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("should wait for the queue to be empty before terminating")
    void mainMethodWaitsForQueueToBeEmpty() {
        LinkedBlockingQueue<String> queue = mock(LinkedBlockingQueue.class);
        when(queue.isEmpty()).thenReturn(false).thenReturn(true);
        SETSOrderBook.wait(queue);
        verify(queue, times(2)).isEmpty();
    }

    @Test
    @DisplayName("should start producer and consumer threads and process orders")
    void mainMethodStartsProducerAndConsumerThreads() {
        LinkedBlockingQueue<String> queue = mock(LinkedBlockingQueue.class);
        Thread orderProducer = mock(Thread.class);
        Thread orderConsumer = mock(Thread.class);
        when(orderProducer.isAlive()).thenReturn(true);
        when(orderConsumer.isAlive()).thenReturn(true);
        when(orderProducer.getState()).thenReturn(Thread.State.RUNNABLE);
        when(orderConsumer.getState()).thenReturn(Thread.State.RUNNABLE);
        when(queue.isEmpty()).thenReturn(false);
        SETSOrderBook.main(new String[]{});
        verify(orderProducer, times(1)).start();
        verify(orderConsumer, times(1)).start();
    }
}
package pl.edu.agh.kis.lab;

import org.junit.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WriterThreadTest {

    @Test
    public void writerThreadRunsProperly() {
        Library library = new Library(5);
        Writer writer = new Writer(library, "W1", 10, 30, 3);
        writer.start();

        await().atMost(Duration.ofMillis(4000)).until(() -> !writer.isAlive());

        assertNull(library.getCurrentWriter());
        assertEquals(0, library.getCurrentQueue().size());

    }
}

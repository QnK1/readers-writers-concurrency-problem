package pl.edu.agh.kis.lab;

import org.junit.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReaderThreadTest {

    @Test
    public void readerThreadRunsProperly() {
        Library library = new Library(2);
        Reader r1 = new Reader(library, "R1", 10, 30, 3);
        Reader r2 = new Reader(library, "R2", 10, 30, 3);
        Reader r3 = new Reader(library, "R3", 10, 30, 3);

        r1.start();
        r2.start();
        r3.start();

        await().atMost(Duration.ofMillis(4000)).until(() -> !r1.isAlive() && !r2.isAlive() && !r3.isAlive());

        assertEquals(0, library.getCurrentReaders().size());
        assertEquals(0, library.getCurrentQueue().size());
        assertNull(library.getCurrentWriter());

    }
}

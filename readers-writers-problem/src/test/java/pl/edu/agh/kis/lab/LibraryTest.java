package pl.edu.agh.kis.lab;

import org.junit.Test;

import static org.junit.Assert.*;

public class LibraryTest {

    @Test
    public void libraryStateAfterReadRequestIsCorrect() {
        Library library = new Library(5);
        Reader reader = new Reader(library, "R1", 1000, 3000);

        library.requestRead(reader);
        assertEquals(1, library.getCurrentReaders().size());
        assertTrue(library.getCurrentReaders().contains(reader));

        library.finishRead(reader);
        assertEquals(0, library.getCurrentReaders().size());
        assertFalse(library.getCurrentReaders().contains(reader));
    }

    @Test
    public void libraryStateAfterWriteRequestIsCorrect() {
        Library library = new Library(5);
        Writer writer = new Writer(library, "W1", 1000, 3000);

        library.requestWrite(writer);
        library.acquireInfoLock();
        assertEquals(0, library.getCurrentReaders().size());
        assertEquals(writer, library.getCurrentWriter());
        assertEquals(0, library.getCurrentQueue().size());
        library.releaseInfoLock();

        library.finishWrite(writer);
        library.acquireInfoLock();
        assertEquals(0, library.getCurrentReaders().size());
        assertNull(library.getCurrentWriter());
        assertEquals(0, library.getCurrentQueue().size());
        library.releaseInfoLock();
    }

    @Test
    public void incorrectReadRequestIsHandledProperly() {
        Library library = new Library(5);
        Reader reader = new Reader(library, "R1", 1000, 3000);

        library.requestRead(reader);
        library.requestRead(reader);
        library.acquireInfoLock();
        assertEquals(1, library.getCurrentReaders().size());
        library.releaseInfoLock();
        library.finishRead(reader);

        library.finishRead(reader);
        library.acquireInfoLock();
        assertEquals(0, library.getCurrentReaders().size());
        library.releaseInfoLock();
    }

    @Test
    public void incorrectWriteRequestIsHandledProperly() {
        Library library = new Library(5);
        Writer writer = new Writer(library, "W1", 1000, 3000);

        library.requestWrite(writer);
        library.requestWrite(writer);

        library.acquireInfoLock();
        assertEquals(writer, library.getCurrentWriter());
        library.releaseInfoLock();

        library.finishWrite(writer);
        library.finishWrite(writer);

        library.acquireInfoLock();
        assertEquals(0, library.getCurrentReaders().size());
        assertNull(library.getCurrentWriter());
        library.releaseInfoLock();
    }
}

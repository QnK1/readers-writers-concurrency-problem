package pl.edu.agh.kis.lab;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LibraryInfoManagerTest {

    @Test
    public void libraryInfoIsConsistentWithLibraryState() {
        Library library = new Library(5);
        LibraryInfoManager libraryInfoManager = new LibraryInfoManager(library);
        Reader r1 = new Reader(library, "R1", 1000, 3000);
        Reader r2 = new Reader(library, "R2", 1000, 3000);

        library.requestRead(r1);
        library.requestRead(r2);

        library.acquireInfoLock();
        String info = libraryInfoManager.getLibraryInfo();

        assertTrue(info.contains("R1"));
        assertTrue(info.contains("R2"));
        assertTrue(info.contains("Current queue size: 0"));
        assertTrue(info.contains("None"));

        library.releaseInfoLock();
        library.finishRead(r1);
        library.finishRead(r2);
    }
}

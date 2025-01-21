package pl.edu.agh.kis.lab;

import lombok.Getter;

import java.util.Random;

/**
 * Represents a reader in the library system who can perform reading operations.
 * A reader runs in its own thread and interacts with the library to request and finish reading.
 */
@Getter
public class Reader extends Thread {
    private final Library library;
    private final String readerName;
    private final Random random;
    private final int nOfIterations;
    private final LibraryInfoManager libraryInfoManager;
    private final int minTime;
    private final int maxTime;

    /**
     * Constructs a Reader instance with the specified library, reader ID, and time bounds.
     *
     * @param library  the library this reader interacts with
     * @param id       the unique name or identifier of the reader
     * @param minTime  the minimum time (in milliseconds) the reader spends reading or waiting
     * @param maxTime  the maximum time (in milliseconds) the reader spends reading or waiting
     */
    public Reader(Library library, String id, int minTime, int maxTime) {
        this.library = library;
        this.readerName = id;
        this.random = new Random();
        this.nOfIterations = 0;
        this.minTime = minTime;
        this.maxTime = maxTime;
        libraryInfoManager = new LibraryInfoManager(library);
    }

    /**
     * Constructs a Reader instance with the specified library, reader ID, time bounds, and iteration count.
     *
     * @param library    the library this reader interacts with
     * @param id         the unique name or identifier of the reader
     * @param minTime    the minimum time (in milliseconds) the reader spends reading or waiting
     * @param maxTime    the maximum time (in milliseconds) the reader spends reading or waiting
     * @param iterations the number of iterations the reader performs; 0 for infinite iterations
     */
    public Reader(Library library, String id, int minTime, int maxTime, int iterations) {
        this.library = library;
        this.readerName = id;
        this.random = new Random();
        this.nOfIterations = Math.max(iterations, 0);
        this.minTime = minTime;
        this.maxTime = maxTime;
        libraryInfoManager = new LibraryInfoManager(library);
    }

    /**
     * Executes the reading process for the reader.
     * The reader alternates between waiting, requesting to read, and finishing reading,
     * according to the specified time bounds and number of iterations.
     */
    @Override
    public void run() {
        boolean condition = true;
        int i = 0;

        while (condition) {
            try {
                Thread.sleep(random.nextInt(maxTime - minTime) + (long) minTime);

                library.acquireInfoLock();
                System.out.println("\n" + readerName + " wants to read");
                System.out.print(libraryInfoManager.getLibraryInfo());
                System.out.println();
                library.releaseInfoLock();

                library.requestRead(this);

                Thread.sleep(random.nextInt(maxTime - minTime) + (long) minTime);

                library.acquireInfoLock();
                System.out.println("\n" + readerName + " has finished reading");
                System.out.print(libraryInfoManager.getLibraryInfo());
                System.out.println();
                library.releaseInfoLock();

                library.finishRead(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ReaderThreadInterruptedException(e);
            }

            if (nOfIterations != 0) {
                ++i;

                if (i == nOfIterations) {
                    condition = false;
                }
            }
        }
    }
}
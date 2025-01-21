package pl.edu.agh.kis.lab;

import lombok.Getter;

import java.util.Random;

/**
 * Represents a writer in the library system who can perform writing operations.
 * A writer runs in its own thread and interacts with the library to request and finish writing.
 */
@Getter
public class Writer extends Thread {
    private final Library library;
    private final String writerName;
    private final Random random;
    private final int nOfIterations;
    private final LibraryInfoManager libraryInfoManager;
    private final int minTime;
    private final int maxTime;

    /**
     * Constructs a Writer instance with the specified library, writer ID, and time bounds.
     *
     * @param library  the library this writer interacts with
     * @param id       the unique name or identifier of the writer
     * @param minTime  the minimum time (in milliseconds) the writer spends writing or waiting
     * @param maxTime  the maximum time (in milliseconds) the writer spends writing or waiting
     */
    public Writer(Library library, String id, int minTime, int maxTime) {
        this.library = library;
        this.writerName = id;
        this.random = new Random();
        this.nOfIterations = 0;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.libraryInfoManager = new LibraryInfoManager(library);
    }

    /**
     * Constructs a Writer instance with the specified library, writer ID, time bounds, and iteration count.
     *
     * @param library    the library this writer interacts with
     * @param id         the unique name or identifier of the writer
     * @param minTime    the minimum time (in milliseconds) the writer spends writing or waiting
     * @param maxTime    the maximum time (in milliseconds) the writer spends writing or waiting
     * @param iterations the number of iterations the writer performs; 0 for infinite iterations
     */
    public Writer(Library library, String id, int minTime, int maxTime, int iterations) {
        this.library = library;
        this.writerName = id;
        this.random = new Random();
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.nOfIterations = Math.max(iterations, 0);
        this.libraryInfoManager = new LibraryInfoManager(library);
    }

    /**
     * Executes the writing process for the writer.
     * The writer alternates between waiting, requesting to write, and finishing writing,
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
                System.out.println("\n" + writerName + " wants to write");
                System.out.print(libraryInfoManager.getLibraryInfo());
                System.out.println();
                library.releaseInfoLock();

                library.requestWrite(this);

                Thread.sleep(random.nextInt(maxTime - minTime) + (long) minTime);

                library.acquireInfoLock();
                System.out.println("\n" + writerName + " has finished writing");
                System.out.print(libraryInfoManager.getLibraryInfo());
                System.out.println();
                library.releaseInfoLock();

                library.finishWrite(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new WriterThreadInterruptedException(e);
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

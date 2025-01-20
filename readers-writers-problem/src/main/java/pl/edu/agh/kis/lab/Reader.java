package pl.edu.agh.kis.lab;

import lombok.Getter;

import java.util.Random;

@Getter
public class Reader extends Thread{
    private final Library library;
    private final String readerName;
    private final Random random;

    public Reader(Library library, String id) {
        this.library = library;
        this.readerName = id;
        this.random = new Random();
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(random.nextInt(2000) + (long) 1000);

                System.out.println("\nI want to read " + readerName);
                System.out.print(library.getCurrentInfo());
                System.out.println();

                library.requestRead(this);

                Thread.sleep(random.nextInt(2000) + (long) 1000);

                System.out.println("\nI have finished reading " + readerName);
                System.out.print(library.getCurrentInfo());
                System.out.println();

                library.finishRead(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ReaderThreadInterruptedException(e);
            }
        }
    }
}

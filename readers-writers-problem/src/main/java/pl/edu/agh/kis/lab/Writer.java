package pl.edu.agh.kis.lab;

import lombok.Getter;

import java.util.Random;

@Getter
public class Writer extends Thread{
    private final Library library;
    private final String writerName;
    private final Random random;

    public Writer(Library library, String id) {
        this.library = library;
        this.writerName = id;
        this.random = new Random();
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(random.nextInt(2000) + (long) 1000);

                System.out.println("\nI want to write " + writerName);
                System.out.print(library.getCurrentInfo());
                System.out.println();

                library.requestWrite(this);

                Thread.sleep(random.nextInt(2000) + (long) 1000);

                System.out.println("\nI have finished writing " + writerName);
                System.out.print(library.getCurrentInfo());
                System.out.println();

                library.finishWrite(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new WriterThreadInterruptedException(e);
            }


        }
    }
}

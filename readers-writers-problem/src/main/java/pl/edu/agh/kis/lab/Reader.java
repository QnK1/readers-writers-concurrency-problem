package pl.edu.agh.kis.lab;

import java.util.Random;

public class Reader extends Thread{
    private final Library library;
    private final int id;

    public Reader(Library library, int id) {
        this.library = library;
        this.id = id;
    }

    @Override
    public void run() {
        Random random = new Random();

        while(true){
            System.out.println("Chce czytac " + id);
            library.requestRead(this);
            try{
                Thread.sleep(random.nextInt(2000) + 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Koncze czytac " + id);
            library.finishRead(this);
            System.out.println(library.getNumberOfReaders() + " " + library.isWriting());
        }
    }
}

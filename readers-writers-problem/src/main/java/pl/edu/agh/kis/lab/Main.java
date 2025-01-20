package pl.edu.agh.kis.lab;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<Reader> readers = new ArrayList<>();
        List<Writer> writers = new ArrayList<>();

        final int nReaders = 10;
        final int nWriters = 3;

        Library library = new Library(5);

        for(int i = 0; i < nReaders; i++){
            readers.add(new Reader(library, i));
            readers.get(i).start();
        }

        for(int i = 0; i < nWriters; i++){
            writers.add(new Writer(library, i));
            writers.get(i).start();
        }
    }
}
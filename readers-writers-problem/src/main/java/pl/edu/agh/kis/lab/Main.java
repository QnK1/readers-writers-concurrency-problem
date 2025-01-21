package pl.edu.agh.kis.lab;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if(args.length != 3 || Integer.parseInt(args[0]) <= 0
                || Integer.parseInt(args[1]) <= 0 || Integer.parseInt(args[2]) <= 0) {
            System.out.println("Usage: java -jar <filename> <n-of-readers> <n-of-writers> <library-capacity>");
            return;
        }

        int nReaders = Integer.parseInt(args[0]);
        int nWriters = Integer.parseInt(args[1]);
        int capacity = Integer.parseInt(args[2]);

        List<Reader> readers = new ArrayList<>();
        List<Writer> writers = new ArrayList<>();

        Library library = new Library(capacity);

        for(int i = 0; i < nReaders; i++){
            readers.add(new Reader(library, "R" + i, 1000, 3000));
            readers.get(i).start();
        }

        for(int i = 0; i < nWriters; i++){
            writers.add(new Writer(library, "W" + i, 1000, 3000));
            writers.get(i).start();
        }
    }
}
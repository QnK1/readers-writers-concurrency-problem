package pl.edu.agh.kis.lab;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Reader> readers = new ArrayList<>();
        List<Writer> writers = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of reader threads: ");
        int nReaders = scanner.nextInt();
        System.out.println("Enter number of writer threads: ");
        int nWriters = scanner.nextInt();
        System.out.println("Enter library capacity (for readers): ");
        int capacity = scanner.nextInt();

        Library library = new Library(capacity);

        for(int i = 0; i < nReaders; i++){
            readers.add(new Reader(library, "R" + i));
            readers.get(i).start();
        }

        for(int i = 0; i < nWriters; i++){
            writers.add(new Writer(library, "W" + i));
            writers.get(i).start();
        }
    }
}
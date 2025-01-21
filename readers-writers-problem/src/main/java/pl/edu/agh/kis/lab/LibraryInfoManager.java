package pl.edu.agh.kis.lab;

/**
 * Manages and provides information about the current state of the library.
 */
public class LibraryInfoManager {
    private final Library library;

    /**
     * Constructs a LibraryInfoManager instance with the specified library.
     *
     * @param lib the library to manage information for
     */
    public LibraryInfoManager(Library lib) {
        library = lib;
    }

    /**
     * Retrieves a summary of the current state of the library.
     *
     * @return a string containing information about the current readers, writer, and queue in the library
     */
    public String getLibraryInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current readers count: ").append(library.getCurrentReaders().size()).append("\n");
        sb.append("Current readers: ");
        for (Reader reader : library.getCurrentReaders()) {
            sb.append(reader.getReaderName()).append(" ");
        }
        sb.append("\n");
        sb.append("Current writer: ").append(library.getCurrentWriter() == null ? "None" : library.getCurrentWriter().getWriterName());
        sb.append("\n");
        sb.append("Current queue size: ").append(library.getCurrentQueue().size()).append("\n");
        sb.append("Current queue: ");
        for (String s : library.getCurrentQueue()) {
            sb.append(s).append(" ");
        }

        return sb.toString();
    }
}

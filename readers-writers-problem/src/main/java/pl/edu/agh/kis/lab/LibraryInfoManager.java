package pl.edu.agh.kis.lab;

public class LibraryInfoManager {
    private final Library library;

    public LibraryInfoManager(Library lib) {
        library = lib;
    }

    public String getLibraryInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("Current readers count: ").append(library.getCurrentReaders().size()).append("\n");
        sb.append("Current readers: ");
        for(Reader reader : library.getCurrentReaders()){
            sb.append(reader.getReaderName()).append(" ");
        }
        sb.append("\n");
        sb.append("Current writer: ").append(library.getCurrentWriter() == null ? "None" : library.getCurrentWriter().getWriterName());
        sb.append("\n");
        sb.append("Current queue size: ").append(library.getCurrentQueue().size()).append("\n");
        sb.append("Current queue: ");
        for(String s : library.getCurrentQueue()){
            sb.append(s).append(" ");
        }

        return sb.toString();
    }
}

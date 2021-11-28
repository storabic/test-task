package sorter;

import java.io.BufferedReader;

/**
 * Pair of minimum row in temporary file, produced by sorter program, and reader of this file
 *
 * @author Daniil Ivanov
 */
public class RowEntry implements Comparable<RowEntry> {
    private String rowContent;
    private BufferedReader reader;

    public RowEntry(String rowContent, BufferedReader reader) {
        this.rowContent = rowContent;
        this.reader = reader;
    }

    public String getRowContent() {
        return rowContent;
    }

    public void setRowContent(String rowContent) {
        this.rowContent = rowContent;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public int compareTo(RowEntry secondRowEntry) {
        return rowContent.compareTo(secondRowEntry.getRowContent());
    }
}

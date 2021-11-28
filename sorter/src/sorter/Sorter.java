package sorter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Java implementation of sorting algorithm, which sorts rows in a text file, that doesn't fit in CPU
 * The algorithm:
 * 1. Split rows in the given file into N smaller chunks, sort rows in each chunk and write to temporary files;
 * 2. Create priority queue of {@link RowEntry}, which contains minimum row in each chunk and chunk-file reader (The
 * order in this priority queue depends on row content {@link RowEntry#compareTo(RowEntry)});
 * 3. Using priority queue, process chunks, writing minimum of all chunks each time to output file;
 * 4. We've got file with sorted rows.
 * <p>
 * TODO ways to improve this program:
 * 1. Test that merge sort is better than other sorting algorithms for sorting chunks and rework that moment, if needed
 * 2. Dynamically set chunk size (with current chunk size, it's not guaranteed that one chunk will fit in every CPU),
 * it should probably depend on given file size?
 *
 * @author Daniil Ivanov
 */
public class Sorter {
    private static final int CHUNK_MAX_SIZE = 100000;

    public static void externalSort(File inputFile, String outputFileName) {
        try (BufferedReader inputReader = FileUtils.fileReader(inputFile)) {
            // Current chunk row's index
            int i = 0;
            int chunkNumber = 0;
            String row;
            List<String> buffer = new ArrayList<>(CHUNK_MAX_SIZE);
            while ((row = inputReader.readLine()) != null) {
                buffer.add(row);
                ++i;
                if (i >= CHUNK_MAX_SIZE) {
                    i = 0;
                    processChunk(buffer, chunkNumber);
                    ++chunkNumber;
                    buffer.clear();
                }
            }
            if (buffer.size() > 0) {
                processChunk(buffer, chunkNumber);
                ++chunkNumber;
            }

            // Priority queue, in which we'll store pair of minimum row
            // in each of temporary file and reader for this file
            PriorityQueue<RowEntry> pq = new PriorityQueue<>();

            for (int chunkIndex = 0; chunkIndex < chunkNumber; ++chunkIndex) {
                File chunkTempFile = FileUtils.file(FileUtils.CHUNK_TEMP_FILE_NAME + chunkIndex + ".txt");
                BufferedReader bufferedReader = FileUtils.fileReader(chunkTempFile);
                String firstChunkRowContent = bufferedReader.readLine();
                pq.add(new RowEntry(firstChunkRowContent, bufferedReader));
            }

            try (PrintWriter outputFileWriter = FileUtils.fileWriter(outputFileName)) {
                while (!pq.isEmpty()) {
                    RowEntry minRowEntry = pq.poll();
                    outputFileWriter.println(minRowEntry.getRowContent());
                    BufferedReader reader = minRowEntry.getReader();
                    String nextChunkRowContent = minRowEntry.getReader().readLine();
                    if (nextChunkRowContent == null) {
                        //Nothing left in file, closing reader
                        reader.close();
                    } else {
                        // Push updated entry with current min row in temp file and reader
                        minRowEntry.setRowContent(nextChunkRowContent);
                        pq.add(minRowEntry);
                    }
                }
            }

            FileUtils.cleanUp(chunkNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processChunk(List<String> buffer, int chunkCount) throws IOException {
        mergeSort(buffer);
        // Write the sorted numbers to temp file
        try (PrintWriter fileWriter = FileUtils.fileWriter(FileUtils.CHUNK_TEMP_FILE_NAME + chunkCount + ".txt")) {
            for (String s : buffer) {
                fileWriter.println(s);
            }
        }
    }

    /**
     * Implementation of mergeSort
     * We'll use merge sort, because it does less element comparison in average
     *
     * @see Sorter#merge(List, List, List) - method for merging two sorted sublists into one sorted
     */
    private static void mergeSort(List<String> buffer) {
        if (buffer.size() <= 1) {
            return;
        }
        int mid = buffer.size() / 2;
        List<String> leftHalf = new ArrayList<>(buffer.subList(0, mid));
        List<String> rightHalf = new ArrayList<>(buffer.subList(mid, buffer.size()));
        mergeSort(leftHalf);
        mergeSort(rightHalf);
        merge(leftHalf, rightHalf, buffer);
    }

    /**
     * Merge two sorted sublists into one, which will be sorted too
     */
    private static void merge(List<String> leftHalf, List<String> rightHalf, List<String> list) {
        int leftIndex = 0;
        int rightIndex = 0;
        int listIndex = 0;
        while (leftIndex < leftHalf.size() && rightIndex < rightHalf.size()) {
            if (leftHalf.get(leftIndex).compareTo(rightHalf.get(rightIndex)) <= 0) {
                list.set(listIndex++, leftHalf.get(leftIndex++));
            } else {
                list.set(listIndex++, rightHalf.get(rightIndex++));
            }
        }
        while (leftIndex < leftHalf.size()) {
            list.set(listIndex++, leftHalf.get(leftIndex++));
        }
        while (rightIndex < rightHalf.size()) {
            list.set(listIndex++, rightHalf.get(rightIndex++));
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Enter arguments <input file name> <output file name>");
        }
        String inputFileName = args[0];
        String outputFileName = args[1];
        File inputFile = FileUtils.file(inputFileName);
        externalSort(inputFile, outputFileName);
    }
}

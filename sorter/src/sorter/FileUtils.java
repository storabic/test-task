package sorter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Daniil Ivanov
 */
public class FileUtils {
    public static final String CHUNK_TEMP_FILE_NAME = "sorter-tmp-";

    private FileUtils() {
        throw new UnsupportedOperationException("Cannot instantiate util class");
    }

    public static BufferedReader fileReader(File file) throws FileNotFoundException {
        FileReader inputReader = new FileReader(file);
        return new BufferedReader(inputReader);
    }

    public static PrintWriter fileWriter(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        return new PrintWriter(fileWriter);
    }

    public static File file(String inputFileName) {
        File file = new File(inputFileName);
        if (!file.exists()) {
            throw new IllegalArgumentException("File " + inputFileName + " cannot be found");
        }
        return file;
    }

    /**
     * Delete temporary files, which were created during program exectuion
     *
     * @param chunkNumber number of chunk, which original file was divided into
     */
    public static void cleanUp(int chunkNumber) {
        for (int chunkIndex = 0; chunkIndex < chunkNumber; ++chunkIndex) {
            file(CHUNK_TEMP_FILE_NAME + chunkIndex + ".txt").delete();
        }
    }
}

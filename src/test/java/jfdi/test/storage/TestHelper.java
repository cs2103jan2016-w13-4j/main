package jfdi.test.storage;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import jfdi.storage.Constants;

public class TestHelper {

    /**
     * This method creates valid data files in directoryPath
     *
     * @param directoryPath
     *            the directory to create the data files in
     */
    public static void createValidDataFiles(String directoryPath) {
        createDataFilesWith(directoryPath, Constants.EMPTY_LIST_STRING);
    }

    /**
     * This method creates invalid data files in directoryPath.
     *
     * @param directoryPath
     *            the directory to create the data files in
     */
    public static void createInvalidDataFiles(String directoryPath) {
        createDataFilesWith(directoryPath, Constants.EMPTY_STRING);
    }

    /**
     * This method creates data files in the specified directoryPath with the
     * given data written into the data files.
     *
     * @param directoryPath
     *            the path to the directory in which we create the data files
     * @param data
     *            the content that is to be written to the data files
     */
    public static void createDataFilesWith(String directoryPath, String data) {
        File parentDirectory = new File(directoryPath);
        parentDirectory.mkdirs();

        Path dataPath;
        File dataFile;
        for (String dataFilename : Constants.ARRAY_FILENAMES) {
            dataPath = Paths.get(directoryPath, dataFilename);
            dataFile = dataPath.toFile();
            writeToFile(dataFile, data);
        }
    }

    /**
     * This method writes the given data to the given dataFile.
     *
     * @param dataFile
     *            the file in which data is to be written into
     * @param data
     *            the data which we want to write into dataFile
     */
    public static void writeToFile(File dataFile, String data) {
        try {
            PrintWriter writer = new PrintWriter(dataFile, Constants.CHARSET);
            writer.println(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

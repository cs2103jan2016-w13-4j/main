package jfdi.test.storage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import jfdi.storage.Constants;

import org.apache.commons.io.FileUtils;

public class TestHelper {

    /**
     * This method ensures that two given collections have the same elements.
     *
     * @param collection1
     *            the first collection to be compared
     * @param collection2
     *            the second collection to be compared
     * @return boolean indicating if the two collections have the same elements
     */
    public static <T> boolean hasSameElements(Collection<T> collection1, Collection<T> collection2) {
        HashSet<T> set1 = new HashSet<T>(collection1);
        HashSet<T> set2 = new HashSet<T>(collection2);
        return set1.equals(set2);
    }

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
        for (String dataFilename : Constants.FILENAME_DATA_ARRAY) {
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

    /**
     * This method checks if all the data files in sourceDirectory has been
     * faithfully copied to destinationDirectory.
     *
     * @param sourceDirectory
     *            the source directory which contains all the data files
     * @param destinationDirectory
     *            the destination directory to copy all the data files to
     * @return boolean indicating if all the data files in sourceDirectory has
     *         been copied to destinationDirectory
     */
    public static boolean hasIdenticalDataFiles(String sourceDirectory, String destinationDirectory) {
        Path destinationFilePath = null;
        File destinationFile = null;

        // Obtain a list of data files that exist in sourceDirectory
        ArrayList<File> sourceDataFiles = getDataFiles(sourceDirectory);

        // Check if an identical data file exists in destinationDirectory
        for (File sourceFile : sourceDataFiles) {
            destinationFilePath = Paths.get(destinationDirectory, sourceFile.getName());
            destinationFile = destinationFilePath.toFile();
            if (!isCopied(sourceFile, destinationFile)) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method checks if sourceFile has been copied to destinationFile with
     * the contents preserved.
     *
     * @param sourceFile
     *            the original file that is copied to destinationFile
     * @param destinationFile
     *            the copy of sourceFile
     * @return boolean indicating if destinationFile is a copy of sourceFile
     */
    public static boolean isCopied(File sourceFile, File destinationFile) {
        if (!destinationFile.exists()) {
            return false;
        }

        boolean hasEqualContent = false;
        try {
            hasEqualContent = FileUtils.contentEquals(sourceFile, destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!hasEqualContent) {
            return false;
        }

        return true;
    }

    /**
     * This method returns an ArrayList of data files that exist in the given directory.
     *
     * @param directory
     *          the directory which contains data files
     * @return an ArrayList of data files that exist in the given directory
     */
    private static ArrayList<File> getDataFiles(String directory) {
        ArrayList<File> dataFiles = new ArrayList<File>();
        Path dataFilePath = null;
        File dataFile = null;

        for (String filename : Constants.FILENAME_DATA_ARRAY) {
            dataFilePath = Paths.get(directory, filename);
            dataFile = dataFilePath.toFile();
            if (dataFile.exists()) {
                dataFiles.add(dataFile);
            }
        }

        return dataFiles;
    }
}

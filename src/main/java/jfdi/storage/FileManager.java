package jfdi.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import jfdi.storage.exceptions.ExistingFilesFoundException;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.exceptions.InvalidPathException;

/**
 * This class manages file operations required by Storage
 *
 * @author Thng Kai Yuan
 */
public class FileManager {

    /**
     * This method creates the necessary directories for files to be stored in
     * storageFolderPath. If there are issues creating the directory or if there
     * is insufficient permission to carry out normal file operations in the
     * directory, then an InvalidPathException is thrown.
     *
     * @param storageFolderPath
     *            the root directory of the data files
     * @throws InvalidPathException
     *             if there are errors creating the directory or if there is
     *             insufficient permission to carry out normal file operations
     *             in the directory
     */
    public static void prepareDirectory(String storageFolderPath) throws InvalidPathException {
        Path directoryPath = Paths.get(storageFolderPath);
        boolean isValidDirectory = true;

        try {
            directoryPath = Files.createDirectories(directoryPath);
            File directory = directoryPath.toFile();
            if (!canUseDirectory(directory)) {
                isValidDirectory = false;
            }
        } catch (IOException e) {
            isValidDirectory = false;
        }

        if (!isValidDirectory) {
            throw new InvalidPathException(storageFolderPath);
        }
    }

    /**
     * This method moves all the data files to newStorageFolderPath.
     *
     * @param newStorageFolderPath
     *            the new storage directory
     * @throws ExistingFilesFoundException
     *             if files in the new directory were replaced (with backups
     *             made)
     */
    public static void moveFilesToDirectory(String newStorageFolderPath) throws ExistingFilesFoundException {
        ArrayList<Path> filePaths = RecordManager.getAllFilePaths();
        ArrayList<FilePathPair> replacedFiles = new ArrayList<FilePathPair>();
        FilePathPair filePathPair = null;

        for (Path path : filePaths) {
            String filename = path.getFileName().toString();
            Path destination = Paths.get(newStorageFolderPath, filename);
            filePathPair = moveAndBackup(path, destination);
            if (filePathPair != null) {
                replacedFiles.add(filePathPair);
            }
        }

        if (!replacedFiles.isEmpty()) {
            throw new ExistingFilesFoundException(replacedFiles);
        }
    }

    /**
     * This method makes a backup of the file at sourcePath before removing it.
     * The absolute path of the backup file made is returned.
     *
     * @param sourcePath
     *            the path of the original file
     * @return the path of the backup file created
     */
    public static String backupAndRemove(Path sourcePath) {
        int attempts = 0;
        String originalFilename = sourcePath.getFileName().toString();
        String destinationFilename;
        File destinationFile;

        do {
            destinationFilename = originalFilename + getBackupExtension(attempts++);
            Path destinationPath = Paths.get(sourcePath.getParent().toString(), destinationFilename);
            destinationFile = destinationPath.toFile();
        } while (destinationFile.exists());

        Path destinationPath = destinationFile.toPath();
        try {
            Files.move(sourcePath, destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destinationFile.getAbsolutePath();
    }

    /**
     * This method moves the file at the given source to the given destination.
     * If a file already exists at destination, a backup file of the destination
     * is made before it is replaced.
     *
     * @param source
     *            the path of the source file
     * @param destination
     *            the path of the destination file
     * @return FilePathPair if a backup was made, null otherwise
     */
    private static FilePathPair moveAndBackup(Path source, Path destination) {
        File destinationFile = destination.toFile();
        FilePathPair filePathPair = null;

        if (destinationFile.exists()) {
            String movedTo = backupAndRemove(destination);
            filePathPair = new FilePathPair(source.toString(), movedTo);
        }

        try {
            Files.move(source, destination);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePathPair;
    }

    /**
     * This method returns the constant backup file extension concatenated with
     * the given integer i if i != 0.
     *
     * @param i the number that will be appended to the back of the extension
     * @return a backup file extension
     */
    private static String getBackupExtension(int i) {
        if (i == 0) {
            return Constants.EXTENSION_BACKUP;
        }

        return Constants.EXTENSION_BACKUP + String.valueOf(i);
    }

    /**
     * This method checks if the program has sufficient permissions to perform
     * certain file operations within the given directory.
     *
     * @param directory
     *            the root directory which files are to be stored in
     * @return a boolean indicating if the directory can be used for operations
     *         required by the program
     */
    private static boolean canUseDirectory(File directory) {
        return directory.canExecute() && directory.canWrite();
    }

    public static void writeToFile(String json, Path filePath) {
        try {
            File file = filePath.toFile();
            PrintWriter writer = new PrintWriter(file, Constants.CHARSET);
            writer.println(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFileToString(Path filePath) {
        File file = filePath.toFile();
        try {
            Scanner scanner = new Scanner(file, Constants.CHARSET);
            scanner.useDelimiter("\\Z");
            String data = scanner.next();
            scanner.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Constants.EMPTY_STRING;
        }
    }

}

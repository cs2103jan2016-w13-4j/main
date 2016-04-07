//@@author A0121621Y

package jfdi.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.exceptions.InvalidFilePathException;

/**
 * This class manages file operations required by Storage.
 *
 * @author Thng Kai Yuan
 */
public class FileManager {

    /*
     * Public APIs
     */

    /**
     * This method creates the necessary directories for files to be stored in
     * storageFolderPath. If there are issues creating the directory or if there
     * is insufficient permission to carry out normal file operations in the
     * directory, then an InvalidFilePathException is thrown.
     *
     * @param storageFolderPath
     *            the root directory of the data files
     * @throws InvalidFilePathException
     *             if there are errors creating the directory or if there is
     *             insufficient permission to carry out normal file operations
     *             in the directory
     */
    public static void prepareDirectory(String storageFolderPath) throws InvalidFilePathException {
        assert storageFolderPath != null;
        Path directoryPath = Paths.get(storageFolderPath);
        boolean isValidDirectory = true;

        // Attempt to create/load the storage directory with the right permissions
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
            String errorMessage = String.format(Constants.MESSAGE_INVALID_PATH, storageFolderPath);
            throw new InvalidFilePathException(storageFolderPath, errorMessage);
        }
    }

    /**
     * This method moves all the existing data files to newStorageFolderPath.
     *
     * @param newStorageFolderPath
     *            the new storage directory
     * @throws FilesReplacedException
     *             if files in the new directory were replaced (with backups
     *             made)
     */
    public static void moveFilesToDirectory(String newStorageFolderPath) throws FilesReplacedException {
        assert newStorageFolderPath != null;
        // Create the new folder if it doesn't already exist
        File newStorageFolder = new File(newStorageFolderPath);
        newStorageFolder.mkdirs();

        // Get the paths of all data file and move them
        ArrayList<Path> filePaths = DatabaseManager.getAllFilePaths();
        ArrayList<FilePathPair> replacedFiles = moveFilesTo(filePaths, newStorageFolderPath);

        // Let the caller know if files were replaced
        if (!replacedFiles.isEmpty()) {
            throw new FilesReplacedException(replacedFiles);
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
        assert sourcePath != null;
        String sourceDirectoryPath = sourcePath.getParent().toString();
        String originalFilename = sourcePath.getFileName().toString();
        Path destinationPath = getUnusedBackupPath(sourceDirectoryPath, originalFilename);

        try {
            Files.move(sourcePath, destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destinationPath.toAbsolutePath().toString();
    }

    /**
     * This method is used to write data into the file given at filePath.
     *
     * @param data
     *            the data to be written into the specified file
     * @param filePath
     *            the path of the file that we want to write data into
     */
    public static void writeToFile(String data, Path filePath) {
        assert data != null && filePath != null;
        try {
            File file = filePath.toFile();
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(file, Constants.CHARSET);
            writer.println(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads the content in the file specified by filePath and
     * returns the content in a String.
     *
     * @param filePath
     *            the path of the file that we want to read
     * @return a String of the file content, or an empty string if there was an
     *         error reading the file
     */
    public static String readFileToString(Path filePath) {
        assert filePath != null;
        try {
            File file = filePath.toFile();
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


    /*
     * Private helper methods
     */

    /**
     * This method returns a suitable backup path for the given file in the
     * given directory.
     *
     * @param directory
     *            the directory which contains the original file
     * @param originalFilename
     *            the filename of the original file
     * @return the path to a suitable (unused) backup file
     */
    private static Path getUnusedBackupPath(String directory, String originalFilename) {
        assert directory != null && originalFilename != null;
        String destinationFilename = null;
        Path destinationPath = null;
        int attempts = 0;

        // Try a different backup filename until we get one that doesn't yet exist
        do {
            destinationFilename = originalFilename + getBackupExtension(attempts++);
            destinationPath = Paths.get(directory, destinationFilename);
        } while (Files.exists(destinationPath));

        return destinationPath;
    }

    /**
     * This method moves all existing files in filePaths to the destination folder.
     *
     * @param filePaths
     *            an ArrayList of Paths for the files that are to be moved
     * @param destination
     *            the destination directory
     * @return an ArrayList of FilePathPairs for every file that was moved in
     *         the destination directory
     */
    private static ArrayList<FilePathPair> moveFilesTo(ArrayList<Path> filePaths, String destination) {
        ArrayList<FilePathPair> movedFiles = new ArrayList<FilePathPair>();

        // For each file path that exists, we backup the file and add it to the list of moved files
        filePaths.stream().filter(Files::exists).map(sourcePath -> {
            String filename = sourcePath.getFileName().toString();
            Path destinationPath = Paths.get(destination, filename);
            return moveAndBackup(sourcePath, destinationPath);
        }).filter(Objects::nonNull).forEach(movedFiles::add);

        return movedFiles;
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
        FilePathPair filePathPair = null;

        if (Files.exists(destination)) {
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
     * the given integer i if i != 0. Examples of extensions generated: .bak,
     * .bak1, .bak2, etc.
     *
     * @param i
     *            the number that will be appended to the back of the extension
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

}

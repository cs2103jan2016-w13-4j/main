//@@author A0121621Y

package jfdi.common.utilities;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.common.io.Files;

import jfdi.storage.Constants;

public class JfdiLogger {

    public static Logger logger = null;

    public static Logger getLogger() {
        try {
            if (logger == null) {
                logger = Logger.getLogger("JfdiLogger");
                FileHandler fileHandler;

                // Ensure that the logging directory is created
                String loggingFilePath = Constants.PATH_LOG_FILE.toString();
                Files.createParentDirs(new File(loggingFilePath));

                // Configure the logging path
                fileHandler = new FileHandler(loggingFilePath);
                logger.addHandler(fileHandler);

                // Configure the log formatter
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);

                logger.setLevel(Level.ALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logger;
    }
}

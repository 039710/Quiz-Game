package de.hda.fbi.db2.tools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by a.hofmann on 03.10.2016.
 */
public class CsvDataReader {
    private static final String SPLIT_CHARACTER = ";";
    private static final Logger log = Logger.getLogger(CsvDataReader.class.getName());
    
    private CsvDataReader() {}

    /**
     * Lists all available csv files in the resource folder.
     * <strong>WARNING:</strong> May won't work on your machine
     * @return list of csv filenames
     * @throws IOException
     * @throws URISyntaxException 
     */
    public static List<String> getAvailableFiles() throws IOException, URISyntaxException {
        final Enumeration<URL> en = CsvDataReader.class.getClassLoader().getResources("");
        en.nextElement();
        File hackyFile = new File(en.nextElement().toURI());
        File[] filenames = hackyFile.listFiles();
        
        List<String> fileList;
                
        if (filenames == null) {
            fileList = new ArrayList<>();
        } else {
        
            fileList = Arrays.asList(filenames).stream()
                .filter(file -> file.isFile())
                .filter(file -> file.getName().endsWith(".csv"))
                .map(file -> file.getName())
                .collect(Collectors.toList());
        }
        
        return fileList;
    }
    
    /**
     * Reads the given embedded file and returns its content in an accessible form.
     * @param otherFile filename of csv file in resource folder
     * @return content of the related file as a list of split strings including 
     * the CSV-header at first position.
     * @throws URISyntaxException
     * @throws IOException
     */
    public static List<String[]> read(String otherFile) throws IOException, URISyntaxException {
        try {
            if (!getAvailableFiles().contains(otherFile)){
                throw new IOException("File not found in Resources.");
            }
        } catch (IOException ioe){
            throw ioe;
        } catch (Exception e){
            log.warning("CsvDataReader.getAvailableFiles() threw a exception."
                    + " Skipping file verification.");
        }
        final URL resource = CsvDataReader.class.getResource("/" + otherFile);
        if (resource == null) {
            throw new IllegalStateException("Unable to find the csv file.");
        }
        return readFile(new File(resource.toURI()));
    }
    
    /**
     * Reads the embedded <code>Wissenstest_sample200.csv</code> file and 
     * returns its content in an accessible form.
     * @return content of the related file as a list of split strings including 
     * the CSV-header at first position.
     * @throws URISyntaxException
     * @throws IOException
     */
    public static List<String[]> read() throws URISyntaxException, IOException {
        final URL resource = CsvDataReader.class.getResource("/Wissenstest_sample200.csv");
        if (resource == null) {
            throw new IllegalStateException("Unable to find the csv file.");
        }
        return readFile(new File(resource.toURI()));
    }

    private static List<String[]> readFile(File file) throws IOException {
        final List<String> lines = Files.readAllLines(file.toPath());
        return lines.stream()
                    .map((line) -> line.split(SPLIT_CHARACTER))
                    .collect(Collectors.toList());
    }
}

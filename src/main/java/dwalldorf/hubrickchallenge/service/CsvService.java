package dwalldorf.hubrickchallenge.service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CsvService {

    private final static Logger logger = LoggerFactory.getLogger(CsvService.class);

    private final static String SEPARATOR = ",";

    public List<List<String>> readFile(Path sourceFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(sourceFile))) {
            return reader.lines()
                         .map(line -> Arrays.asList(line.split(SEPARATOR)))
                         .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void writeFile(Path targetFile, List<String> headers, List<List<String>> lines) {
        logger.info("Writing file {}", targetFile.toString());

        try (FileWriter writer = new FileWriter(targetFile.toString())) {
            writeLine(writer, headers);
            for (List<String> line : lines) {
                writeLine(writer, line);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLine(Writer writer, List<String> line) throws IOException {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (String entry : line) {
            if (!first) {
                sb.append(SEPARATOR);
            }

            sb.append(entry);
            first = false;
        }
        sb.append("\n");
        writer.append(sb.toString());
    }
}

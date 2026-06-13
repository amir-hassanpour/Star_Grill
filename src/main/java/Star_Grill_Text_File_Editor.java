import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Star_Grill_Text_File_Editor {
    private String fileName;
    private Path path;

    Star_Grill_Text_File_Editor(String fileName) {
        this.fileName = fileName;
        this.path = Paths.get(fileName);
    }

    public void addLastLine(String lineAdd) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(lineAdd + System.lineSeparator());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLastLines(List<String> lines) {
        for (String line : lines) {
            addLastLine(line);
        }
    }

    public String removeLastLine() throws IOException {
        List<String> lines = Files.readAllLines(path);

        if (lines.isEmpty()) {
            return null;
        }

        String removedLine = lines.remove(lines.size() - 1);

        Files.write(path, lines);

        return removedLine;
    }

    public long countLines() throws IOException {
        try (var lines = Files.lines(path)) {
            return lines.count();
        }
    }

    public void removeAllLines() throws IOException {
        Files.write(path, new ArrayList<>());
    }

    public List<String> removeLastLines(int count) throws IOException {
        List<String> removedLines = new ArrayList<>();

        List<String> currentLines = Files.readAllLines(path);

        if (count > currentLines.size()) {
            return null;
        }

        for (int i = 0; i < count; i++) {
            removedLines.add(currentLines.remove(currentLines.size() - 1));
        }

        Files.write(path, currentLines);

        return removedLines;
    }
}

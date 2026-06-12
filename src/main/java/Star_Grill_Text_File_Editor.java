import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Star_Grill_Text_File_Editor {
    private String fileName;
    private Path path = Paths.get(fileName);

    Star_Grill_Text_File_Editor(String fileName) {
        this.fileName = fileName;
    }

    public void addLastLine(String lineAdd) {
        try (FileWriter writer = new FileWriter("file.txt", true)) {
            writer.write(lineAdd + "\n");
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

        if (!lines.isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        Files.write(path, lines);

        return String.join(System.lineSeparator(), lines).trim();
    }

    public long countLines() throws IOException {
        try (var lines = Files.lines(path)) {
            return lines.count();
        }
    }

    public void removeAllLines() throws IOException {
        long count = countLines();

        for (int i = 0; i < count; i++) {
            removeLastLine();
        }
    }

    public List<String> removeLastLines(int count) throws IOException {
        List<String> lines = new ArrayList<>();

        if (count > countLines())
            return null;

        for (int i = 0; i < count; i++) {
            lines.add(removeLastLine());
        }

        return lines;
    }
}

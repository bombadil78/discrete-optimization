package ch.bullsoft.coloring;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Coloring {

    public static final void main(final String[] args) {
        try {
            solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solve(String[] args) throws IOException {
        String fileName = args[0].split("=")[1];
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEach(System.out::println);
        }
    }
}

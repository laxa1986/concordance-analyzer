package com.alex4.concordanceanalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Application require one argument - text file to parse (path)");
            return;
        }

        String filePath = args[0];
        Path path = Paths.get(filePath);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath);
            e.printStackTrace(System.err);
            return;
        }
        String text = new String(bytes);

        ConcordanceAnalyzer analyzer = new ConcordanceAnalyzer();
        ConcordancePrinter printer = new ConcordancePrinter(System.out);

        List<WordOccurrence> concordance = analyzer.analyze(text);
        printer.print(concordance);
    }
}
package ru.job4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.nio.*;
import java.util.stream.Collectors;

public class Main {
    // через Files find найти test_job4j_finder.txt
    // название файла передается в параметр "n"
    // вытащить название из "n", пустить процесс поиска в Files find
    // записать найденный файл в папку и файл из параметра "o"
    static List<String> listFiles = new ArrayList<>();

    public static void find(Path path, String fileName, String saveDir) {
        try {
            listFiles.addAll(Files.find(path, 1, (p, basicFileAttributes)
                    -> p.getFileName().getFileName().toString().equals(fileName))
                    .map(Path::toString).toList());
            save(listFiles, saveDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(List<String> files, String dir) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(dir))) {
            files.forEach(x -> {
                try {
                    output.write(x);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ArgsName argsName = ArgsName.of(args);
        Path startPath = Paths.get(argsName.get("d"));
        String saveDir = argsName.get("o");
        System.out.println(argsName.get("d"));
        System.out.println(argsName.get("n"));
        System.out.println(argsName.get("o"));
        find(startPath, argsName.get("n"), saveDir);
        listFiles.forEach(System.out::println);
        System.out.println(argsName);
    }
}

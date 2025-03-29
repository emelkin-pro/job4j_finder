package ru.job4j;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

public class Finder {
    private static List<String> listFiles = new ArrayList<>();

    public static void find(Path path, String fileName, String saveDir, String type) {
        try {
            Pattern pattern = getPattern(type, fileName);
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile()
                            && pattern.matcher(file.getFileName().toString()).find()) {
                        listFiles.add(file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.println("Файл пропущен из за нехватки доступов : " + file);
                    return FileVisitResult.CONTINUE;
                }
            });
            save(listFiles, saveDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pattern getPattern(String type, String name) {
        if (type.equals("")) {
            throw new IllegalArgumentException("Параметр -t пустой");
        } else if (name.equals("")) {
            throw new IllegalArgumentException("Параметр -n пустой");
        } else if (type.equals("mask")) {
            return Pattern.compile(maskToRegex(name));
        } else if (type.equals("regex")) {
            return Pattern.compile(name);
        } else if (type.equals("name")) {
            return Pattern.compile(Pattern.quote(name));
        } else {
            throw new IllegalArgumentException("Некорректный параметр -t");
        }
    }

    public static String maskToRegex(String mask) {
        StringBuilder regex = new StringBuilder();
        for (char c : mask.toCharArray()) {
            switch (c) {
                case '*' -> regex.append(".*");
                case '?' -> regex.append(".");
                case '.' -> regex.append("\\").append(c);
                default -> regex.append(c);
            }
        }
        return regex.toString();
    }

    public static void save(List<String> files, String dir) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(dir))) {
            files.forEach(x -> {
                try {
                    output.write(x);
                    output.write(System.lineSeparator());
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

        Path startDir = Paths.get(argsName.get("d"));
        String name = argsName.get("n");
        String saveDir = argsName.get("o");
        String type = argsName.get("t");
        System.out.println(startDir);
        System.out.println(name);
        System.out.println(saveDir);
        System.out.println(type);

        find(startDir, name, saveDir, type);
        listFiles.forEach(System.out::println);
    }
}

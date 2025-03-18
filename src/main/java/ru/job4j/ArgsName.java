package ru.job4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArgsName {

    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if ("".equals(key)) {
            throw new IllegalArgumentException("Key is empty");
        }
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("This key: '" + key + "' is missing");
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        Arrays.stream(args)
                .forEach(x -> {
                    if (Objects.nonNull(x) && !x.contains("=")) {
                        throw new IllegalArgumentException(
                                String.format(
                                        "Error: This argument '%s'"
                                                + " does not contain an equal sign", x));
                    }
                    if (x.charAt(0) != '-') {
                        throw new IllegalArgumentException(
                                String.format(
                                        "Error: This argument '%s'"
                                                + " does not start with a '-' character", x));
                    }
                    String[] str = x.split("=", 2);
                    if ("".contains(str[1])) {
                        throw new IllegalArgumentException(
                                String.format(
                                        "Error: This argument '%s' does not contain a value", x));
                    }
                    if (str[0].equals("-") || "".contains(str[0])) {
                        throw new IllegalArgumentException(
                                String.format(
                                        "Error: This argument '%s' does not contain a key", x));
                    }
                    values.put(str[0].substring(str[0].indexOf("-") + 1), str[1]);
                });
    }

    public static ArgsName of(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        ArgsName names = new ArgsName();
        names.parse(args);
        return names;
    }

    @Override
    public String toString() {
        return "ArgsName{" +
                "values=" + values +
                '}';
    }
}
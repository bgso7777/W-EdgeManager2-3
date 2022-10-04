package com.inswave.appplatform.util;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PathUtil {
    public static Path concat(Path path, String relativeDir, String delimeter) {
        String[] dirs = relativeDir.split(delimeter);
        for (String dir : dirs) {
            path = path.resolve(dir);
        }
        return path;
    }

    public static List<Path> concat(Path path, List<String> relativeDirs, String delimeter) {
        return relativeDirs.stream().map(dir -> concat(path, dir, delimeter)).collect(Collectors.toList());
    }
}

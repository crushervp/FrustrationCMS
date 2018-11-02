package com.codeexcursion.cms.content;

import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.util.Optional;

import com.codeexcursion.cms.util.PrintUtil;;

public final class ContentFileVisitor extends SimpleFileVisitor<Path> {
    private final Path targetDirectory;
    private final Path sourceDirectory;


    public ContentFileVisitor(Path targetDirectory, Path sourceDirectory) {
        this.targetDirectory = Optional.ofNullable(targetDirectory).orElseThrow(IllegalArgumentException::new);
        this.sourceDirectory = Optional.ofNullable(sourceDirectory).orElseThrow(IllegalArgumentException::new);

    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        System.out.println("Directory Pre Visit");
        if(dir == null) {
          PrintUtil.error("Skipping null directory");
          return FileVisitResult.SKIP_SUBTREE;          
        }
        Path relative = sourceDirectory.relativize(dir);
        System.out.println(relative);
        Path newDirectory = targetDirectory.resolve(relative);
        System.out.println(newDirectory);
        
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        //parse recognized file types and copy unrecognized file types
        return FileVisitResult.CONTINUE;
    }

}
package com.imp.all.netty.day1;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Longlin
 * @date 2024/2/7 11:25
 * @description
 *
 * 文件拷贝、移动、删除、遍历
 */
public class TestFile {

    public static void main(String[] args) {

//        copy();
//        move();
//        delete();

//        scan();
//
//        delete2();

        copy2();
    }

    @SneakyThrows
    public static void copy() {
        Path source = Paths.get("helloword/data.txt");
        Path target = Paths.get("helloword/target.txt");
        Files.copy(source, target);

        // 如果希望用 source 覆盖掉 target，需要用 StandardCopyOption 来控制
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

    }

    @SneakyThrows
    public static void move() {
        Path source = Paths.get("helloword/data.txt");
        Path target = Paths.get("helloword/data.txt");

        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
    }

    @SneakyThrows
    public static void delete() {
        // 删除文件
        Path target = Paths.get("helloword/target.txt");
        Files.delete(target);
        // 删除目录
        Path target2 = Paths.get("helloword/d1");
        Files.delete(target2);
    }

    @SneakyThrows
    public static void scan() {
        Path path = Paths.get("E:\\Downloads");
        // 文件目录数目
        AtomicInteger dirCount = new AtomicInteger();
        // 文件数目
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("===>"+dir);
                // 增加文件目录数
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                // 增加文件数
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        // 打印数目
        System.out.println("文件目录数:"+dirCount.get());
        System.out.println("文件数:"+fileCount.get());
    }

    // 删除多级目录
    @SneakyThrows
    public static void delete2() {
        Path path = Paths.get("E:\\Downloads");
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    // 拷贝多级目录
    @SneakyThrows
    public static void copy2() {
        String source = "E:\\Downloads";
        String target = "E:\\Downloads2";
        Files.walk(Paths.get(source)).forEach(new Consumer<Path>() {
            @SneakyThrows
            @Override
            public void accept(Path path) {
                String targetName = path.toString().replace(source, target);
                // 是目录
                if (Files.isDirectory(path)) {
                    Files.createDirectory(Paths.get(targetName));
                }
                else if (Files.isRegularFile(path)) {
                    Files.copy(path, Paths.get(targetName));
                }
            }
        });
    }
}

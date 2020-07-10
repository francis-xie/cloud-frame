package com.emis.vi.bm.api;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 深入理解BIO、NIO、AIO
 */
public class IoApi {
    private static String filePath = "E:\\Java\\IdeaProjects\\cloud-frame\\vi-bm\\src\\main\\java\\com\\emis\\vi\\bm" +
            "\\api\\data\\X.ZH001ZH0010002.20200622133407.SYSTEMINFO.TXT";

    public static void main(String[] args) throws Exception {
        //testOutputStream();
        //testInputStream();
        //testWriter();
        //testReader();

        //testFileWriterToReader();
        testFiles();
    }

    public static void testInputStream() {
        //InputStream 使用示例
        try {
            InputStream inputStream = new FileInputStream(filePath);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String str = new String(bytes, "utf-8");
            System.out.println(str);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testOutputStream() {
        //OutputStream 使用示例
        try {
            OutputStream outputStream = new FileOutputStream(filePath, true); // 参数二，表示是否追加，true=追加
            outputStream.write("你好，OutputStream".getBytes("utf-8"));
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testWriter() {
        //Writer 使用示例
        try {
            Writer writer = new FileWriter(filePath, true); // 参数二，是否追加文件，true=追加
            writer.append("\n" + "Writer，你好");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testReader() {
        //Reader 使用示例
        try {
            Reader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer bf = new StringBuffer();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                bf.append(str + "\n");
            }
            bufferedReader.close();
            reader.close();
            System.out.println(bf.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testFileWriterToReader() {
        //Java 7 之前文件的读取
        try {
            // 添加文件
            FileWriter fileWriter = new FileWriter(filePath, true); // 参数二，是否追加文件，true=追加
            fileWriter.write("\n" + "你好，FileWriter");
            fileWriter.close();

            // 读取文件
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer bf = new StringBuffer();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                bf.append(str + "\n");
            }
            bufferedReader.close();
            fileReader.close();
            System.out.println(bf.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testFiles() {
        //Java 7 引入了Files（java.nio包下）的，大大简化了文件的读写
        try {
            // 创建多（单）层目录（如果不存在创建，存在不会报错）
            //new File("E:\\Java\\IdeaProjects\\cloud-frame\\vi-bm\\src\\main\\java\\com\\emis\\vi\\bm\\data\\a\\b").mkdirs();

            // 写入文件（追加方式：StandardOpenOption.APPEND）
            Files.write(Paths.get(filePath), "\n你好，Files".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

            // 读取文件
            byte[] data = Files.readAllBytes(Paths.get(filePath));
            System.out.println(new String(data, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

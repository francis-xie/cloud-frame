package com.emis.vi.bm.javase.io.basis;

import java.io.*;

/**
 * OutputStream：https://www.liaoxuefeng.com/wiki/1252599548343744/1298069169635361
 * 和InputStream相反，OutputStream是Java标准库提供的最基本的输出流。
 * 和InputStream类似，OutputStream也是抽象类，它是所有输出流的超类。
 * 和InputStream类似，OutputStream也提供了close()方法关闭输出流，以便释放系统资源。
 * 要特别注意：OutputStream还提供了一个flush()方法，它的目的是将缓冲区的内容真正输出到目的地。
 * <p>
 * 小结
 * Java标准库的java.io.OutputStream定义了所有输出流的超类：
 * FileOutputStream实现了文件流输出；
 * ByteArrayOutputStream在内存中模拟一个字节流输出。
 * 某些情况下需要手动调用OutputStream的flush()方法来强制输出缓冲区。
 * 总是使用try(resource)来保证OutputStream正确关闭。
 */
public class OutputStreamTest {

    public static void main(String[] args) throws IOException {
        writeFile();
    }

    public static void writeFile() throws IOException {
        File f = new File(".");
        String path = f.getCanonicalPath() + File.separator + "vi-bm" + File.separator;
        System.out.println(path); //它和绝对路径类似，但是返回的是规范路径

        /* OutputStream这个抽象类定义的一个最重要的方法就是void write(int b)，签名如下：
           public abstract void write(int b) throws IOException;
           这个方法会写入一个字节到输出流。要注意的是，虽然传入的是int参数，但只会写入一个字节，
           即只写入int最低8位表示字节的部分（相当于b & 0xff）。
        */
        OutputStream output = new FileOutputStream(path + "src/readme.txt");
        output.write(72); // H
        output.write(101); // e
        output.write(108); // l
        output.write(108); // l
        output.write(111); // o
        /* 和InputStream类似，OutputStream也提供了close()方法关闭输出流，以便释放系统资源。
           要特别注意：OutputStream还提供了一个flush()方法，它的目的是将缓冲区的内容真正输出到目的地。
           通常情况下，我们不需要调用这个flush()方法，因为缓冲区写满了OutputStream会自动调用它，
           并且，在调用close()方法关闭OutputStream之前，也会自动调用flush()方法。*/
        output.close();

        //每次写入一个字节非常麻烦，更常见的方法是一次性写入若干字节。
        //这时，可以用OutputStream提供的重载方法void write(byte[])来实现：
        output = new FileOutputStream(path + "src/readme.txt");
        output.write("Hello".getBytes("UTF-8")); // Hello
        output.close();

        /* 和InputStream一样，上述代码没有考虑到在发生异常的情况下如何正确地关闭资源。
           写入过程也会经常发生IO错误，例如，磁盘已满，无权限写入等等。
           我们需要用try(resource)来保证OutputStream在无论是否发生IO错误的时候都能够正确地关闭：
        */
        try (OutputStream output2 = new FileOutputStream(path + "src/readme.txt")) {
            //阻塞，和InputStream一样，OutputStream的write()方法也是阻塞的。
            output2.write("Hello".getBytes("UTF-8")); // Hello
        } // 编译器在此自动为我们写入finally并调用close()

        //同时操作多个AutoCloseable资源时，在try(resource) { ... }语句中可以同时写出多个资源，用;隔开。例如，同时读写两个文件：
        //读取input.txt，写入output.txt:
        /*try (InputStream input3 = new FileInputStream("input.txt");
             OutputStream output3 = new FileOutputStream("output.txt")) {
            //JDK 9新增了一个有趣的方法,InputStream.transferTo(OutputStream)方法
            input3.transferTo(output3); // transferTo的作用是?
        }*/
    }

    /**
     * OutputStream实现类
     * 用FileOutputStream可以从文件获取输出流，这是OutputStream常用的一个实现类。
     * 此外，ByteArrayOutputStream可以在内存中模拟一个OutputStream：
     * ByteArrayOutputStream实际上是把一个byte[]数组在内存中变成一个OutputStream，虽然实际应用不多，
     * 但测试的时候，可以用它来构造一个OutputStream。
     *
     * @throws IOException
     */
    public static void testByteArrayOutputStream() throws IOException {
        byte[] data;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            output.write("Hello ".getBytes("UTF-8"));
            output.write("world!".getBytes("UTF-8"));
            data = output.toByteArray();
        }
        System.out.println(new String(data, "UTF-8"));
    }
}

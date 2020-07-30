package com.emis.vi.bm.javase.io.basis;

import java.io.*;

/**
 * InputStream：https://www.liaoxuefeng.com/wiki/1252599548343744/1298069163343905
 * InputStream就是Java标准库提供的最基本的输入流。它位于java.io这个包里。java.io包提供了所有同步IO的功能。
 * 要特别注意的一点是，InputStream并不是一个接口，而是一个抽象类，它是所有输入流的超类。
 * 这个抽象类定义的一个最重要的方法就是int read()，签名如下：public abstract int read() throws IOException;
 * 这个方法会读取输入流的下一个字节，并返回字节表示的int值（0~255）。如果已读到末尾，返回-1表示不能继续读取了。
 * <p>
 * Java的IO标准库提供的InputStream根据来源可以包括：
 * FileInputStream：从文件读取数据，是最终数据源；
 * ServletInputStream：从HTTP请求读取数据，是最终数据源；
 * Socket.getInputStream()：从TCP连接读取数据，是最终数据源；
 * ...
 * <p>
 * 小结
 * Java标准库的java.io.InputStream定义了所有输入流的超类：
 * FileInputStream实现了文件流输入；
 * ByteArrayInputStream在内存中模拟一个字节流输入。
 * 总是使用try(resource)来保证InputStream正确关闭。
 */
public class InputStreamTest {

    public static void main(String[] args) {
        try {
            InputStreamTest test = new InputStreamTest();
            test.readFile();

            //testByteArrayInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * FileInputStream是InputStream的一个子类。顾名思义，FileInputStream就是从文件流中读取数据。
     * 下面的代码演示了如何完整地读取一个FileInputStream的所有字节：
     *
     * @throws IOException 我们还要注意到在读取或写入IO流的过程中，可能会发生错误，例如，文件不存在导致无法读取，
     *                     没有写权限导致写入失败，等等，这些底层错误由Java虚拟机自动封装成IOException异常并抛出。
     *                     因此，所有与IO操作相关的代码都必须正确处理IOException。
     */
    public void readFile() throws IOException {
        File f = new File(".");
        String path = f.getCanonicalPath() + File.separator + "vi-bm" + File.separator;
        System.out.println(path); //它和绝对路径类似，但是返回的是规范路径

        //调用getResourceAsStream()需要特别注意的一点是，如果资源文件不存在，它将返回null。
        //因此，我们需要检查返回的InputStream是否为null，如果为null，表示资源文件在classpath中没有找到：
        try (InputStream input = getClass().getResourceAsStream("/application.properties")) {
            if (input != null) {
                System.out.println(input);
            }
        }

        // 创建一个FileInputStream对象:
        InputStream input = null;
        try {
            input = new FileInputStream(path + "src/readme.txt");
            for (; ; ) {
                //阻塞，在调用InputStream的read()方法读取数据时，我们说read()方法是阻塞（Blocking）的。
                int n = input.read(); // 反复调用read()方法，直到返回-1
                if (n == -1) {
                    break;
                }
                System.out.println(n); // 打印byte的值
            }
        } finally {
            if (input != null) {
                //InputStream和OutputStream都是通过close()方法来关闭流。关闭流就会释放对应的底层资源。
                input.close(); // 关闭流
                input = null;
            }
        }

        /* 用try ... finally来编写上述代码会感觉比较复杂，更好的写法是利用Java 7引入的新的try(resource)的语法，
           只需要编写try语句，让编译器自动为我们关闭资源。推荐的写法如下：
           实际上，编译器并不会特别地为InputStream加上自动关闭。
           编译器只看try(resource = ...)中的对象是否实现了java.lang.AutoCloseable接口，
           如果实现了，就自动加上finally语句并调用close()方法。InputStream和OutputStream都实现了这个接口，
           因此，都可以用在try(resource)中。

           缓冲
           在读取流的时候，一次读取一个字节并不是最高效的方法。很多流支持一次性读取多个字节到缓冲区，
           对于文件和网络流来说，利用缓冲区一次性读取多个字节效率往往要高很多。
           InputStream提供了两个重载方法来支持读取多个字节：
           int read(byte[] b)：读取若干字节并填充到byte[]数组，返回读取的字节数
           int read(byte[] b, int off, int len)：指定byte[]数组的偏移量和最大填充数
           利用上述方法一次读取多个字节时，需要先定义一个byte[]数组作为缓冲区，
           read()方法会尽可能多地读取字节到缓冲区， 但不会超过缓冲区的大小。
           read()方法的返回值不再是字节的int值，而是返回实际读取了多少个字节。如果返回-1，表示没有更多的数据了。
           利用缓冲区一次读取多个字节的代码如下：
        */
        try (InputStream input2 = new FileInputStream(path + "src/readme.txt")) {
            // 定义1000个字节大小的缓冲区:
            byte[] buffer = new byte[1000];
            int n;
            while ((n = input2.read(buffer)) != -1) { // 利用while同时读取并判断
                System.out.println("read " + n + " bytes.");
            }
        } // 编译器在此自动为我们写入finally并调用close()
    }

    /**
     * InputStream实现类
     * 用FileInputStream可以从文件获取输入流，这是InputStream常用的一个实现类。
     * 此外，ByteArrayInputStream可以在内存中模拟一个InputStream：
     * ByteArrayInputStream实际上是把一个byte[]数组在内存中变成一个InputStream，
     * 虽然实际应用不多，但测试的时候，可以用它来构造一个InputStream。
     *
     * @throws IOException
     */
    public static void testByteArrayInputStream() throws IOException {
        byte[] data = {72, 101, 108, 108, 111, 33};
        try (InputStream input = new ByteArrayInputStream(data)) {
            String s = readAsString(input);
            System.out.println(s);
        }
    }

    /**
     * 对这个String readAsString(InputStream input)方法进行测试就相当简单，因为不一定要传入一个真的FileInputStream：
     * 举个栗子：我们想从文件中读取所有字节，并转换成char然后拼成一个字符串，可以这么写：
     * 这就是面向抽象编程原则的应用：接受InputStream抽象类型，而不是具体的FileInputStream类型，
     * 从而使得代码可以处理InputStream的任意实现类。
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static String readAsString(InputStream input) throws IOException {
        int n;
        StringBuilder sb = new StringBuilder();
        while ((n = input.read()) != -1) {
            sb.append((char) n);
        }
        return sb.toString();
    }
}

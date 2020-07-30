package com.emis.vi.bm.javase.io.basis;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Reader：https://www.liaoxuefeng.com/wiki/1252599548343744/1298366902304801
 * Reader是Java的IO库提供的另一个输入流接口。和InputStream的区别是，InputStream是一个字节流，即以byte为单位读取，
 * 而Reader是一个字符流，即以char为单位读取：
 * <p>
 * 小结
 * Reader定义了所有字符输入流的超类：
 * FileReader实现了文件字符流输入，使用时需要指定编码；
 * CharArrayReader和StringReader可以在内存中模拟一个字符流输入。
 * Reader是基于InputStream构造的：可以通过InputStreamReader在指定编码的同时将任何InputStream转换为Reader。
 * 总是使用try (resource)保证Reader正确关闭。
 */
public class ReaderTest {
    public static void main(String[] args) throws IOException {
        readFile();
    }

    /**
     * FileReader
     * FileReader是Reader的一个子类，它可以打开文件并获取Reader。下面的代码演示了如何完整地读取一个FileReader的所有字符：
     *
     * @throws IOException
     */
    public static void readFile() throws IOException {
        File f = new File(".");
        String path = f.getCanonicalPath() + File.separator + "vi-bm" + File.separator;
        System.out.println(path); //它和绝对路径类似，但是返回的是规范路径

        // 创建一个FileReader对象:
        Reader reader = new FileReader(path + "src/readme.txt"); // 字符编码是???
        for (; ; ) {
            int n = reader.read(); // 反复调用read()方法，直到返回-1
            if (n == -1) {
                break;
            }
            System.out.println((char) n); // 打印char
        }
        reader.close(); // 关闭流

        //和InputStream类似，Reader也是一种资源，需要保证出错的时候也能正确关闭，
        //所以我们需要用try (resource)来保证Reader在无论有没有IO错误的时候都能够正确地关闭：
        try (Reader reader2 = new FileReader(path + "src/readme.txt")) {
            //Reader还提供了一次性读取若干字符并填充到char[]数组的方法：
            char[] buffer = new char[1000];
            int n;
            //read(char[] c) 它返回实际读入的字符个数，最大不超过char[]数组的长度。返回-1表示流结束。
            //利用这个方法，我们可以先设置一个缓冲区，然后，每次尽可能地填充缓冲区：
            while ((n = reader2.read(buffer)) != -1) {
                System.out.println("read " + n + " chars.");
            }
        }

        //CharArrayReader可以在内存中模拟一个Reader，它的作用实际上是把一个char[]数组变成一个Reader，
        //这和ByteArrayInputStream非常类似：
        try (Reader reader3 = new CharArrayReader("Hello".toCharArray())) {
        }
        //StringReader可以直接把String作为数据源，它和CharArrayReader几乎一样：
        try (Reader reader4 = new StringReader("Hello")) {
        }

        /* InputStreamReader
           Reader和InputStream有什么关系？
           除了特殊的CharArrayReader和StringReader，普通的Reader实际上是基于InputStream构造的，
           因为Reader需要从InputStream中读入字节流（byte），然后，根据编码设置，再转换为char就可以实现字符流。
           如果我们查看FileReader的源码，它在内部实际上持有一个FileInputStream。
           既然Reader本质上是一个基于InputStream的byte到char的转换器，那么，如果我们已经有一个InputStream，
           想把它转换为Reader，是完全可行的。InputStreamReader就是这样一个转换器，它可以把任何InputStream转换为Reader。
           示例代码如下：
        */
        // 持有InputStream:
        InputStream input5 = new FileInputStream(path + "src/readme.txt");
        // 变换为Reader:
        Reader reader5 = new InputStreamReader(input5, "UTF-8");

        //构造InputStreamReader时，我们需要传入InputStream，还需要指定编码，就可以得到一个Reader对象。
        //上述代码可以通过try (resource)更简洁地改写如下：
        try (Reader reader6 = new InputStreamReader(new FileInputStream(path + "src/readme.txt"),
                StandardCharsets.UTF_8)) {
        }

        /* 上述代码实际上就是FileReader的一种实现方式。
           使用try (resource)结构时，当我们关闭Reader时，它会在内部自动调用InputStream的close()方法，
           所以，只需要关闭最外层的Reader对象即可。
           使用InputStreamReader，可以把一个InputStream转换成一个Reader。
        */
    }
}

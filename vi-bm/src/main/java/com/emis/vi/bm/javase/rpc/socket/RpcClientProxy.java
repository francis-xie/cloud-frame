package com.emis.vi.bm.javase.rpc.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 客户端实现
 * InvocationHandler：https://www.cnblogs.com/LCcnblogs/p/6823982.html
 * <p>
 * 泛型：https://www.liaoxuefeng.com/wiki/1252599548343744/1265102638843296
 *
 * @param <T>
 */
public class RpcClientProxy<T> implements InvocationHandler {
    /**
     * 指定接口类
     * https://blog.csdn.net/javazejian/article/details/70768369
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1264799402020448
     */
    private Class<T> serviceInterface;
    /**
     * 指定IP端口连线
     * https://www.cnblogs.com/kuangzhisen/p/7053689.html
     */
    private InetSocketAddress addr;

    /**
     * 传入接口类和ip端口
     *
     * @param serviceInterface 接口类
     * @param ip               IP地址
     * @param port             访问端口
     */
    public RpcClientProxy(Class<T> serviceInterface, String ip, String port) {
        //绑定委托对象
        this.serviceInterface = serviceInterface;
        //根据主机名和端口号创建连线地址
        this.addr = new InetSocketAddress(ip, Integer.parseInt(port));
    }

    /**
     * 调用getClientIntance方法时,对当前接口进行代理,实际调用方法为invoke
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1264804593397984
     *
     * @return
     */
    public T getClientIntance() {
        //绑定该类实现的所有接口，取得代理类
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), // 传入ClassLoader
                new Class<?>[]{serviceInterface}, // 传入要实现的接口
                this); // 传入处理调用方法的InvocationHandler
    }

    /**
     * 遵循下面步骤
     * 1：创建socket，并和远程进行三次连接握手【socket.connect(addr)】。
     * 2：封装socket输出流【ObjectOutputStream】。
     * 3：输出类名称，方法名称，参数类型和参数值给server。
     * 4：获取socket输入流，等待server返回结果。
     *
     * @param o
     * @param method
     * @param objects
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable { //不依赖具体接口实现
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;

        try {
            // 2.创建Socket客户端，根据指定地址连接远程服务提供者
            socket = new Socket();
            socket.connect(addr);

            // 3.将远程服务调用所需的接口类、方法名、参数列表等编码后发送给服务提供者
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeUTF(serviceInterface.getName());
            output.writeUTF(method.getName());
            output.writeObject(method.getParameterTypes());
            output.writeObject(objects);

            // 4.同步阻塞等待服务器返回应答，获取应答后返回
            input = new ObjectInputStream(socket.getInputStream());
            return input.readObject();
        } finally {
            if (socket != null) socket.close(); //关闭socket远程连接
            if (output != null) output.close(); //关闭socket输出流
            if (input != null) input.close(); //关闭socket输入流
        }
    }

    public static void main(String[] args) {
        RpcClientProxy client = new RpcClientProxy<>(IHello.class, "localhost", "6666");
        IHello hello = (IHello) client.getClientIntance();
        System.out.println(hello.sayHello("socket rpc"));
    }
}

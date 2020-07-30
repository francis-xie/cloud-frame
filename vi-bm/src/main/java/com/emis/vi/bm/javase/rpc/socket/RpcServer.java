package com.emis.vi.bm.javase.rpc.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * 服务端实现
 */
public class RpcServer {
    private static final HashMap<String, Class<?>> serviceRegistry = new HashMap<>();
    private int port;

    public RpcServer(int port) {
        this.port = port;
    }

    /**
     * 初始化服务端，将服务类注册到hashMap【模拟spring上下文】
     *
     * @param serviceInterface
     * @param impl
     * @return
     */
    public RpcServer register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
        return this;
    }

    /**
     * 服务端执行做了以下几件事：
     * 1：绑定端口，阻塞等待客户端调用【socket = server.accept ()】。
     * 2：封装输入流【socket.getInputStream()】。
     * 3：从输入流中获取到接口名，方法名，参数类型，参数值。
     * 4：找到初始化时hashmap中的服务类。
     * 5：反射获取服务实现类方法并根据请求参数进行服务调用。
     * 6：封装输出流【ObjectOutputStream】，并且返回结果。
     *
     * @throws IOException
     */
    public void run() throws IOException {

        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(port)); //绑定端口
        System.out.println("start server");
        ObjectInputStream input = null;
        ObjectOutputStream output = null;
        Socket socket = null;
        try {
            while (true) {
                socket = server.accept(); //阻塞等待客户端调用
                input = new ObjectInputStream(socket.getInputStream()); //封装输入流
                //从输入流中获取到接口名，方法名，参数类型，参数值。
                String serviceName = input.readUTF();
                String methodName = input.readUTF();
                System.out.println(methodName);
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                Class serviceClass = serviceRegistry.get(serviceName); //找到初始化时hashmap中的服务类
                if (serviceClass == null) {
                    throw new ClassNotFoundException(serviceName + " not found");
                }
                //反射获取服务实现类方法并根据请求参数进行服务调用
                Method method = serviceClass.getMethod(methodName, parameterTypes);
                Object result = method.invoke(serviceClass.newInstance(), arguments);
                output = new ObjectOutputStream(socket.getOutputStream()); //封装输出流
                output.writeObject(result); //返回结果
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close(); //关闭输入流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) { //关闭socket远程连接
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new RpcServer(6666).register(IHello.class, HelloServiceImpl.class).run();
    }
}

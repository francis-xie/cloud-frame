package com.emis.vi.bm.javase.rpc.socket;

/**
 * 服务实现类
 */
public class HelloServiceImpl implements IHello {
    @Override
    public String sayHello(String string) {
        //concat：String 类提供的连接两个字符串的方法、将指定字符串连接到此字符串的结尾。
        return "你好:".concat(string);
    }
}

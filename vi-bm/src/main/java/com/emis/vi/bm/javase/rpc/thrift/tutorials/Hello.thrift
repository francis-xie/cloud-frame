 /**
  * 其中定义了服务 Hello 的五个方法，每个方法包含一个方法名，参数列表和返回类型。
  * 每个参数包括参数序号，参数类型以及参数名。
  * Thrift 是对 IDL(Interface Definition Language) 描述性语言的一种具体实现。
  * 因此，以上的服务描述文件使用 IDL 语法编写。使用 Thrift 工具编译 Hello.thrift，就会生成相应的 Hello.java 文件。
  * 该文件包含了在 Hello.thrift 文件中描述的服务 Hello 的接口定义，即 Hello.Iface 接口，以及服务调用的底层通信细节，
  * 包括客户端的调用逻辑 Hello.Client 以及服务器端的处理逻辑 Hello.Processor，用于构建客户端和服务器端的功能。
  * thrift -r --gen java Hello.thrift
  */
 namespace java service.demo
 service Hello{
  string helloString(1:string para)
  i32 helloInt(1:i32 para)
  bool helloBoolean(1:bool para)
  void helloVoid()
  string helloNull()
 }
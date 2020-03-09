package com.kelail.nio.wechat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @program: NettyPro
 * @description:  群聊系统服务器端
 * @author: ytw
 * @create: 2020-03-07 20:42
 **/
public class NIOServer {

    /**
     * 1.编写一个NIO群聊系统，实现客户端和服务器端的简单通讯（非阻塞）
     * 2.实现多人群聊
     * 3.服务器端：可以监测客户端上线下线，并且实现消息转发
     * 4.客户端：通过Channel发送消息，给其他用户，同时接受其他客户端的消息，由服务器转发得到
     *
     * 1. 服务器端口：6667
     * 2.服务器接受并转发消息，处理客户端上线下线
     * 3.编写客户端
     */

    //定义相关属性
    private Selector selector;
    private ServerSocketChannel listenChannel;//专门用来监听
    private static final int port = 6667;

    //构造器
    public NIOServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //得到ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(port));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try {
            //循环监听
            while (selector.select() > 0){
                //设置监听事件
                int count = selector.select();
                if (count > 0) {//有事件处理
                    //遍历selecttionkey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出selectionKey
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将改sc注册到select上   OP_READ  读事件
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }if (key.isReadable()){//通道发送read事件；通道是可读的状态，我们可以从通道读数据到buffer中
                            //处理读数据，专门写一个方法
                            readData(key);
                        }
                        //删除key，防止重复处理
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待中....");
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {

        }
    }


    /**
     * 读取客户端消息
     * @param
     */
    public void readData(SelectionKey key){

        SocketChannel socketChannel = null;
        try {
            //得到channel
            socketChannel = (SocketChannel)key.channel();
            //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //返回一个长度  根据count的值进行处理大于0有数据，把缓冲区的数据转成字符串输出，
            int count = socketChannel.read(buffer);

            if (count > 0){
                String str = new String(buffer.array());
                System.out.println("form 客户端：" + str);
                //向其他客户端转发消息(去掉自己 A客户不需要收自己的消息)；专门写一个方法处理
                sendInfoToOtherListenClients(str,socketChannel);
            }

        } catch (IOException e){
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                //取消注册，
                key.cancel();
                //关闭通道
                listenChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他的客户——>通道   每个客户就是一个通道
     * @param self 发消息的通道
     */
    public void sendInfoToOtherListenClients(String msg,SocketChannel self)throws IOException{
        System.out.println("服务器转发消息...");
        //遍历所有注册到selector上的SocketChannel,并排除自己
        for (SelectionKey  key : selector.keys() ) {
            //通过key取出对应的socketchneel
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self){
                //转发消息给其他客户端
                SocketChannel dest = (SocketChannel)targetChannel;
                //将msg存到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer数据写到通道
                dest.write(buffer);
            }
        }
    }



    public static void main(String[] args) {
        //创建一个服务器对象
        NIOServer nioServer = new NIOServer();
        nioServer.listen();


    }



}

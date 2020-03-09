package com.kelail.nio.wechat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @program: NettyPro
 * @description:
 * @author: ytw
 * @create: 2020-03-07 21:41
 **/
public class NIOClient {
    private final String host =  "127.0.0.1";//服务器IP
    private final int port = 6667;//服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    //构造器
    public NIOClient() throws IOException {
        //初始化工作
        selector = Selector.open();
        //连接
        socketChannel = socketChannel.open(new InetSocketAddress("127.0.0.1", port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //获取客户单的localAddress
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("客户端初始化完成...");

    }

    //向服务器发送消息
    public void send(String info){
        info = userName +"说："+ info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //读取服务端回发的消息
    public void read(){
        try {
            int readChannel = selector.select(2000);//不写参数表示阻塞
            if (readChannel > 0){//有可用通道
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){//如果是可读的，我们得到相关的通道
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        //Dedao得到一个buffer
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                        socketChannel.read(byteBuffer);
                        //转字符串
                        String str = new String(byteBuffer.array());
                        System.out.println(str.trim());
                    }
                }
            }else {
                System.out.println("没有可用通道...");
            }
        }catch (IOException e){

        }
    }

    public static void main(String[] args) throws Exception {
        //启动客户端
          final NIOClient nioClient = new NIOClient();
        //启动线程
        new Thread(){
            public void run() {
                while (true){
                    nioClient.read();
                    try {
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.nextLine();
            nioClient.send(str);
        }



    }


}

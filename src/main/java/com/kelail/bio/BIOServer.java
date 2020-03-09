package com.kelail.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: NettyPro
 * @description: BIO 服务端
 * @author: ytw
 * @create: 2020-02-25 09:51
 **/
public class BIOServer {
    /**
     * 使用线程池机制编写一个BIOserve模型，监听6666端口，当有客户端连接的时候就启动一个线程与之通信
     * @param args
     */
    public static void main(String[] args) throws Exception{

        //1 创建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动");
        while (true){
            //监听；等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("有一个客户端连接");
            //启动一个线程，与之通讯
            executorService.execute(new Runnable() {
                public void run() {
                    //可以和客户端进行通信
                    handler(socket);
                }
            });

        }

    }
    //编写一个handler 方法，和客户端通信
    public static void handler(Socket socket){
        System.out.println("线程ID：" + Thread.currentThread().getId() + Thread.currentThread().getName());

        try {
            byte[] bytes = new byte[2048];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环读取数据
            while (true){
        System.out.println("线程ID：" + Thread.currentThread().getId() + Thread.currentThread().getName());

                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

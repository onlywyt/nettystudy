package com.kelail.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: NettyPro
 * @description: FileChannel
 * @author: ytw
 * @create: 2020-02-25 15:49
 **/
public class NIOFileChannel {

    public static void main(String[] args) throws IOException {
        String s = "你好，我在吃饭";
       // FileChannelRead(s);
        FileChannelRead();
    }
    private static void FileChannelRead() throws IOException {
        File file = new File("/Users/wang/Desktop/code/NettyPro/src/main/java/com/kelail/nio/01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel readChannel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        readChannel.read(byteBuffer);
        //byteBuffer中的数据以数组的方式输出
        System.out.println(new String(byteBuffer.array()));
        readChannel.close();


    }
    //Channel 写数据到本地
    private static void FileChannelWrite(String s) throws IOException {
        try {
            //创一个输出流 channel
            FileOutputStream fileInputStream = new FileOutputStream("/Users/wang/Desktop/code/NettyPro/src/main/java/com/kelail/nio/01.txt");
            //获取channel,真是类型是FileChannelImpl
            FileChannel fileChannel = fileInputStream.getChannel();
            //创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(s.getBytes());
            //反转Buffer
            byteBuffer.flip();
            //将byteBuffer中的数据写到Channel中
            fileChannel.write(byteBuffer);
            fileChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

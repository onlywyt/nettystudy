package com.kelail.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: NettyPro
 * @description: 利用Buffer,channel 进行文件的拷贝
 * @author: ytw
 * @create: 2020-02-25 18:43
 **/
public class NIOChannelCopy  {

    public static void main(String[] args) throws Exception {
        CopyString();
        CopyPic();
        return;
    }
    private static void CopyPic() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/wang/Desktop/code/NettyPro/src/main/java/com/kelail/nio/qq1.png");
        FileChannel channel = fileOutputStream.getChannel();

        FileInputStream fileInputStream = new FileInputStream("/Users/wang/Desktop/code/NettyPro/src/main/java/com/kelail/nio/qq.png");
        FileChannel channel1 = fileInputStream.getChannel();

        channel.transferFrom(channel1,0,channel1.size());
        channel.close();
        channel1.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
    private static void CopyString() throws IOException {
        FileInputStream fileInputStream1 = new FileInputStream("/Users/wang/Desktop/code/NettyPro/src/main/java/com/kelail/nio/01.txt");
        FileChannel channel1 = fileInputStream1.getChannel();

        FileOutputStream fileOutputStream2 = new FileOutputStream("/Users/wang/Desktop/code/NettyPro/src/main/java/com/kelail/nio/02.txt");
        FileChannel channel2 = fileOutputStream2.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.putInt(1);
        byteBuffer.putChar('你');
        byteBuffer.flip();
        int anInt = byteBuffer.getInt();
        char aChar = byteBuffer.getChar();
        System.out.println(aChar);
        System.out.println(anInt);
        while (true) {
            byteBuffer.clear();
            int read = channel1.read(byteBuffer);
            System.out.println(read);
            if (read == -1){
                break;
            }
            byteBuffer.flip();
            channel2.write(byteBuffer);
        }
        fileInputStream1.close();
        fileOutputStream2.close();
    }

}

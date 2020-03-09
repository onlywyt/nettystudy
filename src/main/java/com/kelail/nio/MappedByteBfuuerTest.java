package com.kelail.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: ytw
 * @create: 2020-02-26 14:14
 **/
public class MappedByteBfuuerTest {

    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile //rw代表读写
                ("/Users/wang/Desktop/code/NettyPro/src/main/java/com/kelail/nio/01.txt","rw");

        //获取通道  此通道关联randomAccessFile
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数：
         *  1：FileChannel.MapMode.READ_WRITE 表示使用读写模式
         *  2：0 代表可以直接修改的起始位置
         *  3：5 是映射到内存的大小-->表示我把这个文件的第几个位置映射过去，5 表示最多映射5个字节即可以直接修改的范围
         *  就是0到5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0,(byte)'H');
        mappedByteBuffer.put(2,(byte)'0');
        randomAccessFile.close();

    }
}

package com.kelail.nio;

import java.nio.IntBuffer;

/**
 * @program: NettyPro
 * @description:NIO
 * @author: ytw
 * @create: 2020-02-25 13:11
 **/
public class BasicBuffer {
    /**
     * 举例说明Buffer的使用
     * @param args
     */
    public static void main(String[] args) {

        //创建一个Buffer;可以存5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        intBuffer.put(1);
        intBuffer.put(2);
        intBuffer.put(3);
        intBuffer.put(4);
        intBuffer.put(5);
        //切换状态。将Buffer状态转变（读写切换）
        intBuffer.flip();
        intBuffer.position(3);
        while (intBuffer.hasRemaining()){
            //buffer中维护了一个索引，每次get索引都会往后移动
            System.out.println(intBuffer.get());
        }
    }
}

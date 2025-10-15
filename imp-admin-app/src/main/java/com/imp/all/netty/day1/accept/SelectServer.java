package com.imp.all.netty.day1.accept;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Longlin
 *
 * https://nyimac.gitee.io/2021/04/18/Netty%E5%AD%A6%E4%B9%A0%E4%B9%8BNIO%E5%9F%BA%E7%A1%80/#%E4%B8%89%E3%80%81%E7%BD%91%E7%BB%9C%E7%BC%96%E7%A8%8B
 */
public class SelectServer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 获得服务器通道
        try(ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(8080));
            // 创建选择器
            Selector selector = Selector.open();
            
            /**
             * 将通道设置为非阻塞模式，并注册到选择器中，并设置感兴趣的事件
             * channel 必须工作在非阻塞模式
             * FileChannel 没有非阻塞模式，因此不能配合 selector 一起使用
             * 绑定的事件类型可以有
             * connect - 客户端连接成功时触发
             * accept - 服务器端成功接受连接时触发
             * read - 数据可读入时触发，有因为接收能力弱，数据暂不能读入的情况
             * write - 数据可写出时触发，有因为发送能力弱，数据暂不能写出的情况
             */
            // 通道必须设置为非阻塞模式
            server.configureBlocking(false);
            // 将通道注册到选择器中，并设置感兴趣的事件
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                // 若没有事件就绪，线程会被阻塞，反之不会被阻塞。从而避免了CPU空转
                // 返回值为就绪的事件个数
                int ready = selector.select();
                System.out.println("selector ready counts : " + ready);
                
                // 获取所有事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                
                // 使用迭代器遍历事件
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    
                    // 判断key的类型
                    if(key.isAcceptable()) {
                        // 获得key对应的channel
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        System.out.println("before accepting...");
                        
        				// 获取连接并处理，而且是必须处理，否则需要取消
                        SocketChannel socketChannel = channel.accept();
                        System.out.println("after accepting...");
                        
                        // 处理完毕后移除
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
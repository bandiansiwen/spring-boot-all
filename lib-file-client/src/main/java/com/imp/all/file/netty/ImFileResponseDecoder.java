package com.imp.all.file.netty;

import com.imp.all.file.type.TransmissionType;
import com.imp.all.file.utils.MsgUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 */
@Slf4j
public class ImFileResponseDecoder extends MessageToMessageDecoder<ByteBuf> {
    private byte[] lastBytes;
    private long total;

    public ImFileResponseDecoder() {
        super();
    }

    public void reset() {
        lastBytes = null;
        total = 0;
    }

    /**
     * TODO 如果发生粘包情况，要处理拆包
     *
     * @param ctx
     * @param msg
     * @param out
     */

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        try {
            int readableBytes = msg.readableBytes();
            //没有任何可读的数据
            if ((lastBytes == null || lastBytes.length == 0) && readableBytes == 0) {
                return;
            }
            decodeByteBuf(msg, out, readableBytes);
        } catch (Exception e) {
            e.printStackTrace();
            reset();
        }
    }

    private void decodeByteBuf(ByteBuf msg, List<Object> out, int readableBytes) {
        byte[] merger;
        byte[] curBytes = new byte[readableBytes];
        msg.getBytes(0, curBytes);
        if (lastBytes != null && lastBytes.length > 0) {
            merger = byteMerger(lastBytes, curBytes);
        } else {
            merger = curBytes;
        }

        int length = merger.length;

        //如果加在一起的总长度少于5，不做解析，5为 头（1个字节） + 长度（4个字节）的和
        //并且缓存当前数组
        if (length < 5) {
            lastBytes = merger;
            return;
        }
        //TODO 不要删除，必要时查看报文
        log.info("decodeByteBuf > lastBytes="
                + (lastBytes == null ? 0 : lastBytes.length)
                + " ByteBuf=" + readableBytes
                + " merger=" + length
                + " total=" + total
                + " ThreadName=" + Thread.currentThread().getName());

        int fullLength = 0;
        int index = 0;
        int lastIndex = length - 1;
        while (index < lastIndex) {
            if (lastIndex - index < 4) {
                lastBytes = new byte[length - index];
                System.arraycopy(merger, index, lastBytes, 0, lastBytes.length);
                break;
            }
            TransmissionType type = TransmissionType.valueOf(merger[index]);
            switch (type) {
                case HEARTBEAT_REQ:
                case HEARTBEAT_RSP:
                    fullLength = 7;
                    total = fullLength;
                    break;
                case NEGOTIATE_REQ:
                case NEGOTIATE_RSP:
                    fullLength = 8;
                    total = fullLength;
                    break;
                case PB:
                case JSON:
                case JSON_ENCRYPT:
                    byte[] lengthArray = new byte[4];
                    lengthArray[0] = merger[index + 1];
                    lengthArray[1] = merger[index + 2];
                    lengthArray[2] = merger[index + 3];
                    lengthArray[3] = merger[index + 4];
                    fullLength = MsgUtil.byte2int(lengthArray) + 7; //因为要加上头+数据区长度+CRC长度
                    total = fullLength;
                    break;
                default:
                    reset();
                    break;
            }
            int curBlockLastIndex = index + fullLength - 1;
            if (curBlockLastIndex <= lastIndex) {
                byte[] outBytes = new byte[fullLength];
                System.arraycopy(merger, index, outBytes, 0, fullLength);
                out.add(new FileResponse(outBytes));
                if (curBlockLastIndex == lastIndex) {
                    index = curBlockLastIndex;
                    break;
                } else {
                    index = curBlockLastIndex + 1;//从下一个指针开始
                }
            } else {
                lastBytes = new byte[length - index];
                System.arraycopy(merger, index, lastBytes, 0, lastBytes.length);
                break;
            }
        }
        //如果指针刚好等于新数组长度，表明刚好迭代，重设所有变量
        if (index == lastIndex) {
            reset();
        }
    }

    private byte[] byteMerger(byte[] byte1, byte[] byte2) {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

}

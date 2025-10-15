package com.imp.all.file.utils;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.imp.all.file.type.FileCmdType;
import com.imp.all.file.type.TransmissionType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LIANGYJ3
 * @date 2016-5-7
 */
public class MsgUtil {

    private static AtomicInteger sq = new AtomicInteger();
    public static int byte2int(byte[] res) {
        return (res[0] << 24) | ((res[1] << 24) >>> 8) | ((res[2] << 8) & 0xff00) | (res[3] & 0xff);
    }

    public static byte[] assembleFileRequestBytes(int sq, FileCmdType type, Message.Builder builder) {

        // MessageLite to byte[]
        byte[] dataArray = FileDataByteUtil.toFileData(sq, type, builder.build());

        CRC16Modbus crc16Modbus = new CRC16Modbus();
        crc16Modbus.reset();
        crc16Modbus.update(dataArray, 0, dataArray.length);
        byte[] crc = crc16Modbus.getCrcBytes();

        int size = dataArray.length + 7;
        //组装字节数组
        byte[] requestByte = new byte[size];
        //第1位：type，占用1个字节
        requestByte[0] = TransmissionType.PB.getByte();
        //第2到5位：length，占用4个字节由高位到低位
        requestByte[1] = (byte) ((dataArray.length >> 24) & 0xFF);
        requestByte[2] = (byte) ((dataArray.length >> 16) & 0xFF);
        requestByte[3] = (byte) ((dataArray.length >> 8) & 0xFF);
        requestByte[4] = (byte) (dataArray.length & 0xFF);
        //第6位到倒数第三位 data, 占用（总长度 - 1 - 4 - 2）个字节
        int index = 0;
        for (int d = 5; d < 5 + dataArray.length; d++) {
            requestByte[d] = dataArray[index++];
        }
        //最后两位：crc，占用2个字节
        requestByte[size - 2] = crc[1];
        requestByte[size - 1] = crc[0];
        return requestByte;
    }

    public static byte[] assembleFileRequestBytes(int sq, FileCmdType type, MessageLite msg) {
        // MessageLite to byte[]
        byte[] dataArray = FileDataByteUtil.toFileData(sq, type, msg);
        /*
         *校验码, 考虑文件数据比较多, 对于文件数据暂时不用, 直接设为0
         */
        byte[] crc = new byte[2];
        int size = dataArray.length + 7;
        //组装字节数组
        byte[] requestByte = new byte[size];
        //第1位：type，占用1个字节
        requestByte[0] = TransmissionType.PB.getByte();
        //第2到5位：length，占用4个字节由高位到低位
        requestByte[1] = (byte) ((dataArray.length >> 24) & 0xFF);
        requestByte[2] = (byte) ((dataArray.length >> 16) & 0xFF);
        requestByte[3] = (byte) ((dataArray.length >> 8) & 0xFF);
        requestByte[4] = (byte) (dataArray.length & 0xFF);
        //第6位到倒数第三位 data, 占用（总长度 - 1 - 4 - 2）个字节
        int index = 0;
        for (int d = 5; d < 5 + dataArray.length; d++) {
            requestByte[d] = dataArray[index++];
        }
        //最后两位：crc，占用2个字节
        requestByte[size - 2] = crc[1];
        requestByte[size - 1] = crc[0];
        return requestByte;
    }

    public static synchronized int incrementAndGet7f() {
        sq.compareAndSet(0x7FFF, 1);
        return sq.incrementAndGet();
    }
}

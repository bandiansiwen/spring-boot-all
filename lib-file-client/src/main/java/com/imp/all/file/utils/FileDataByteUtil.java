package com.imp.all.file.utils;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.MessageLite;
import com.imp.all.file.type.FileCmdType;

import java.io.IOException;

/**
 */
public class FileDataByteUtil {

    /**
     * @param sq      序号(2字节)
     * @param cmdType 指令(1字节)
     * @param data    PB数据
     * @return
     */
    public static byte[] toFileData(int sq, FileCmdType cmdType, MessageLite data) {
        try {
            int dataLen = data.getSerializedSize();
            byte[] msgData = new byte[dataLen + 3];
            msgData[0] = (byte) ((sq >> 8) & 0xFF);
            msgData[1] = (byte) (sq & 0xFF);
            msgData[2] = cmdType.value();

            CodedOutputStream output = CodedOutputStream.newInstance(msgData, 3, dataLen);
            data.writeTo(output);
            output.checkNoSpaceLeft();
            return msgData;
        } catch (IOException var3) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", var3);
        }
    }

}

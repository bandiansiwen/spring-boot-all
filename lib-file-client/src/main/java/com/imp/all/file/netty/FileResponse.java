package com.imp.all.file.netty;

import com.imp.all.file.type.FileCmdType;
import com.imp.all.file.type.TransmissionType;
import com.imp.all.file.utils.ByteUtil;

import java.util.Arrays;

/**
 */
public class FileResponse {
    private byte mType;
    private byte[] mLengthArray;
    private byte[] mDataArray;
    private byte[] mCrcArray;
    private byte[] mPbData;

    public FileResponse(byte[] bytes) {
        if (bytes.length < 7) {
            return;
        }
        mType = bytes[0];
        mLengthArray = Arrays.copyOfRange(bytes, 1, 5);
        mDataArray = Arrays.copyOfRange(bytes, 5, bytes.length - 2);
        mCrcArray = Arrays.copyOfRange(bytes, bytes.length - 2, bytes.length);
    }

    public TransmissionType getType() {
        return TransmissionType.valueOf(mType);
    }

    public byte[] getData() {
        return mDataArray;
    }

    public int getSq() {
        return ByteUtil.byteArrayToInt(Arrays.copyOfRange(mDataArray, 0, 2));
    }

    public FileCmdType getCmd() {
        return FileCmdType.valueOf(mDataArray[2]);
    }

    public byte[] getPbData() {
        if (getType() == TransmissionType.PB && mPbData == null) {
            mPbData = Arrays.copyOfRange(mDataArray, 3, mDataArray.length);
        }
        return mPbData;
    }

}

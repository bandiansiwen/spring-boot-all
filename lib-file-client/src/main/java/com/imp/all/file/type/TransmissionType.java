package com.imp.all.file.type;

/**
 * @author LIANGYJ3
 * @date 2016-5-7
 * <p/>
 * 传输格式类型
 */
public enum TransmissionType {
    BINARY((byte) 0),
    JSON((byte) 32),
    PB((byte) 33),
    //json with gzip and aes encrypt4IM
    JSON_ENCRYPT((byte) 42),
    HEARTBEAT_REQ((byte) 100),
    HEARTBEAT_RSP((byte) 101),
    NEGOTIATE_REQ((byte) 102),
    NEGOTIATE_RSP((byte) 103);

    private final byte mByte;

    TransmissionType(byte b) {
        mByte = b;
    }

    public TransmissionType getType(byte b) {

        return values()[b];
    }

    public byte getByte() {
        return mByte;
    }

    public static TransmissionType valueOf(byte b) {
        switch (b) {
            case 0:
                return BINARY;
            case 32:
                return JSON;
            case 33:
                return PB;
            case 42:
                return JSON_ENCRYPT;
            case 100:
                return HEARTBEAT_REQ;
            case 101:
                return HEARTBEAT_RSP;
            case 102:
                return NEGOTIATE_REQ;
            case 103:
                return NEGOTIATE_RSP;
            default:
                return JSON;
        }
    }
}

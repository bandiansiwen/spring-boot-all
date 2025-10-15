package com.imp.all.file.utils;

import com.google.protobuf.Descriptors;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.TextFormat;

import java.io.IOException;
import java.util.Map;

public class PBFormat {

    public static String format(MessageOrBuilder fields) {

        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<Descriptors.FieldDescriptor, Object> field : fields.getAllFields().entrySet()) {

            try {
                if (field.getKey().getType() != Descriptors.FieldDescriptor.Type.BYTES) {
                    TextFormat.printField(field.getKey(), field.getValue(), stringBuffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "\n======PB报文=====\n" + stringBuffer + "=======PB报文END=======";
    }
}

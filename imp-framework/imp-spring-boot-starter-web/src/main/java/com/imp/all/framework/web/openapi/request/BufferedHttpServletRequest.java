package com.imp.all.framework.web.openapi.request;

import com.imp.all.framework.web.openapi.utils.Encodes;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author Longlin
 * @date 2023/1/16 16:29
 * @description
 */
public class BufferedHttpServletRequest extends HttpServletRequestWrapper {

    private ByteArrayOutputStream cachedBytes;

    public BufferedHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null)
            cacheInputStream();

        return new CachedServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException{
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {
        /* Cache the inputstream in order to read it multiple times. For
         * convenience, I use apache.commons IOUtils
         */
        cachedBytes = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedBytes);
    }

    /* An inputstream which reads the cached request body */
    public class CachedServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream input;

        public CachedServletInputStream() {
            /* create a new input stream from the cached request body */
            input = new ByteArrayInputStream(cachedBytes.toByteArray());
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }

        @Override
        public int read() throws IOException {
            return input.read();
        }
    }
    public String getRequestBody(InputStream stream) throws IOException {
        String line = StringUtils.EMPTY;
        StringBuilder body = new StringBuilder();
        int counter = 0;


        // 读取POST提交的数据内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while ((line = reader.readLine()) != null) {
//            if (counter > 0) {
//                body.append("rn");
//            }
            body.append(line);
            counter++;
        }
        reader.close();
        return body.toString();
    }

    public HashMap<String, String[]> parseQueryString(String s) {
        String valArray[] = null;
        if (s == null) {
            throw new IllegalArgumentException();
        }
        HashMap<String, String[]> ht = new HashMap<String, String[]>();
        StringTokenizer st = new StringTokenizer(s, "&");
        while (st.hasMoreTokens()) {
            String pair = (String) st.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                continue;
            }
            String key = pair.substring(0, pos);
            String val = pair.substring(pos + 1, pair.length());
            if (ht.containsKey(key)) {
                String oldVals[] = (String[]) ht.get(key);
                valArray = new String[oldVals.length + 1];
                for (int i = 0; i < oldVals.length; i++) {
                    valArray[i] = oldVals[i];
                }
                valArray[oldVals.length] = decodeValue(val);
            } else {
                valArray = new String[1];
                valArray[0] = decodeValue(val);
            }
            ht.put(key, valArray);
        }
        return ht;
    }


    private static byte[] readBytes(InputStream in) throws IOException {
        BufferedInputStream bufin = new BufferedInputStream(in);
        final int buffSize = 1024;
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);


        byte[] temp = new byte[buffSize];
        int size = 0;
        while ((size = bufin.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        out.flush();
        byte[] content = out.toByteArray();
        bufin.close();
        out.close();
        return content;
    }


    /**
     * 自定义解码函数
     *
     * @param value
     * @return
     */
    private String decodeValue(String value) {
        if (value.contains("%u")) {
            return Encodes.urlDecode(value);
        } else {
            try {
                return URLDecoder.decode(value, CharEncoding.UTF_8);
            } catch (UnsupportedEncodingException e) {
                return StringUtils.EMPTY;// 非UTF-8编码
            }
        }
    }

}

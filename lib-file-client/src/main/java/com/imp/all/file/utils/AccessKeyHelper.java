package com.imp.all.file.utils;


import com.imp.all.file.model.Bucket;

public class AccessKeyHelper {

    public static String getAccessKey(Bucket bucket, String fileKey, Integer accessTime) {
        return McFileMD5.getStrMd5(bucket.getId() + bucket.getSalt() + bucket.getRole() + fileKey + accessTime);
    }
}

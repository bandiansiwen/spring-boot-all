package com.imp.all.file.error;

import java.util.HashMap;
import java.util.Map;

public class FileErrorMap {

    private static final Map<Integer, String> map = new HashMap<>();

    public static final int ERROR_CODE_LOCAL = -1;

    public static final int ERROR_CODE_EXPIRE = 18023;
    public static final int ERROR_CODE_ERROR_BUCKET_ID = 18009;
    public static final int ERROR_CODE_FILE5_EXPIRE = 18018;
    public static final int ERROR_CODE_PULL_DATA_REQUEST_PARAM_ERROR = 18031;

    /**
     * 客户端错误码
     */
    public static final int ERROR_CODE_UPLOAD_FAILED = 1100;
    public static final int ERROR_CODE_UPLOAD_TIMEOUT = 1101;
    public static final int ERROR_CODE_UPLOAD_READ_FAILED = 1102;
    public static final int ERROR_CODE_DOWNLOAD_FAILED = 1103;
    public static final int ERROR_CODE_DOWNLOAD_TIMEOUT = 1104;
    public static final int ERROR_CODE_DOWNLOAD_WRITE_FAILED = 1105;
    public static final int ERROR_CODE_DOWNLOAD_THUMBNAIL_FAILED = 1106;
    public static final int ERROR_CODE_DOWNLOAD_COPY_FAILED = 1107;

    static void init() {
        if (map.isEmpty()) {
            map.put(ERROR_CODE_LOCAL, "本地错误");

            map.put(18001, "未知异常");
            map.put(18002, "非法请求");
            map.put(18003, "参数有误");
            map.put(18004, "数据库操作失败");
            map.put(18005, "磁盘空间不足");
            map.put(18006, "没有可用空间id");
            map.put(18007, "没有可用空间id");
            map.put(18008, "非法空间");
            map.put(ERROR_CODE_ERROR_BUCKET_ID, "非法空间角色");
            map.put(18010, "token超时");
            map.put(18011, "md5错误");
            map.put(18012, "更新配置失败");
            map.put(18013, "pb 格式错误");
            map.put(18014, "内存分配失败");
            map.put(18015, "任务正在上传，不能下载");
            map.put(18016, "任务不在任务列表中");
            map.put(18017, "保存数据失败");
//            map.put(18018, "文件信息不在数据库中");
            map.put(18018, "文件已过期或已被清理");
            map.put(18019, "文件状态异常");
            map.put(18020, "打开文件失败");
            map.put(18021, "文件查找失败");
            map.put(18022, "文件大小为空");
            map.put(18023, "文件过期");
            map.put(18024, "加载文件失败");
            map.put(18031, "下载文件请求参数错误");
            map.put(ERROR_CODE_DOWNLOAD_THUMBNAIL_FAILED, "缩略图不存在，请用原图下载");
        }
    }

    public static String getMsg(int code) {
        init();
        return map.get(code);
    }
}

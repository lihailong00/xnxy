package com.lee.xnxydev.util;

import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 晓龙coding
 */
@Component
public class QiNiuUtil {
    @Value("${qiNiu.url}")
    public String url;
    public String getUrl() {
        return url;
    }

    @Value("${qiNiu.accessKey}")
    private String accessKey;
    @Value("${qiNiu.accessSecretKey}")
    private String accessSecretKey;

    @Value("${qiNiu.bucketName}")
    private String bucket;

    public boolean upload(MultipartFile file,String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, fileName, upToken);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
package com.alone.common.mns;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DownloadFileRequest;
import com.aliyun.oss.model.DownloadFileResult;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author zhouxianjun(Alone)
 * @ClassName:
 * @Description: 阿里云 OSS 文件存储管理器
 * @date 2017/2/27 16:08
 */
@Slf4j
public class OssStoreManager {
    /** accessId */
    @Setter
    private String accessId;

    /** accessKey */
    @Setter
    private String accessKey;

    /** accountEndpoint */
    @Setter
    private String accountEndpoint;

    @Setter
    @Getter
    private String bucket = "intf-files";

    @Setter
    private int downloadTaskNum = 3;

    private OSSClient client;

    public OSSClient getClient() {
        if (client == null) {
            client = new OSSClient(accountEndpoint, accessId, accessKey);
        }
        Assert.notNull(bucket, "bucket 不能为空");
        return client;
    }

    public PutObjectResult upload(String pathName, InputStream inputStream) {
        return getClient().putObject(bucket, pathName, inputStream);
    }

    public PutObjectResult upload(String pathName, byte[] bytes) {
        return getClient().putObject(bucket, pathName, new ByteArrayInputStream(bytes));
    }

    public PutObjectResult upload(String pathName, String content) {
        return upload(pathName, content.getBytes());
    }

    public PutObjectResult upload(String pathName, File file) {
        return getClient().putObject(bucket, pathName, file);
    }

    public DownloadFileResult download(String pathName, String save) throws Throwable {
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucket, pathName);
        if (StringUtils.isNotEmpty(save)) {
            downloadFileRequest.setDownloadFile(save);
        }
        downloadFileRequest.setTaskNum(downloadTaskNum);
        downloadFileRequest.setEnableCheckpoint(true);
        return getClient().downloadFile(downloadFileRequest);
    }

    public byte[] download(String pathName) throws IOException {
        OSSObject ossObject = getClient().getObject(bucket, pathName);
        InputStream in = ossObject.getObjectContent();
        byte[] result = StreamUtils.copyToByteArray(in);
        in.close();
        return result;
    }

    public String makeSecurityTokenUrl(String pathName, int expiration) {
        URL url = getClient().generatePresignedUrl(bucket, pathName, DateUtils.addMilliseconds(new Date(), expiration));
        return url.toString();
    }
}

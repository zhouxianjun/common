package com.alone.common.mns;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.alone.common.GsonStatis;
import com.alone.common.dto.Result;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 17-8-8 下午4:42
 */
@Slf4j
public class MnsMessageManager {
    /** accessId */
    @Setter
    private String accessId;

    /** accessKey */
    @Setter
    private String accessKey;

    @Setter
    private String region;

    private IAcsClient acsClient = null;

    public MnsMessageManager(String accessId, String accessKey, String region) {
        this.accessId = accessId;
        this.accessKey = accessKey;
        this.region = region;
    }

    public MnsMessageManager(String accessId, String accessKey) {
        this.accessId = accessId;
        this.accessKey = accessKey;
        this.region = "cn-hangzhou";
    }

    private IAcsClient getAcsClient() throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile(region, accessId, accessKey);
        DefaultProfile.addEndpoint(region, region, "Dysmsapi", "dysmsapi.aliyuncs.com");
        this.acsClient = new DefaultAcsClient(profile);
        return this.acsClient;
    }

    /**
     * 新阿里云短信发送
     * @param sign 短信签名
     * @param template 短信模板
     * @param params 短信参数
     * @param phones 接收手机号
     * @return
     */
    public Result<String> send(String sign, String template, Map<String, String> params, String... phones) {
        String phone = StringUtils.join(phones, ",");
        try {
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(phone);
            request.setSignName(sign);
            request.setTemplateCode(template);
            if (params != null && !params.isEmpty()) {
                request.setTemplateParam(GsonStatis.instance().toJson(params));
            }
            SendSmsResponse response = getAcsClient().getAcsResponse(request);
            Result<String> result = new Result<>();
            result.setValue(response.getCode());
            result.setCode(response.getCode() != null && response.getCode().equals("OK") ? Result.SUCCESS : Result.FAIL);
            return result;
        } catch (Throwable e) {
            log.error("发送短信异常,手机号:" + phone, e);
            return Result.fail(e.getMessage());
        }
    }
}

/*
package com.zsyc.framework.video;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
public class UpLoadVedio {


    */
/**
     * 获取视频上传地址和凭证
     * @param client 发送请求客户端
     * @return CreateUploadVideoResponse 获取视频上传地址和凭证响应数据
     * @throws Exception
     *//*

    public static CreateUploadVideoResponse createUploadVideo(DefaultAcsClient client) throws Exception {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle("20190313test");
        request.setFileName("D:\\1.mp4");
       */
/* JSONObject userData = new JSONObject();
        JSONObject messageCallback = new JSONObject();
        messageCallback.put("CallbackURL", "http://xxxxx");
        messageCallback.put("CallbackType", "http");
        userData.put("MessageCallback", messageCallback.toJSONString());
        JSONObject extend = new JSONObject();
        extend.put("MyId", "user-defined-id");
        userData.put("Extend", extend.toJSONString());
        request.setUserData(userData.toJSONString());*//*

       request.setTemplateGroupId("0c2c5aa77fc38fc93e7376f427655718");
        return client.getAcsResponse(request);
    }
    // 请求示例
    public static void main(String[] argv) {
        Init init=new Init();
        DefaultAcsClient client = null;
        try {
            client = init.initVodClient("LTAIwNl2EMpYg3rk", "H3BaJcTcBzpbeGHVELkei32HHdylPk");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        CreateUploadVideoResponse response = new CreateUploadVideoResponse();
        try {
            response = createUploadVideo(client);
            System.out.print("VideoId = " + response.getVideoId() + "\n");
            System.out.print("UploadAddress = " + response.getUploadAddress() + "\n");
            System.out.print("UploadAuth = " + response.getUploadAuth() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
*/

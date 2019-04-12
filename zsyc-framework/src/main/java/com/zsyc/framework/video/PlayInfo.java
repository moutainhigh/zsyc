/*
package com.zsyc.framework.video;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;

public class PlayInfo {


    */
/*获取播放地址函数*//*

    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("5924c141bad9489192261ee3aec7b671");
        return client.getAcsResponse(request);
    }
    */
/*以下为调用示例*//*

    public static void main(String[] argv) {
        DefaultAcsClient client = null;
        try {
            client = Init.initVodClient("LTAIwNl2EMpYg3rk", "H3BaJcTcBzpbeGHVELkei32HHdylPk");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            response = getPlayInfo(client);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
*/

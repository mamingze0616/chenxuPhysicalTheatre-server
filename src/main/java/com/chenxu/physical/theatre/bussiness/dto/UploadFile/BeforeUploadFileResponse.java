package com.chenxu.physical.theatre.bussiness.dto.UploadFile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

/**
 * @author mamingze
 * @version 1.0
 * @title BeforeUploadFileResponse
 * @description
 * @create 2025/5/28 17:21
 */
@Data
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BeforeUploadFileResponse {
    //
    int errcode;
    String errmsg;
    //https://cos.ap-shanghai.myqcloud.com/8888-werun-id-1300000000/web/test.zip
    String url;
    //"token": "cbl3vhld2EFYnNHa0ndCvDrmd24d6GPa9",
    String token;
    //"authorization": "q-sign-algorithm=sha1&q-ak=AKIDFnbuKfk_qeIIhWcEFWN",
    String authorization;
    //"file_id": "cloud://werun-id.8888-werun-id-1300000000/web/test.zip",
    String fileId;
    //"cos_file_id": "HIqJeJmHDQoHMIlxshGWJR2mdCaaJZ96bxm=="
    String cosFileId;
}

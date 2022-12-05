package com.ld.poetry.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class QiniuUtil {

    public static String getToken(String key) {
        Auth auth = Auth.create(CommonConst.ACCESS_KEY, CommonConst.SECRET_KEY);
        return auth.uploadToken(CommonConst.BUCKET, key);
    }

    public static void deleteFile(List<String> files) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        Auth auth = Auth.create(CommonConst.ACCESS_KEY, CommonConst.SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000
            String[] keyList = files.toArray(new String[0]);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(CommonConst.BUCKET, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                if (status.code == 200) {
                    log.info(key + "：删除成功！");
                } else {
                    log.error(key + "：" + status.data.error);
                }
            }
        } catch (QiniuException ex) {
            log.error(ex.response.toString());
        }
    }
}

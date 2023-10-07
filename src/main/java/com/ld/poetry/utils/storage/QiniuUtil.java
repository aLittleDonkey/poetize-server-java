package com.ld.poetry.utils.storage;

import com.ld.poetry.entity.Resource;
import com.ld.poetry.service.ResourceService;
import com.ld.poetry.utils.CommonConst;
import com.ld.poetry.vo.FileVO;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name = "qiniu.enable", havingValue = "true")
public class QiniuUtil implements StoreService {

    /**
     * 七牛云
     */
    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.downloadUrl}")
    private String downloadUrl;

    private static final long EXPIRE_SECONDS = 60L;
    private static final long F_SIZE_LIMIT = 20971520L;

    @Autowired
    private ResourceService resourceService;

    public String getToken(String key) {
        StringMap putPolicy = new StringMap();
        putPolicy.put("fsizeLimit", F_SIZE_LIMIT);
        Auth auth = Auth.create(accessKey, secretKey);
        return auth.uploadToken(bucket, key, EXPIRE_SECONDS, putPolicy);
    }

    @Override
    public void deleteFile(List<String> files) {
        if (CollectionUtils.isEmpty(files)) {
            return;
        }

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000
            String[] keyList = files.stream().map(path -> path.replace(downloadUrl, "")).toArray(String[]::new);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                if (status.code == 200) {
                    log.info("文件删除成功：" + key);
                    resourceService.lambdaUpdate().eq(Resource::getPath, downloadUrl + key).remove();
                } else {
                    log.error("文件删除失败：" + key + "，原因：" + status.data.error);
                }
            }
        } catch (QiniuException ex) {
            log.error("文件删除失败：" + ex.response.toString());
        }
    }

    @Override
    public FileVO saveFile(FileVO fileVO) {
        return null;
    }

    @Override
    public String getStoreName() {
        return StoreEnum.QINIU.getCode();
    }

    public Map<String, Map<String, String>> getFileInfo(List<String> files) {
        Map<String, Map<String, String>> result = new HashMap<>();

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000
            String[] keyList = files.toArray(new String[0]);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addStatOps(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                if (status.code == 200) {
                    //文件存在
                    Map<String, String> info = new HashMap<>();
                    info.put("size", String.valueOf(status.data.fsize));
                    info.put("mimeType", status.data.mimeType);
                    result.put(key, info);
                } else {
                    log.error(key + "：" + status.data.error);
                }
            }
        } catch (QiniuException ex) {
            log.error(ex.response.toString());
        }

        return result;
    }

    public void saveFileInfo() {
        List<Resource> resourceList = resourceService.lambdaQuery().select(Resource::getPath).list();
        List<String> paths = resourceList.stream().map(Resource::getPath).collect(Collectors.toList());

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);

        List<Resource> resources = new ArrayList<>();

        while (fileListIterator.hasNext()) {
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if (item.fsize != 0L && !paths.contains(downloadUrl + item.key)) {
                    Resource re = new Resource();
                    re.setPath(downloadUrl + item.key);
                    re.setType(CommonConst.PATH_TYPE_ASSETS);
                    re.setSize(Integer.valueOf(Long.toString(item.fsize)));
                    re.setMimeType(item.mimeType);
                    re.setStoreType(StoreEnum.QINIU.getCode());
                    re.setUserId(CommonConst.ADMIN_USER_ID);
                    resources.add(re);
                }
            }
        }

        if (!CollectionUtils.isEmpty(resources)) {
            resourceService.saveBatch(resources);
            System.out.println("保存数量：" + resources.size());
        }
        System.out.println("同步完成");
    }
}

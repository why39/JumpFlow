package com.hxy.provenance.logparser;

import com.hxy.modules.common.utils.JsonUtil;
import com.hxy.modules.common.utils.MD5;
import com.hxy.provenance.neo4j.utils.http.HttpUtils;
import com.qiniu.util.Json;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对接数据汇聚平台
 */
@Service
public class DataPlatformService {

    Logger logger = LoggerFactory.getLogger(DataPlatformService.class);


    @Value("${spring.data.platform.url}")
    String DATA_URL;

    @Value("${spring.data.platform.api-key}")
    String DATA_API_KEY;

    @Value("${spring.data.platform.api-id}")
    String DATA_API_ID;

    @Value("${spring.data.platform.random-str}")
    String DATA_RANDOM_STR;

    @Autowired
    GJAJService caseService;

    /**
     * @param start 2019-11-08 15:10:42
     * @param end
     * @return
     */
    public List<GJAJEntity> queryAJ(String start, String end) {
        List<GJAJEntity> gjajEntityList = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        String params = "queryStartDate=" + start + "&queryEndDate=" + end;
        String signature = MD5.MD5Encode((params + "&" + DATA_RANDOM_STR + "&" + DATA_API_KEY).replace(" ", "%20"));

//        HttpUtils.sendGet(DATA_URL + "/api/proc/aj",)
        Request request = new Request.Builder()
                .url(DATA_URL + "/api/proc/aj?" + params)
                .get()
                .addHeader("X-Api-Id", DATA_API_ID)
                .addHeader("X-Api-Temp", DATA_RANDOM_STR)
                .addHeader("X-Api-Signature", signature)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("accept", "*/*")
                .addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                DataPlatformAJEntity resultEntity = Json.decode(response.body().string(), DataPlatformAJEntity.class);
                if (resultEntity != null && !CollectionUtils.isEmpty(resultEntity.data)) {
                    for (DataPlatformAJEntity.Item entity : resultEntity.data) {
                        GJAJEntity gjajEntity = new GJAJEntity();
                        gjajEntity.setBMSAH(entity.getBmsah());
                        gjajEntity.setAJLB_MC(entity.getAjlbmc());
                        gjajEntity.setAJMC(entity.getAjmc());
                        gjajEntity.setTYSAH(entity.getTysah());
                        gjajEntity.setCBDW_MC(entity.getCbdwmc());
                        caseService.saveAJ(gjajEntity);
                        gjajEntityList.add(gjajEntity);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("DATAPlatform...." + gjajEntityList.size());
            return gjajEntityList;
        }

    }

    /**
     * @param start 2019-11-08 15:10:42
     * @param end
     * @return
     */
    public List<GJRZEntity> queryRZ(String start, String end) {
        List<GJRZEntity> gjrzEntityList = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        String params = "queryStartDate=" + start + "&queryEndDate=" + end;
        String signature = MD5.MD5Encode((params + "&" + DATA_RANDOM_STR + "&" + DATA_API_KEY).replace(" ", "%20"));

        Request request = new Request.Builder()
                .url(DATA_URL + "/api/logs/oper?" + params)
                .get()
                .addHeader("X-Api-Id", DATA_API_ID)
                .addHeader("X-Api-Temp", DATA_RANDOM_STR)
                .addHeader("X-Api-Signature", signature)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("accept", "*/*")
                .addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                DataPlatformRZEntity resultEntity = Json.decode(response.body().string(), DataPlatformRZEntity.class);
                if (resultEntity != null && !CollectionUtils.isEmpty(resultEntity.data)) {
                    for (DataPlatformRZEntity.Item entity : resultEntity.data) {
                        GJRZEntity gjrzEntity = new GJRZEntity();
                        gjrzEntity.setBMSAH(entity.getBmsah());
                        gjrzEntity.setCZRM(entity.getCzrm());
                        gjrzEntity.setEJFL(entity.getEjfl());
                        gjrzEntity.setID(entity.getId());
                        gjrzEntity.setRZMS(entity.getRzms());
                        gjrzEntity.setZHXGSJ(entity.getZhxgsj());
                        caseService.saveRZ(gjrzEntity);
                        gjrzEntityList.add(gjrzEntity);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("DATAPlatform...." + gjrzEntityList.size());
            return gjrzEntityList;
        }

    }

}

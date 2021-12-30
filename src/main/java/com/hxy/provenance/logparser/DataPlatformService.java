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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        String params = "queryStartDate=" + start + "&queryEndDate=" + end;
        logger.info("params", params);
        System.out.println("params" + params);
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
        logger.info("api-id", DATA_API_ID);
        System.out.println("api-id" + DATA_API_ID);
        logger.info("api-temp", DATA_RANDOM_STR);
        System.out.println("api-temp" + DATA_RANDOM_STR);
        logger.info("api-sign", signature);
        System.out.println("api-sign" + signature);
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String body = response.body().string();
                logger.info("DATAPlatform00000....querAJ resultEntity : " + body);
                DataPlatformAJEntity resultEntity = Json.decode(body, DataPlatformAJEntity.class);
                if (resultEntity != null && !CollectionUtils.isEmpty(resultEntity.data)) {

                    for (DataPlatformAJEntity.Item entity : resultEntity.data) {
                        GJAJEntity gjajEntity = new GJAJEntity();

                        gjajEntity.setBMSAH(entity.getBmsah());
                        gjajEntity.setTYSAH(entity.getTysah());
                        gjajEntity.setAJMC(entity.getAjmc());
                        gjajEntity.setCBDW_MC(entity.getCbdwmc());
                        gjajEntity.setCJSJ(entity.getCjsj());
                        gjajEntity.setAJLB_MC(entity.getAjlbmc());
                        //20210517 Guizhou
                        gjajEntity.setYWMC(entity.getYwmc());
                        gjajEntity.setCBJCG(entity.getCbjcg());
                        gjajEntity.setCBJCGSF(entity.getCbjcgsf());
                        gjajEntity.setBATDMC(entity.getBatdmc());
                        System.out.println("ywmc" + entity.getYwmc());
                        logger.info("ywmc" + entity.getYwmc());
                        System.out.println("cbjcg" + entity.getCbjcg());
                        logger.info("cbjcg" + entity.getCbjcg());
                        System.out.println("cbjcgsf" + entity.getCbjcgsf());
                        logger.info("cbjcgsf" + entity.getCbjcgsf());
                        System.out.println("batdmc" + entity.getBatdmc());
                        logger.info("batdmc" + entity.getBatdmc());
                        gjajEntity.setIS_COMPLETE(0);

                        caseService.saveAJ(gjajEntity);
                        gjajEntityList.add(gjajEntity);
                    }
                }else {
                    logger.info("queryAJ----------------------response body is empty");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("DATAPlatform....queryAJ error" + e.toString());
        } finally {
            logger.info("DATAPlatform....queryAJ" + gjajEntityList.size());
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

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

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
                String body = response.body().string();
                logger.info("DATAPlatform1111....querRZ body : " + body);
                DataPlatformRZEntity resultEntity = Json.decode(body, DataPlatformRZEntity.class);
                if (resultEntity != null && !CollectionUtils.isEmpty(resultEntity.data)) {
                    logger.info("DATAPlatform1111....querRZ resultEntity : " + resultEntity.data);
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
                } else {
                    logger.info("queryRZ----------------------response body is empty");
                }
            }

        } catch (Exception e) {
            logger.info("DATAPlatform1111....querRZ resultEntity : " + e.toString());
            e.printStackTrace();
        } finally {
            logger.info("DATAPlatform....queryRZ" + gjrzEntityList.size());
            return gjrzEntityList;
        }

    }

    /**
     * 查询流程环节
     *
     * @param start 2019-11-08 15:10:42
     * @param end
     * @return
     */
    public List<GJRZEntity> queryLCHJ(String start, String end) {
        List<GJRZEntity> gjrzEntityList = new ArrayList<>();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        String params = "queryStartDate=" + start + "&queryEndDate=" + end;
        String signature = MD5.MD5Encode((params + "&" + DATA_RANDOM_STR + "&" + DATA_API_KEY).replace(" ", "%20"));

        Request request = new Request.Builder()
                .url(DATA_URL + "/api/proc/inst/nodes?" + params)
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
                DataPlatformJDEntity resultEntity = Json.decode(response.body().string(), DataPlatformJDEntity.class);
                logger.info("DATAPlatform queryLCHJ....queryLCHJ resultEntity : " + resultEntity.data);
                if (resultEntity != null && !CollectionUtils.isEmpty(resultEntity.data)) {
                    logger.info("DATAPlatform....queryLCHJ -------datadatadata : " + resultEntity.data);
                    for (DataPlatformJDEntity.Item entity : resultEntity.data) {
                        GJRZEntity gjrzEntity = new GJRZEntity();
                        gjrzEntity.setBMSAH(entity.getBmsah());
                        gjrzEntity.setCZRM(entity.getJdzxzxm());
                        gjrzEntity.setEJFL("TYYW_LCBA_YW_BL_LC_JD_TABLE"); //从其他表中
                        gjrzEntity.setID(entity.getBmsah() + "-" +String.valueOf(Math.random() * 100000));
                        gjrzEntity.setRZMS(entity.getLcjdmc());
                        gjrzEntity.setZHXGSJ(entity.getJdjrsj());
                        caseService.saveRZ(gjrzEntity);
                        gjrzEntityList.add(gjrzEntity);
                        logger.info("DATAPlatform queryLCHJ....saveLCHJ : " + gjrzEntity.getID());

                    }
                } else {
                    logger.info("DATAPlatform queryLCHJ....queryLCHJ data is empty");
                }
            } else {
                logger.info("DATAPlatform queryLCHJ....queryLCHJ failed : " + response.body().string());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("DATAPlatform queryLCHJ....queryLCHJ error : " + e.toString());
        } finally {
            logger.info("DATAPlatform queryLCHJ...." + gjrzEntityList.size());
            return gjrzEntityList;
        }
    }

}

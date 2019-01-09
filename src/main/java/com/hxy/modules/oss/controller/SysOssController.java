package com.hxy.modules.oss.controller;

import com.alibaba.fastjson.JSON;
import com.hxy.config.SpringConfig;
import com.hxy.modules.common.common.Constant;
import com.hxy.modules.common.common.RRException;
import com.hxy.modules.common.utils.PageUtils;
import com.hxy.modules.common.utils.Query;
import com.hxy.modules.common.utils.Result;
import com.hxy.modules.common.validator.ValidatorUtils;
import com.hxy.modules.common.validator.group.AliyunGroup;
import com.hxy.modules.common.validator.group.QcloudGroup;
import com.hxy.modules.common.validator.group.QiniuGroup;
import com.hxy.modules.oss.cloud.CloudStorageConfig;
import com.hxy.modules.oss.cloud.OSSFactory;
import com.hxy.modules.oss.entity.SysOssEntity;
import com.hxy.modules.sys.service.SysConfigService;
import com.hxy.modules.oss.service.SysOssService;
import com.hxy.modules.utils.FastDFSClient;
import com.hxy.modules.utils.FastDFSFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 类SysOssController的功能描述:
 * 文件上传
 *
 * @auther hxy
 * @date 2017-08-25 16:17:21
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {

    @Autowired
    SpringConfig springConfig;

    @Autowired
    private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;

    private final static String KEY = Constant.CLOUD_STORAGE_CONFIG_KEY;
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:oss:all")
    public Result list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<SysOssEntity> sysOssList = sysOssService.queryList(query);
        int total = sysOssService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(sysOssList, total, query.getLimit(), query.getPage());

        return Result.ok().put("page", pageUtil);
    }

    /**
     * 云存储配置信息
     */
    @RequestMapping("/config")
    @RequiresPermissions("sys:oss:all")
    public Result config() {
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return Result.ok().put("config", config);
    }


    /**
     * 保存云存储配置信息
     */
    @RequestMapping("/saveConfig")
    @RequiresPermissions("sys:oss:all")
    public Result saveConfig(@RequestBody CloudStorageConfig config) {
        //校验类型
        ValidatorUtils.validateEntity(config);

        if (config.getType() == Constant.CloudService.QINIU.getValue()) {
            //校验七牛数据
            ValidatorUtils.validateEntity(config, QiniuGroup.class);
        } else if (config.getType() == Constant.CloudService.ALIYUN.getValue()) {
            //校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        } else if (config.getType() == Constant.CloudService.QCLOUD.getValue()) {
            //校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }


        sysConfigService.updateValueByKey(KEY, JSON.toJSONString(config));

        return Result.ok();
    }

    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    @RequiresPermissions("sys:oss:all")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String url = OSSFactory.build().upload(file.getBytes());
        //保存文件信息
        SysOssEntity ossEntity = new SysOssEntity();
        ossEntity.setUrl(url);
        ossEntity.setCreateDate(new Date());
        sysOssService.save(ossEntity);
        return Result.ok().put("url", url);
    }

    @RequestMapping("/fileupload")
    @RequiresPermissions("sys:oss:all")
    public Result handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();

        String url = null;
        try {
            url = saveFileFDFS(file);
            uploadFileLocal(file);

            //保存文件信息
            SysOssEntity ossEntity = new SysOssEntity();
            ossEntity.setUrl(url);
            ossEntity.setCreateDate(new Date());
            sysOssService.save(ossEntity);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.ok().put("url", url);
    }

    /**
     * 上传文件到fastdfs
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String saveFileFDFS(MultipartFile multipartFile) throws IOException {
        String[] fileAbsolutePath = {};
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream = multipartFile.getInputStream();
        if (inputStream != null) {
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file);  //upload to fastdfs
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("upload file failed,please upload again!");
        }
        if (fileAbsolutePath == null) {
            logger.error("upload file failed,please upload again!");
        }
        String path = FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
        return path;
    }

    /**
     * 上传文件到本地目录
     *
     * @param file
     */
    private void uploadFileLocal(MultipartFile file) {
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(springConfig.getLocation() + file.getOriginalFilename());
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        try {

            String fileUrl = request.getParameter("url");

            String fileName = fileUrl.split("group1/")[1];

            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            InputStream inputStream = FastDFSClient.downFile("group1", fileName);
            IOUtils.copy(inputStream, response.getOutputStream());

            logger.debug("Download the song successfully!");

        } catch (Exception e) {
            logger.debug("Download the song failed!");
        }
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:oss:all")
    public Result delete(@RequestBody Long[] ids) {
        sysOssService.deleteBatch(ids);
        return Result.ok();
    }

}

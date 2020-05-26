package com.hxy.modules.activiti.controller;

import com.hxy.modules.common.annotation.SysLog;
import com.hxy.modules.common.utils.ShiroUtils;
import com.hxy.modules.common.utils.UserUtils;
import com.zhuozhengsoft.pageoffice.DocumentVersion;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.wordreader.WordDocument;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

@Controller
public class Thymetest {
    @Value("${pageoffice.posyspath}")
    private String poSysPath;
    @Value("${pageoffice.popassword}")
    private String poPassWord;

    /**

     * 添加PageOffice的服务器端授权程序Servlet（必须）

     * @return

     */

    @Bean

    public ServletRegistrationBean servletRegistrationBean() {

        com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();

        //设置PageOffice注册成功后,license.lic文件存放的目录

        poserver.setSysPath(poSysPath);

        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);

        srb.addUrlMappings("/poserver.zz");

        srb.addUrlMappings("/posetup.exe");

        srb.addUrlMappings("/pageoffice.js");

        srb.addUrlMappings("/sealsetup.exe");

        return srb;

    }

    /**

     * 添加印章管理程序Servlet（可选）

     * @return

     */

    @Bean

    public ServletRegistrationBean servletRegistrationBean2() {

        com.zhuozhengsoft.pageoffice.poserver.AdminSeal adminSeal = new com.zhuozhengsoft.pageoffice.poserver.AdminSeal();

        adminSeal.setAdminPassword(poPassWord);//设置印章管理员admin的登录密码

        //设置印章数据库文件poseal.db存放目录，该文件在当前demo的“集成文件”夹中

        adminSeal.setSysPath(poSysPath);



        ServletRegistrationBean srb = new ServletRegistrationBean(adminSeal);

        srb.addUrlMappings("/adminseal.zz");

        srb.addUrlMappings("/sealimage.zz");

        srb.addUrlMappings("/loginseal.zz");

        return srb;

    }

    /**

     * 添加PageOffice的服务器端授权程序Servlet（必须）,第一个配置bean无效

     * @return

     */

    @Bean

    public ServletRegistrationBean servletRegistrationBean3() {

        com.zhuozhengsoft.pageoffice.poserver.Server ps = new com.zhuozhengsoft.pageoffice.poserver.Server();


        ps.setSysPath(poSysPath);



        ServletRegistrationBean srb = new ServletRegistrationBean(ps);

        srb.addUrlMappings("/plib/poserver.zz");

        srb.addUrlMappings("/plib/posetup.exe");

        srb.addUrlMappings("/plib/pageoffice.js");

        srb.addUrlMappings("/plib/sealsetup.exe");

        return srb;

    }

    /**

     * 打开已有文书模板

     * @return

     */

    @RequestMapping(value="/word", method= RequestMethod.GET)
    public String showWord(String fileName, HttpServletRequest request, HttpServletResponse response){
        String fn = fileName;
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);

        request.setAttribute("poCtrl", poCtrl);

        //设置服务页面
        poCtrl.setServerPage(request.getContextPath()+"/plib/poserver.zz");
        //添加保存按钮
        poCtrl.addCustomToolButton("保存并关闭","Save",1);
        //设置保存的action
        poCtrl.setSaveFilePage("savefile");
        //打开word
        //poCtrl.webOpen("d:\\test.wps",OpenModeType.docAdmin,"张三");
        poCtrl.webOpen("doc/sss.wps",OpenModeType.docAdmin,"张三");   //这里相对路径是读取target/doc下的文件，而不是src/doc下的文件
        poCtrl.setTagId("PageOfficeCtrl1"); //此行必须
        //--- PageOffice的调用代码 结束 -----


        return "pageoffice/word";

    }

    /**

     * 保存已有文档模板的修改（更新已有模板）

     * @return

     */
    @RequestMapping(value = "/savefile")

    public void saveFile(HttpServletRequest request, HttpServletResponse response){

        FileSaver fs = new FileSaver(request, response);

        //fs.saveToFile("d:\\" + fs.getFileName());
        try {
            fs.saveToFile(ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/instances/" + fs.getFileName());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        fs.close();
    }

    /**

     * 创建新文书模板

     * @return

     */

    @RequestMapping(value="/createword", method= RequestMethod.GET)
    public String createWord(String fileName, HttpServletRequest request, HttpServletResponse response){
        String fn = fileName;
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);

        request.setAttribute("poCtrl", poCtrl);

        //设置服务页面
        poCtrl.setServerPage(request.getContextPath()+"/plib/poserver.zz");
        //添加保存按钮
        poCtrl.addCustomToolButton("保存并关闭","Save",1);
        //设置保存的action
        poCtrl.setSaveFilePage("saveCreateFile?fileName="+fn);
        //创建word
        poCtrl.webCreateNew("张三", DocumentVersion.Word2007);
        poCtrl.setFileTitle(fn);
        poCtrl.setTagId("PageOfficeCtrl1"); //此行必须
        //--- PageOffice的调用代码 结束 -----


        return "pageoffice/word";

    }

    /**

     * 保存新建的文档模板

     * @return

     */
    @RequestMapping(value = "/saveCreateFile")

    public void saveCreateFile(String fileName, HttpServletRequest request, HttpServletResponse response){
        String fn = fileName;
        FileSaver fs = new FileSaver(request, response);
        //fs.saveToFile("d:\\" + fs.getFileName());
        try {
            fs.saveToFile(ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/instances/" + fs.getFileName());
            File file = new File(ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/" + fs.getFileName());
            //将文书模板名称从默认的newword.docx改为自定义的名称
            file.renameTo(new File(ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/" + fn + ".docx"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        fs.close();
    }

    @RequestMapping(value="/pageIndex", method= RequestMethod.GET)
    public String pageIndex(HttpServletRequest request, Map<String,Object> map){
        return "pageoffice/pageIndex";
    }


}

package com.hxy.modules.demo.callBack;

import com.hxy.modules.activiti.dto.ProcessTaskDto;
import org.springframework.stereotype.Component;

/**
 * 类的功能描述.
 *
 * @Auther hxy
 * @Date 2017/7/27
 */
@Component
public class CaseCallBack {

    public void caseBack(ProcessTaskDto processTaskDto){
        System.out.println("案件环节回调成功啦！！！！！！！");
    }
}

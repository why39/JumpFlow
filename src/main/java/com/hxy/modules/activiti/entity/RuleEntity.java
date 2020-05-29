package com.hxy.modules.activiti.entity;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


/**
 * 流程规则表
 *
 * @author why
 */
public class RuleEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键ID
    private String id;
    //部署id
    private String deploymentId;
    //模型id
    private String modelId;
    //名称
    @NotBlank(message = "规则内容不能为空")
    private String expression;
    //规则执行环节
    private String startEvent;
    //规则跳转环节
    private String endEvent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(String startEvent) {
        this.startEvent = startEvent;
    }

    public String getEndEvent() {
        return endEvent;
    }

    public void setEndEvent(String endEvent) {
        this.endEvent = endEvent;
    }




}

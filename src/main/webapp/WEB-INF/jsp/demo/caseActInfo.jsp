<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
<div id="actBusFields">
    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">标题:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input id="title" name="title" type="text" class="form-control" value="${caseEntity.title}"
                           readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">属于本院管辖:</label>
            <div class="col-sm-9">
            <span class="col-xs-11 block input-icon input-icon-right">
                <input id="rule_ourcase" name="rule_ourcase" type="text" class="form-control" value="${caseEntity.rule_ourcase}"
                       readonly/>
            </span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">案件环节详情说明:</label>
            <div class="col-sm-9">
            <span class="col-xs-11 block input-icon input-icon-right">
                <input id="leavewhy" name="leavewhy" type="text" class="form-control" value="${caseEntity.leavewhy}"/>
            </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">编号:</label>
            <div class="col-sm-9">
            <span class="col-xs-11 block input-icon input-icon-right">
                <input id="code" name="code" type="text" class="form-control" value="${caseEntity.code}" readonly/>
            </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">是否为单位犯罪:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input  id="prop_is_unit" name="prop_is_unit" type="text" class="form-control"
                           value="${prop_is_unit}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">是否共同犯罪》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input  id="prop_is_together" name="prop_is_together" type="text" class="form-control"
                           value="${prop_is_together}" readonly/>
                </span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">是否主犯:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input  id="prop_is_main" name="prop_is_main" type="text" class="form-control"
                           value="${prop_is_main}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">是否未成年人:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input  id="prop_is_juveniles" name="prop_is_juveniles" type="text" class="form-control"
                           value="${prop_is_juveniles}" readonly/>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件受理登记表》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="file_djb" name="djb" type="text" class="form-control"
                           value="${file_djb}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《补充移送材料通知书》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="file_bccl" name="bccl" type="text" class="form-control"
                           value="${file_bccl}" readonly/>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件审批表》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="file_spb" name="spb" type="text" class="form-control"
                           value="${file_spb}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《移送函》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="file_ysh" name="ysh" type="text" class="form-control"
                           value="${file_ysh}" readonly/>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件通知书》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="file_tzs" name="tzs" type="text" class="form-control"
                           value="${file_tzs}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员表决意见通知书》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="file_bjyj" name="bjyj" type="text" class="form-control"
                           value="${file_bjyj}" readonly/>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件处理结果通知书》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="file_cljg" name="cljg" type="text" class="form-control"
                           value="${file_cljg}" readonly/>
                </span>
            </div>
        </div>
    </div>

</div>
<c:if test="${flag == 2}">
    <%@include file="../activiti/processComSub.jsp" %>
</c:if>

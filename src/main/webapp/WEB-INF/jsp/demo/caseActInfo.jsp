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
        <%--<div class="form-group col-sm-6 col-md-5 ">--%>
            <%--<label class="col-sm-3 control-label no-padding-right">属于本院管辖:</label>--%>
            <%--<div class="col-sm-9">--%>
            <%--<span class="col-xs-11 block input-icon input-icon-right">--%>
                <%--<input id="guilty" name="guilty" type="text" class="form-control" value="${caseEntity.guilty}"--%>
                       <%--readonly/>--%>
            <%--</span>--%>
            <%--</div>--%>
        <%--</div>--%>
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
            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件受理登记表》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="djb" name="djb" type="text" class="form-control"
                           value="${djb}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《补充移送材料通知书》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="bccl" name="bccl" type="text" class="form-control"
                           value="${bccl}" readonly/>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件审批表》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="spb" name="spb" type="text" class="form-control"
                           value="${spb}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《移送函》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="ysh" name="ysh" type="text" class="form-control"
                           value="${ysh}" readonly/>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件通知书》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="tzs" name="tzs" type="text" class="form-control"
                           value="${tzs}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员表决意见通知书》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="bjyj" name="bjyj" type="text" class="form-control"
                           value="${bjyj}" readonly/>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件处理结果通知书》:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input placeholder="(文件链接)" id="cljg" name="cljg" type="text" class="form-control"
                           value="${cljg}" readonly/>
                </span>
            </div>
        </div>
    </div>

</div>
<c:if test="${flag == 2}">
    <%@include file="../activiti/processComSub.jsp" %>
</c:if>


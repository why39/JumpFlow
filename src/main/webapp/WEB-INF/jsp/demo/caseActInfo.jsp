
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
<div id="actBusFields">
    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">标题:</label>
            <div class="col-sm-9">
                <span class="col-xs-11 block input-icon input-icon-right">
                    <input id="title" name="title" type="text"  class="form-control" value="${caseEntity.title}" readonly/>
                </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">是否有罪:</label>
            <div class="col-sm-9">
            <span class="col-xs-11 block input-icon input-icon-right">
                <input id="guilty" name="guilty" type="text"  class="form-control" value="${caseEntity.guilty}" readonly/>
            </span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">案件环节详情说明:</label>
            <div class="col-sm-9">
            <span class="col-xs-11 block input-icon input-icon-right">
                <input id="leavewhy" name="leavewhy" type="text"  class="form-control" value="${caseEntity.leavewhy}" readonly/>
            </span>
            </div>
        </div>
        <div class="form-group col-sm-6 col-md-5 ">
            <label class="col-sm-3 control-label no-padding-right">编号:</label>
            <div class="col-sm-9">
            <span class="col-xs-11 block input-icon input-icon-right">
                <input id="code" name="code" type="text"  class="form-control" value="${caseEntity.code}" readonly/>
            </span>
            </div>
        </div>
    </div>
</div>
<c:if test="${flag == 2}">
    <%@include file="../activiti/processComSub.jsp"%>
</c:if>


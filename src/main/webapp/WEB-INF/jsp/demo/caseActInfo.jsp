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
    <label class="col-sm-3 control-label no-padding-right">编号:</label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="code" name="code" type="text" class="form-control" value="${caseEntity.code}" readonly/>
    </span>
    </div>
    </div>
<%--    <div class="form-group col-sm-6 col-md-5 ">--%>
<%--    <label class="col-sm-3 control-label no-padding-right">办案人:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_deal_name')">&#xe615;</i></label>--%>
<%--    <div class="col-sm-9">--%>
<%--    <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--    <input  id="prop_deal_name" name="prop_deal_name" type="text" class="form-control"--%>
<%--    value="${prop_deal_name}" />--%>
<%--    </span>--%>
<%--    </div>--%>
<%--    </div>--%>
    </div>

<%--    <div class="row">--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">是否未单位犯罪:<i class="layui-icon" onclick="traceProp('${caseEntity.prop_is_unit}','prop_is_unit')">&#xe615;</i></label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input  id="prop_is_unit" name="prop_is_unit" type="text" class="form-control"--%>
<%--                           value="${prop_is_unit}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">是否共同犯罪》:<i class="layui-icon" onclick="traceProp('${caseEntity.prop_is_together}','prop_is_together')">&#xe615;</i></label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input  id="prop_is_together" name="prop_is_together" type="text" class="form-control"--%>
<%--                           value="${prop_is_together}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    --%>
<%--    <div class="row">--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">是否主犯:<i class="layui-icon" onclick="traceProp('${caseEntity.prop_is_main}','prop_is_main')">&#xe615;</i></label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input  id="prop_is_main" name="prop_is_main" type="text" class="form-control"--%>
<%--                           value="${prop_is_main}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">是否未成年人:<i class="layui-icon" onclick="traceProp('${caseEntity.prop_is_juveniles}','prop_is_juveniles')">&#xe615;</i></label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input  id="prop_is_juveniles" name="prop_is_juveniles" type="text" class="form-control"--%>
<%--                           value="${prop_is_juveniles}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>

    <div class="row">
    <div class="form-group col-sm-6 col-md-5 ">
    <label class="col-sm-3 control-label no-padding-right">移送意见:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_move_reason')">&#xe615;</i></label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="prop_move_reason" name="prop_move_reason" type="text" class="form-control"
    value="${prop_move_reason}" />
    </span>
    </div>
    </div>
    <div class="form-group col-sm-6 col-md-5 ">
    <label class="col-sm-3 control-label no-padding-right">犯罪嫌疑人姓名/单位名称:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_main_name')">&#xe615;</i></label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="prop_main_name" name="prop_main_name" type="text" class="form-control"
    value="${prop_main_name}" />
    </span>
    </div>
    </div>
    </div>

    <div class="row">
    <div class="form-group col-sm-6 col-md-5 ">
    <label class="col-sm-3 control-label no-padding-right">犯罪嫌疑人作案时年龄:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_main_age')">&#xe615;</i></label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="prop_main_age" name="prop_main_age" type="text" class="form-control"
    value="${prop_main_age}" />
    </span>
    </div>
    </div>
    <div class="form-group col-sm-6 col-md-5 ">
    <label class="col-sm-3 control-label no-padding-right">犯罪嫌疑人民族:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_main_nation')">&#xe615;</i></label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="prop_main_nation" name="prop_main_nation" type="text" class="form-control"
    value="${prop_main_nation}" />
    </span>
    </div>
    </div>
    </div>

    <div class="row">
    <div class="form-group col-sm-6 col-md-5 ">
    <label class="col-sm-3 control-label no-padding-right">受教育状况:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_main_education')">&#xe615;</i></label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="prop_main_education" name="prop_main_education" type="text" class="form-control"
    value="${prop_main_education}" />
    </span>
    </div>
    </div>
    <div class="form-group col-sm-6 col-md-5 ">
    <label class="col-sm-3 control-label no-padding-right">住所地:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_main_address')">&#xe615;</i></label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="prop_main_address" name="prop_main_address" type="text" class="form-control"
    value="${prop_main_address}" />
    </span>
    </div>
    </div>
    </div>

    <div class="row">
    <div class="form-group col-sm-6 col-md-5 ">
    <label class="col-sm-3 control-label no-padding-right">前科:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_main_history')">&#xe615;</i></label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="prop_main_history" name="prop_main_history" type="text" class="form-control"
    value="${prop_main_history}" />
    </span>
    </div>
    </div>
    <div class="form-group col-sm-6 col-md-5 ">
    <label class="col-sm-3 control-label no-padding-right">主要作案地:<i class="layui-icon" onclick="traceProp('${caseEntity.id}','prop_main_exec_address')">&#xe615;</i></label>
    <div class="col-sm-9">
    <span class="col-xs-11 block input-icon input-icon-right">
    <input  id="prop_main_exec_address" name="prop_main_exec_address" type="text" class="form-control"
    value="${prop_main_exec_address}" />
    </span>
    </div>
    </div>
    </div>


<%--    <div class="row">--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件受理登记表》:</label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input placeholder="(文件链接)" id="file_djb" name="djb" type="text" class="form-control"--%>
<%--                           value="${file_djb}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">《补充移送材料通知书》:</label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input placeholder="(文件链接)" id="file_bccl" name="bccl" type="text" class="form-control"--%>
<%--                           value="${file_bccl}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>

<%--    <div class="row">--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件审批表》:</label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input placeholder="(文件链接)" id="file_spb" name="spb" type="text" class="form-control"--%>
<%--                           value="${file_spb}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">《移送函》:</label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input placeholder="(文件链接)" id="file_ysh" name="ysh" type="text" class="form-control"--%>
<%--                           value="${file_ysh}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>

<%--    <div class="row">--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件通知书》:</label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input placeholder="(文件链接)" id="file_tzs" name="tzs" type="text" class="form-control"--%>
<%--                           value="${file_tzs}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">《人民监督员表决意见通知书》:</label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input placeholder="(文件链接)" id="file_bjyj" name="bjyj" type="text" class="form-control"--%>
<%--                           value="${file_bjyj}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>

<%--    <div class="row">--%>
<%--        <div class="form-group col-sm-6 col-md-5 ">--%>
<%--            <label class="col-sm-3 control-label no-padding-right">《人民监督员监督案件处理结果通知书》:</label>--%>
<%--            <div class="col-sm-9">--%>
<%--                <span class="col-xs-11 block input-icon input-icon-right">--%>
<%--                    <input placeholder="(文件链接)" id="file_cljg" name="cljg" type="text" class="form-control"--%>
<%--                           value="${file_cljg}" readonly/>--%>
<%--                </span>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>



</div>
<c:if test="${flag == 2}">
    <%@include file="../activiti/processComSub.jsp" %>
</c:if>
<script>
    /**
     * 新增/编辑模型
     * @param id 扩展模型id
     */
    function traceProp(id,prop) {
        var url = "${webRoot}/act/deal/traceProp?id="+id+"&prop="+prop;
        //弹框层
        layer.open({
            scrollbar: true,
            type: 2,
            title : [prop , true],
            area: ['100%', '100%'], //宽高
            content: [url,'no'],
            shadeClose : true,
        });
    }
</script>

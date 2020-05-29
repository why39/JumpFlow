<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>选择文书模板</title>
    <%@include file="/common/commonCSS.jsp" %>
    <%@include file="/common/commonJS.jsp" %>
    <%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
    <!--PageOffice.js和jquery.min.js文件一定要引用-->
</head>

<body >
<div class="main-container" id="main-container" style="margin-top: 20px;margin-left: 20px">
    <div class="row" style="margin-top: 20px;">
        <form class="layui-form" id="search-from" action="${webRoot}${url}">
            <div class="layui-form-item">
                <button class="layui-btn" type="button" id="refresh">刷 新 </button>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="row">
                <div class="col-xs-12">
                    <table id="table-list" class="layui-table">
                        <thead>
                        <tr>
                            <th>规则ID</th>
                            <th>规则内容</th>
                            <th>规则所在节点</th>
                            <th>规则跳转节点</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="rule" items="${ruleList}">
                            <c:if test="${rule != null}" >
                                <tr id="${rule}" >
                                    <td>${rule.id} </td>
                                    <td>${rule.expression} </td>
                                    <td>${rule.startName} </td>
                                    <td>${rule.endName} </td>
                                    <td><button type="button" onclick="delRule(${rule.id})" class="btn btn-xs btn-white btn-danger"><i class="fa fa-trash-o"></i> 删 除 规 则</button></td>';
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>


    /**
     * 刷新页面
     */
    $("#refresh").click(function () {
        window.location.reload();
    });


    /**
     * 删除规则
     */
    function delRule(id) {
        var url ="${webRoot}/ruleDel";
        var params ={
            'id':id
        }
        $.post(url,params,function (result) {
            if(result.code == '0'){
                alert(result);
            }else {
                alertMsg(result.msg);
            }
        });
        window.location.reload();
    }
</script>
</html>
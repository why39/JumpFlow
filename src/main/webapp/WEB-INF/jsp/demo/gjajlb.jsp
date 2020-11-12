<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>数据质量</title>
    <%@include file="/common/commonCSS.jsp" %>
    <%@include file="/common/commonJS.jsp" %>
    <%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
</head>

<body>
<div class="main-container" id="main-container">
    <div class="row" style="margin-top: 20px;">
        <div class="col-md-12">
            <form class="layui-form" id="search-from" action="${webRoot}/demo/gj/gjajlb">
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width:10%;">案件标题:</label>
                    <div class="layui-input-inline">
                        <input type="text" name="title" value="${leave.title}" placeholder="请输入案件标题"
                               class="layui-input">
                    </div>
                    <button class="layui-btn" id="searchSubmit"><i class="layui-icon">&#xe615;</i>搜 索</button>
                    <button class="layui-btn layui-btn-warm" type="button" id="refresh">重 置</button>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <table id="table-list" class="layui-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>部门受案号</th>
                    <th>案件类别</th>
                    <th>案件名称</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.result}" var="leave" varStatus="i">
                    <tr id="leave_${leave.BMSAH }">
                        <td>${i.index+1 }</td>
                    <td>${leave.BMSAH}</td>
                    <td>${leave.AJLB_MC}</td>
                    <td>${leave.AJMC}</td>
                        <td>
                            <div class=" btn-group ">
                                <button class="layui-btn layui-btn-small" type="button"
                                        onclick="logDeal('${leave.BMSAH}')"><i class="layui-icon">&#xe604;</i>生成世系数据
                                </button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <sys:page page="${page}"></sys:page>
        </div>
    </div>
</div>
</body>
<script src="${webRoot}/js/activiti/actSumbit.js"></script>
<script>
    function logDeal(bmsah) {
        var url = "${webRoot}/demo/gj/parselog";
        confirm('确定生成世系数据？', function () {
            $.post(url, "bmsah=" + bmsah, function (r) {
                alert('解析成功', function () {
                    vm.reload();
                });
            });
        });

    }

</script>

</html>

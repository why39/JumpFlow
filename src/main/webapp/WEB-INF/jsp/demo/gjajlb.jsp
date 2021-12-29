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
                        <input type="text" name="title" id="test-count" value="${leave.AJLB_MC}" placeholder="请输入案件标题"
                               class="layui-input">
                    </div>
                    <button class="layui-btn" type="button" id="searchSubmitCase" onclick="searchCase()"><i class="layui-icon">&#xe615;</i>搜 索</button>
                    <button class="layui-btn layui-btn-warm" type="button" id="refresh">重 置</button>
                    <button class="layui-btn layui-btn-warm" type="button" onclick="testCases()">测试</button>
                    <button class="layui-btn layui-btn-warm" type="button" onclick="deleteTestCases()">清空测试</button>

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
                    <th>业务名称</th>
                    <th>案件名称</th>
                    <th>世系数据操作</th>
                </tr>
                </thead>
                <tbody id="my_tbody">
                <c:forEach items="${page.result}" var="leave" varStatus="i">
                    <tr id="leave_${leave.BMSAH}" >
                        <td>${i.index+1 }</td>
                        <td>${leave.BMSAH}</td>
                        <td>${leave.AJLB_MC}</td>
                        <td>${leave.AJMC}</td>
                        <td>
                            <div class=" btn-group ">
                                <c:if test="${leave.IS_COMPLETE == 0}">
                                    <button class="layui-btn layui-btn-small" type="button"
                                            onclick="logDeal('${leave.BMSAH}')">生成
                                    </button>
                                </c:if>

                                <c:if test="${leave.IS_COMPLETE == 1}">
                                    <button class="layui-btn layui-btn-small" type="button"
                                            onclick="logRead('${leave.BMSAH}')">查看
                                    </button>
                                    <%--                                    <button class="layui-btn layui-btn-small" type="button"--%>
                                    <%--                                            onclick="logDelete('${leave.BMSAH}')">删除--%>
                                    <%--                                    </button>--%>

                                </c:if>

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
                $("#search-from").submit();
            });
        });

    }

    function logRead(bmsah) {
        window.location.href="${webRoot}/neoData.html?bmsah="+encodeURI(bmsah);
    }

    function logDelete(bmsah) {
        var url = "${webRoot}/demo/gj/deletelog";
        confirm('确定删除世系数据？', function () {
            $.post(url, "bmsah=" + bmsah, function (r) {
                $("#search-from").submit();
            });
        });
    }

    function testCases() {
        var url = "${webRoot}/demo/gj/parselog-test";
        confirm('开始测试？', function () {
            $.post(url, "count=" + $("#test-count").val(), function (r) {
                $("#search-from").submit();
            });
        });
    }

    function searchCase() {
        //alert(document.getElementById("test-count").value);
        //var jia_tbody = document.getElementById("my_tbody");
        //var array_tr = jia_tbody.getElementsByTagName("tr");
        /*for(var i = 0; i < array_tr.length; i++) {
            var array_td = array_tr[i].getElementsByTagName("td");
            if(array_td[1].innerHTML != document.getElementById("test-count").value){
                array_tr[i].style.display = "none";
            }
        }*/
        document.getElementById("search-from").action="${webRoot}/demo/gj/search-aj?id=" + document.getElementById("test-count").value;
        document.getElementById("search-from").submit();
    }

    function deleteTestCases() {
        var url = "${webRoot}/demo/gj/delete-test";
        confirm('确认删除？', function () {
            $.post(url, "count=" + $("#test-count").val(), function (r) {
                $("#search-from").submit();
            });
        });
    }

</script>

</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>数据质量列表</title>
    <%@include file="/common/commonCSS.jsp" %>
    <%@include file="/common/commonJS.jsp" %>
    <%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
</head>
<%--    demo/case/dataquality--%>
<body>
<div class="main-container" id="main-container">

    <div class="row">
        <div class="col-sm-12">
            <table id="table-list" class="layui-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>表名</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>犯罪嫌疑人信息表</td>
                    <td>
                        <div class=" btn-group ">

                            <button class="layui-btn layui-btn-small" type="button"
                                    onclick="jumpDetect('tb_dq_fzxyr')">错误检测
                            </button>

                        </div>
                    </td>
                </tr>

                <tr>
                    <td>2</td>
                    <td>案件信息表</td>
                    <td>
                        <div class=" btn-group ">

                            <button class="layui-btn layui-btn-small" type="button"
                                    onclick="jumpDetect('tb_dq_ak')">错误检测
                            </button>

                        </div>
                    </td>
                </tr>
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


    function jumpDetect(tb_name) {
        var url = "${webRoot}/demo/case/dealquality";
        confirm('确定检测数据质量？', function () {
            $.post(url, "tb_name=" + tb_name, function (r) {
                console.log(r)
                if (r!=null && r["code"] == 0){
                    var content = "";
                    for(var key in r){
                        if (key != "code") {
                            console.log("属性：" + key + ",值：" + r[key]);
                            content=content+key+":"+r[key]+"\r";
                        }
                        
                    }
                    console.log(content)
                    if (content.length>0) {
                        alert(content,null,null);
                    } else {
                        alert("检测成功，没有错误",null,null);
                    }
                    
                }
            });
        });
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

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
                var url="${webRoot}/demo/case/dataquality_res";
                //弹框层
                layer.open({

                scrollbar: true,
                type: 2,
                title : ["数据质量检测结果" , true],
                area: ['100%', '100%'], //宽高
                content: [url,'no'],
                shadeClose : false,
                });


            });
        });
    }


</script>

</html>

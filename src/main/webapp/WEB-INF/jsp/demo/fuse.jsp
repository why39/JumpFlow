<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>世系融合</title>
    <%@include file="/common/commonCSS.jsp" %>
    <%@include file="/common/commonJS.jsp" %>
    <%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
</head>

<body>
<div class="main-container" id="main-container">
    <div class="row" style="margin-top: 20px;">
        <div class="col-md-4">
            <form class="layui-form" id="search-from" action="${webRoot}/demo/gj/fuse">
                <div class="layui-form-item">
<%--                    <label class="layui-form-label" style="width:10%;">业务名称:</label>--%>


        <select class = "select_class" id = "case_type">
                <option>刑事执行检察</option>
                <option>普通犯罪检察</option>
                <option>重大犯罪检察</option>
                <option>职务犯罪检察</option>
                <option>经济犯罪检察</option>
                <option>民事检察</option>
                <option>行政检察</option>
                <option selected>公益诉讼检察</option>
                <option>未成年人检察</option>
                <option>控告申诉检察</option>
                <option>法律政策研究和案件管理</option>
        </select>

<%--                    <div class="layui-input-inline">--%>
<%--                        <input type="text" name="title" id="test-count" value="${leave.title}" placeholder="请输入案件类别"--%>
<%--                               class="layui-input">--%>
<%--                    </div>--%>
<%--                    <button class="layui-btn" id="searchSubmit"><i class="layui-icon">&#xe615;</i>筛选</button>--%>

                </div>
            </form>
        </div>
    </div>

    <div class="row">
    <div class="col-sm-10">

        <div class="row">
        <div class="col-sm-12">
        <table id="table-list" class="layui-table">
        <thead>
        <tr>
<%--        <th>序号</th>--%>
        <th>部门受案号</th>
<%--        <th>业务名称</th>--%>
        <th>案件名称</th>
        <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.result}" var="leave" varStatus="i">
            <tr id="leave_${leave.BMSAH }">
<%--            <td>${i.index+1 }</td>--%>
            <td>${leave.BMSAH}</td>
<%--            <td>${leave.AJLB_MC}</td>--%>
            <td>${leave.AJMC}</td>
            <td>
            <div class=" btn-group ">
                <button class="layui-btn layui-btn-small" type="button"
                onclick="addCase('${leave.BMSAH}','${leave.AJMC}')">添加
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

        <div class="col-sm-2">
                <div  class="row">
                        <ul id="selectCases" class='list-group'>

                        </ul>
                        <button class="layui-btn layui-btn-warm" type="button" id="fuse" style="display:none" onclick="dofuse()">融合</button>
                </div>

        </div>
    </div>



</div>
</body>
<script src="${webRoot}/js/activiti/actSumbit.js"></script>
<script>

        var selectCases = []

<%--        添加选中案件--%>
        function addCase(bmsah,ajmc) {
                selectCases.push(bmsah);
                if (selectCases.length >=2 ) {
                        $("#fuse").show();
                } else {
                        $("#fuse").hide();
                }
                $("#selectCases").append("<li class='list-group-item'>"+ajmc+"</li>");
        }



        function dofuse() {
                var url = "${webRoot}/demo/gj/dofuse";
                var params ={
                'bmsah':selectCases[0],
                'bmsah2':selectCases[1]
                };
                $.post(url,params, function (r) {
                        toast('融合成功!', function () {

                        });
                });

        }

        $(function(){
                $('#case_type').change(function(){
                        var data= $(this).val();
                        console.log(data);
                        alert(data);
                });


        });


</script>

</html>

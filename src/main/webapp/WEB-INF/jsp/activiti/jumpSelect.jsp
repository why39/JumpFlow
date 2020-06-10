<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>选择跳转环节</title>
    <%@include file="/common/commonCSS.jsp" %>
    <%@include file="/common/commonJS.jsp" %>
    <%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
</head>

<body >
<div class="main-container" id="main-container" style="margin-top: 20px;margin-left: 20px">
    <div class="row" style="margin-top: 20px;">
        <form class="layui-form" id="search-from" action="${webRoot}${url}">
            <div class="layui-form-item">
                <button class="layui-btn" type="button" id="submitBtn">确 定</button>
                <button class="layui-btn" type="button" id="submitJup">建 立 跳 转 </button>
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
                            <th>选择</th>
                            <th>流程环节名称</th>
                            <th>流程环节id</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="act" items="${actList}">
                            <c:if test="${act.properties.name != null}" >
                                <tr id="user_${act.id }" >
                                    <td>
                                        <input type="radio"  name="user">
                                    </td>
                                    <td>${act.properties.name} </td>
                                    <td>${act.id} </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <table id="userTab" class="layui-table">
                        <thead>
                        <tr>
                            <th>跳转的环节名称</th>
                            <th>跳转的环节id</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    //td checkBox事件
    $("#table-list tbody tr").off("click").click(function (e) {
        var tag = e.target.tagName;
        if(tag == "INPUT"){
            return;
        }
        var isCheck = $(this).find(":checkbox").is(":checked");
        if(isCheck){
            $(this).find(":checkbox").prop("checked",false);
        }else {
            $(this).find(":checkbox").prop("checked",true);
        }
    });

    //点单tr redio选中
    $(document).on("click","tr",function(){
        $("input:radio").prop("checked",false);
        $(this).find(":radio").prop("checked",true);
    });

    /**
     * 切换用户类型select事件
     */
    layui.use(['form'], function(){
        var form = layui.form;
        form.on('select(searchFilter)', function(data){
            $("#search-from").submit();
        });
    });

    //确定选择
    $("#submitBtn").click(function () {
        //父级搜索表单
        var userTab =$(document.getElementById("main-container")).find("#userTab tbody");
        var html ="";
        var count =0;
        $("#table-list tbody input:checkbox:checked,#table-list tbody input:radio:checked").each(function () {
            var id = $(this).val();
                var tds =$(this).parent().siblings();
                html+="<tr id='"+id+"'>";
                html+="  <td>"+tds[0].innerText+"</td>";
                html+="  <td style='display: none'>";
                html+="  	<input name='userIds' value='"+id+"'/>";
                html+="  </td>";
                html+="  <td>"+tds[1].innerText+"</td>";
                html+='<td><button type="button" onclick="delUser(this)" class="btn btn-xs btn-white btn-danger"><i class="fa fa-trash-o"></i> 删 除 </button></td>';
                html+="</tr>";
                count++;
        });
        userTab.html(html);
    });

    /**
     * 删除跳转环节
     */
    function delUser(_this) {
        $(_this).parent().parent().remove();
    }

    //点击跳转
    $("#submitJup").click(function () {
        var url ="${webRoot}/act/deal/doJump";
        var userTab =$(document.getElementById("main-container")).find("#userTab tbody");
        var params ={
            'busId':'${flowbus.busId}',
            'taskId':'${taskDto.taskId}',
            'taskName':'${taskDto.taskName}',
            'instanceId':'${flowbus.instanceId}',
            'defId':'${flowbus.defid}',
            'varValue':'${taskDto.varValue}',
            'varName':'${taskDto.varName}'
        };
        //可变字段
        var changefiles ='${nodeSet.changeFiles}';
        var fileArr=changefiles.split(",");
        for(var i=0;i<fileArr.length;i++){
            var fieldName = fileArr[i];
            if (fieldName == ''){
                continue;
            }
            //父级搜索表单
            var fieldValue=$(parent.document.getElementById("main-container")).find("#"+fieldName+"").val();
            params[fieldName]=fieldValue;
        }
        //var actId =  $("#userTab tr:eq(1) td:nth-child(2)").find("td").val();
        var actId = $("#userTab tr:eq(1) td:eq(2)").text();
        params["actId"]=actId;
        if(actId == ''){
            alertMsg("请选择跳转的环节并点击确定！");
        }
        else{
            $.post(url,params,function (result) {
                if(result.code == '0'){
                    alert(result,function () {
                        //父级搜索 刷新待办列表
                        $(parent.parent.document.getElementById("main-container")).find("#search-from").submit();
                        //closeThisWindow();
                    });
                }else {
                    alertMsg(result.msg);
                }
            });
        }
    });
</script>
</html>
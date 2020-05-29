<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>选择文书模板</title>
    <%@include file="/common/commonCSS.jsp" %>
    <%@include file="/common/commonJS.jsp" %>
    <%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
</head>

<body >
<div class="main-container" id="main-container" style="margin-top: 20px;margin-left: 20px">
    <div class="row" style="margin-top: 20px;">
        <form class="layui-form" id="search-from" action="${webRoot}${url}">
            <div class="layui-form-item">
                <button class="layui-btn" type="button" id="refresh">刷 新 </button>
                <button class="layui-btn" type="button" id="ruleExist">查 看 已 有 规 则</button>
            </div>
        </form>
    </div>
            <div class="row">
                <div class="col-xs-12">
                    <table id="table-act" class="layui-table">
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
    <button class="layui-btn" type="button" id="ChooseAct">选 择 跳 转 发 生 流 程</button>
            <div class="row">
                <div class="col-sm-12">
                    <table id="showActTable" class="layui-table">
                        <thead>
                        <tr>
                            <th>发生的环节名称</th>
                            <th>发生的环节id</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
    <div class="row">
        <div class="col-xs-12">
            <table id="table-act2" class="layui-table">
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
    <button class="layui-btn" type="button" id="ChooseAct2">选 择 跳 转 前 往 流 程</button>
    <div class="row">
        <div class="col-sm-12">
            <table id="showActTable2" class="layui-table">
                <thead>
                <tr>
                    <th>前往的环节名称</th>
                    <th>前往的环节id</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
            <div class="row">
                <div class="col-xs-12">
                    <table id="tempTab" class="layui-table">
                        <thead>
                        <tr>
                            <th>选择</th>
                            <th>文书模板名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="file" items="${files}">
                            <c:if test="${file != null}" >
                                <tr id="${file}" >
                                    <td>
                                        <input type="radio"  name="user">
                                    </td>
                                    <td>${file} </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
    <button class="layui-btn" type="button" id="ChooseFile">选 择 文 书 模 板</button>
    <div class="row">
        <div class="col-sm-12">
            <table id="fileTab" class="layui-table">
                <thead>
                <tr>
                    <th>选中的文书模板</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
    <button class="layui-btn" type="button" id="createRule">新 建 规 则</button>
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

    //选择文书模板
    $("#ChooseFile").click(function () {
        var fname = $("#tempTab tr:eq(1) td:eq(0)").text();
        //父级搜索表单
        var fileTab =$(document.getElementById("main-container")).find("#fileTab tbody");
        $("#tempTab tbody input:checkbox:checked,#tempTab tbody input:radio:checked").each(function () {
            var flag = true;
            $('#fileTab tr').each(function () {
                $(this).find('td').each(function() {
                    if($(this).text() == fname){
                        flag = false;
                    }
                });
            });
            if(flag){
                var tds =$(this).parent().siblings();
                var tr = document.createElement("tr");
                var ws = document.createElement("td");
                var ck = document.createElement("td");
                ws.innerHTML = tds[0].innerText;
                ck.innerHTML = '<button type="button" onclick="delUser(this)" class="btn btn-xs btn-white btn-danger"><i class="fa fa-trash-o"></i> 删除 </button>';
                var tab = document.getElementById("fileTab");
                tab.appendChild(tr);
                tr.appendChild(ws);
                tr.appendChild(ck);
            }});
    });

    //选择流程环节
    $("#ChooseAct").click(function () {
        //父级搜索表单
        var userTab =$(document.getElementById("main-container")).find("#showActTable tbody");
        var html ="";
        var count =0;
        $("#table-act tbody input:checkbox:checked,#table-act tbody input:radio:checked").each(function () {
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

    //选择流程环节
    $("#ChooseAct2").click(function () {
        //父级搜索表单
        var userTab =$(document.getElementById("main-container")).find("#showActTable2 tbody");
        var html ="";
        var count =0;
        $("#table-act2 tbody input:checkbox:checked,#table-act2 tbody input:radio:checked").each(function () {
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




    /**
     * 刷新页面
     */
    $("#refresh").click(function () {
        window.location.reload();
    });

    /**
     * 查看已有规则
     */
    $("#ruleExist").click(function () {
        var params ="busId="+'${taskDto.busId}'.busId+"&taskId="+'${taskDto.taskId}'+"&taskName="+'${taskDto.taskName}'+"&defId="+'${taskDto.defId}'+"&instanceId="+'${taskDto.instanceId}';
        var url="${webRoot}/ruleExt?"+params;
        //弹框层
        layer.open({
            scrollbar: true,
            type: 2,
            title : ["已存在的规则" , true],
            area: ['100%', '100%'], //宽高
            content: [url,'no'],
            shadeClose : false,
        });
    });

    /**
     * 新建规则
     */
    $("#createRule").click(function () {
        var modelId = '${taskDto.defId}';
        var instanceId = '${taskDto.instanceId}';
        var startEvent = $("#showActTable tr:eq(1) td:eq(2)").text();
        var startName = $("#showActTable tr:eq(1) td:eq(0)").text();
        var endEvent = $("#showActTable2 tr:eq(1) td:eq(2)").text();
        var endName = $("#showActTable2 tr:eq(1) td:eq(0)").text();
        var table = document.getElementById("fileTab");
        var expression = '';
        for(var i = 1, rows = table.rows.length; i < rows; i++){
            expression += table.rows[i].cells[0].innerHTML;
            if(i != rows - 1){
                expression += "&";
            }
        }
        if(startEvent == ''){
            alertMsg("请选择跳转发生环节！");
        }
        else if(endEvent == ''){
            alertMsg("请选择跳转目标环节！");
        }
        else if(expression == ''){
            alertMsg("请选择跳转发生的文书规则！");
        }

        var url = "${webRoot}/ruleCreate";
        var params ={
            'startEvent':startEvent,
            'endEvent':endEvent,
            'expression':expression,
            'modelId':modelId,
            'instanceId':instanceId,
            'startName':startName,
            'endName':endName,
        };
        $.post(url,params,function (result) {
            if(result.code == '0'){
                alert(result);
            }else {
                alertMsg(result.msg);
            }
        });
    });
</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>选择文书模板</title>
    <%@include file="/common/commonCSS.jsp" %>
    <%@include file="/common/commonJS.jsp" %>
    <%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
    <!--PageOffice.js和jquery.min.js文件一定要引用-->
    <script type="text/javascript" src="${webRoot}/plib/jquery.min.js"></script>
    <script type="text/javascript" src="${webRoot}/plib/pageoffice.js" id="po_js_main"></script>
</head>

<body >
<div class="main-container" id="main-container" style="margin-top: 20px;margin-left: 20px">
    <div class="row" style="margin-top: 20px;">
        <form class="layui-form" id="search-from" action="${webRoot}${url}">
            <div class="layui-form-item">
                <button class="layui-btn" type="button" id="submitBtn">确 定</button>
                <button class="layui-btn" type="button" id="write">新 建 文 书</button>
                <button class="layui-btn" type="button" id="submitFile">文 书 提 交</button>
                <button class="layui-btn" type="button" id="create">新 建 文 书 模 板 </button>
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
            <div class="row">
                <div class="col-sm-12">
                    <table id="tempTab" class="layui-table">
                        <thead>
                        <tr>
                            <th>选择的文书模板</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <table id="fileTab" class="layui-table">
                        <thead>
                        <tr>
                            <th>已编辑的文书</th>
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
        var tempTab =$(document.getElementById("main-container")).find("#tempTab tbody");
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
                html+='<td><button type="button" onclick="delUser(this)" class="btn btn-xs btn-white btn-danger"><i class="fa fa-trash-o"></i> 删 除 </button></td>';
                html+="</tr>";
                count++;
        });
        tempTab.html(html);
    });

    /**
     * 删除跳转环节
     */
    function delUser(_this) {
        $(_this).parent().parent().remove();
    }


    //编辑文书
    $("#write").click(function () {

        var fname = $("#tempTab tr:eq(1) td:eq(0)").text();
        if(fname == ''){
            alertMsg("请选择一个文书模板并点击确定！");
        }
        else{
            POBrowser.openWindowModeless('word?fileName='+fname.toString() , 'width=1200px;height=1200px;');

            var fileTab =$(document.getElementById("main-container")).find("#fileTab tbody");
            $("#table-list tbody input:checkbox:checked,#table-list tbody input:radio:checked").each(function () {
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
                    ck.innerHTML = '<button type="button" onclick="look(\'' + fname + '\')" class="layui-btn"><i class="fa fa-trash-o"></i> 查 看 </button>';
                    var tab = document.getElementById("fileTab");
                    tab.appendChild(tr);
                    tr.appendChild(ws);
                    tr.appendChild(ck);
                }});
        }
    });

    //新建文书模板
    $("#create").click(function () {
        POBrowser.openWindowModeless('createword?fileName=aaaa' , 'width=1200px;height=1200px;');
    });

    /**
     * 查看已编辑的文书
     */
    function look(fname) {
        POBrowser.openWindowModeless('wordIns?fileName='+fname.toString() , 'width=1200px;height=1200px;');
    }
</script>
</html>
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
            <form class="layui-form" id="search-from" action="${webRoot}/demo/case/list">
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width:10%;">案件标题:</label>
                    <div class="layui-input-inline">
                        <input type="text" name="title" value="${leave.title}"  placeholder="请输入案件标题"  class="layui-input" >
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
                    <th>案件标题</th>
                    <th>案件发起人</th>
                    <th>案件详情说明</th>
                    <th>案件执行结果</th>
                    <th>案件执行状态</th>
                    <th>案件创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.result}" var="leave" varStatus="i">
                    <tr id="leave_${leave.id }">
                        <td>${i.index+1 }</td>
                        <td>${leave.title}</td>
                        <td>${leave.leaveUser}</td>
                        <td>${leave.leavewhy}</td>
                        <td>${fns:getCodeName("act_result",leave.actResult)}</td>
                        <td>${fns:getCodeName("act_process_status",leave.status)}</td>
                        <td><fmt:formatDate value="${leave.createTime}" pattern="yyyy-MM-dd"/></td>
                        <td>
                            <div class=" btn-group ">
                    <button class="layui-btn layui-btn-small" type="button" onclick="dataqualityDeal('${leave.id}')"><i class="layui-icon">&#xe604;</i>数据质量分析</button>
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
    function dataqualityDeal(id) {
      var url ="${webRoot}/demo/case/dealquality";
      confirm('确定进行数据质量分析？', function(){
          $.post(url,"id="+id,function (r) {
              toast(r.msg);
          });
      });

    }

    function deleteById(id) {
        var url ="${webRoot}/demo/case/delete";
        confirm('确定要删除选中的记录？', function(){
            $.post(url,"id="+id,function (r) {
                if(r.code=='0'){
                    toast(r.msg);
                    $("#leave_"+id+"").remove();
                }else {
                    alertMsg(r.msg);
                }
            });
        });
    }
</script>

</html>

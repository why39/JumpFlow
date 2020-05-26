<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.zhuozhengsoft.pageoffice.*" %>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>"><

    <title>My JSP 'index.jsp' starting page</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

    <!--PageOffice.js和jquery.min.js文件一定要引用-->
    <script type="text/javascript" src="${webRoot}/plib/jquery.min.js"></script>
    <script type="text/javascript" src="${webRoot}/plib/pageoffice.js" id="po_js_main"></script>

    <%--<script type="text/javascript" src="pageoffice.js" id="po_js_main"></script>--%>
</head>

<body>
<div style="text-align:center;">
    <b>文书在线编辑</b><br>
    <a href="javascript:POBrowser.openWindowModeless('word?fileName=aaaa' , 'width=1200px;height=800px;');">打开文书</a><br>
</div>
<div style="text-align:center;">
    <b>新建文书模板</b><br>
    <a href="javascript:POBrowser.openWindowModeless('createword?fileName=aaaa' , 'width=1200px;height=1200px;');">新建文书</a><br>
</div>
</body>

</html>
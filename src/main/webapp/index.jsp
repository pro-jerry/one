<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>index</title>
	<jsp:include page="inc.jsp"></jsp:include>
  </head>
  
  <body>
	<jsp:include page="user/login.jsp"></jsp:include>
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',title:'North Title',split:true,href:'${pageContext.request.contextPath}/layout/north.jsp'" style="height:100px;"></div>   
	    <div data-options="region:'south',title:'South Title'" style="height:100px;"></div>   
	    <div data-options="region:'east',iconCls:'icon-reload',title:'East',split:true" style="width:150px;"></div>   
	    <div data-options="region:'west',title:'West',split:true,href:'${pageContext.request.contextPath}/layout/west.jsp'" style="width:200px;"></div>   
	    <div data-options="region:'center',border:false,noheader:true" style="overflow: hidden;">
	    <div id="index_tabs" class="easyui-tabs" data-options="fit:true,border:false" style="overflow: hidden;">
	    	<div title="首页"  data-options="border:false" style="overflow: hidden;">
	    			<iframe src="${pageContext.request.contextPath}/protal/index.jsp"
	    			frameborder="0" style="border: 0; width: 100%; height: 98%;"></iframe>
	    		</div>
	    	</div>
	    </div>   
	</div>      
  </body>
</html>

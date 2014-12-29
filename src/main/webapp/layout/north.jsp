<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" charset="utf-8">
	function logoutFun(b){
		$.getJSON('${pageContext.request.contextPath}/userController/logout',{
			t:new Date()
		},function(result){
			if(b){
				location.replace('${pageContext.request.contextPath}/');
			}else{
				$('#sessionInfoDiv').html('');
				$('#loginDialog').dialog('open');
			}
		});
	}
</script>
<div id="sessionInfoDiv">
	<!--  
	<c:if test="${sessionInfo.id!=null}">[<strong>${sessionInfo.name}</strong>],欢迎你！你使用[<strong>${sessionInfo.ip}</strong>]IP登录！</c:if>
	-->
	
</div>

<div id="layout_north_zxMenu" style="width: 100px;display: none;">
	<div onclick="logoutFun();">锁定窗口</div>
	<div class="menu-step"></div>
	<div onclick="logoutFun();"></div>
	<div onclick="logoutFun(true);">退出登录</div>
</div>
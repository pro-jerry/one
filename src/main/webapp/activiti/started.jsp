<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
table,table td,table th {
	border: 1px solid gray;
	border-collapse: collapse;
}

a {
	height: 30px;
	line-height: 30px;
	border: 1px solid black;
	background: gray;
	color: white;
	text-decoration: none;
	padding: 3px;
	font-weight: bold;
}
</style>
</head>
<body>

	<div style="margin: 0 auto; width: 400px; padding-top: 50px;">
		<h2>已启动流程</h2>
		<table width="400px;">
			<thead>
				<tr>
					<th>流程实例ID</th>
					<th>流程定义ID</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="temp" items="${list}">
					<tr>
						<td>${temp.id}</td><td>${temp.processDefinitionId}</td><td> <a href="../activiti/graphics?instanceId=${temp.id}">图形</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<a href="../activiti">流程模板</a>
		<a href="../activiti/deployed">已部署流程</a>
		<a href="../activiti/task">任务列表</a>
	</div>
</body>
</html>
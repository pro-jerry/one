<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <script type="text/javascript">
    	var loginDialog;
    	var loginTabs;
		   $(function(){
			   
			   loginDialog=$('#loginDialog').show().dialog({
				   closable : false,
				   modal: true,
				   buttons:[{
					   text:'登陆',
					   handler:function(){
						   loginFun();
					   }

				   }]
			   });
			   
			   /*
			   var sessionInfo_userId = '${sessionInfo.id}';  
				if(sessionInfo_userId){
					loginDialog.dialog('close');
				}
				*/
				$('#loginDialog input').keyup(function(event) {
					if (event.keyCode == '13') {
						loginFun();
					}
				});
				
		   });
		
		
		 function loginFun(){
			 if(layout_west_tree){
				 
				 loginTabs=$('#loginTabs').tabs('getSelected'); 
				
				 var form = loginTabs.find('form');
				 
				 if(form.form('validate')){
					 /*
					parent.$.messager.progress({
						title:'提示',
						text:'数据处理中，请稍后...'
					}); */
				
				$.post('${pageContext.request.contextPath}/userController/login',
						form.serialize(),
					function(result){
						if(result.success){
							if(!layout_west_tree_url){
								layout_west_tree.tree({
									url:'${pageContext.request.contextPath}/privilegeController/tree',
									onBeforeLoad:function(node,param){
										parent.$.messager.progress({
											title:'提示',
											text:'数据处理中,请稍后....'
										});
									}
								});
							}
							$('#layout_west_tree').tree('reload');
							$('#loginDialog').dialog('close');
							$('#sessionInfoDiv').html($.formatString('[<strong>{0}</strong>]，欢迎你！您使用[<strong>{1}</strong>]IP登录！', result.obj.name, result.obj.ip));
						} else {
							$.messager.alert('错误', result.msg, 'error');
						}
						parent.$.messager.progress('close');
					}, "JSON");
				 }
			 }
		 }
    </script>
  </head>
  
  <div id="loginDialog" title="用户登陆" style="width: 330px; height: 220px; overflow: hidden; display: none;">
      <div id="loginTabs" class="easyui-tabs" data-options="fit:true,border:false">
		<div title="用户输入" style="overflow: hidden; padding: 10px;">
			<form method="post">
				<table class="table table-hover table-condensed">
					<tr>
						<th>登录名</th>
						<td><input name="name" type="text" placeholder="请输入登录名" class="easyui-validatebox" data-options="required:true"></td>
					</tr>
					<tr>
						<th>密码</th>
						<td><input name="pwd" type="password" placeholder="请输入密码" class="easyui-validatebox" data-options="required:true" ></td>
					</tr>
				</table>
			</form>
		</div>
    </div>
  </div>

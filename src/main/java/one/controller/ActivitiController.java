package one.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import one.util.ActivitiUtil;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/activiti")
public class ActivitiController {

	@Autowired
	ProcessEngine processEngine;
	
	/**
	 * 列出所有流程模板
	 * @param mav
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	 public ModelAndView list(ModelAndView mav) {
        mav.addObject("list", ActivitiUtil.list());
        mav.setViewName("activiti/template");
        return mav;
    }
	
	/**
	 * 部署流程
	 * @param processName
	 * @param mav
	 * @return
	 */
	@RequestMapping("/deploy")
	public ModelAndView deploy(String processName, ModelAndView mav) {
		 
        RepositoryService service = processEngine.getRepositoryService();
 
        if (null != processName)
            service.createDeployment()
                    .addClasspathResource("diagrams/" + processName).deploy();
 
        List<ProcessDefinition> list = service.createProcessDefinitionQuery()
                .list();
 
        mav.addObject("list", list);
        mav.setViewName("activiti/deployed");
        return mav;
    }
	
	/**
	 * 已部署的流程
	 * @param mav
	 * @return
	 */
	@RequestMapping("/deployed")
	public ModelAndView deployed(ModelAndView mav) {
		 
        RepositoryService service = processEngine.getRepositoryService();
 
        List<ProcessDefinition> list = service.createProcessDefinitionQuery()
                .list();
 
        mav.addObject("list", list);
        mav.setViewName("activiti/deployed");
 
        return mav;
    }
	
	/**
     * 启动一个流程实例
     */
    @RequestMapping("/start")
    public ModelAndView start(String id, ModelAndView mav) {
 
        RuntimeService service = processEngine.getRuntimeService();
 
        service.startProcessInstanceById(id);
 
        List<ProcessInstance> list = service.createProcessInstanceQuery()
                .list();
 
        mav.addObject("list", list);
        mav.setViewName("activiti/started");
 
        return mav;
    }
    
    /**
     * 所有已启动流程实例
     */
    @RequestMapping("started")
    public ModelAndView started(ModelAndView mav) {
 
        RuntimeService service = processEngine.getRuntimeService();
 
        List<ProcessInstance> list = service.createProcessInstanceQuery()
                .list();
 
        mav.addObject("list", list);
        mav.setViewName("activiti/started");
 
        return mav;
    }
    
    /**
     * 进入任务列表 
     */
    @RequestMapping("/task")
    public ModelAndView task(ModelAndView mav){
        TaskService service=processEngine.getTaskService();
        List<Task> list=service.createTaskQuery().list();
        mav.addObject("list", list);
        mav.setViewName("activiti/task");
        return mav;
    }
    
    /**
     *完成当前节点 
     */
    @RequestMapping("/complete")
    public ModelAndView complete(ModelAndView mav,String id){
         
        TaskService service=processEngine.getTaskService();
         
        service.complete(id);
         
        return new ModelAndView("redirect:/activiti/task");
    }
    
    /**
     * 所有已启动流程实例
     * 
     * @throws IOException
     */
    @RequestMapping("/graphics")
    public void graphics(String definitionId, String instanceId,
            String taskId, ModelAndView mav, HttpServletResponse response)
            throws IOException {
         
        response.setContentType("image/png");
        Command<InputStream> cmd = null;
 
        if (definitionId != null) {
            cmd = new GetDeploymentProcessDiagramCmd(definitionId);
        }
 
        if (instanceId != null) {
            cmd = new ProcessInstanceDiagramCmd(instanceId);
        }
 
        if (taskId != null) {
            Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
            cmd = new ProcessInstanceDiagramCmd(
                    task.getProcessInstanceId());
        }
 
        if (cmd != null) {
            InputStream is = processEngine.getManagementService().executeCommand(cmd);
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = is.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        }
    }
    
    
}

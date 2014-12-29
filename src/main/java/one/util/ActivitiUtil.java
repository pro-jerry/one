package one.util;

import java.io.File;



public class ActivitiUtil {

	public static String[] list(){
		String basePath=ActivitiUtil.class.getResource("/").getPath();
		basePath=basePath.substring(1,basePath.length());
		return new File(basePath+File.separator+"diagrams").list();
	}

}

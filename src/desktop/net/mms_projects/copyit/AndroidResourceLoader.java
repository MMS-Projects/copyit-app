package net.mms_projects.copyit;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;

public class AndroidResourceLoader {

	public static Image getImage(String path) {
		System.out.println(AndroidResourceLoader.class.getPackage());
		String basePath = "/";
		return SWTResourceManager.getImage(AndroidResourceLoader.class, basePath + path);
	}
	
}

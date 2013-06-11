package net.mms_projects.copy_it;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

abstract public class FileStreamBuilder {

	abstract public FileInputStream getInputStream() throws IOException;
	abstract public FileOutputStream getOutputStream() throws IOException;
	
}

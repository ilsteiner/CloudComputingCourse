package finalProject.main.model;

import java.io.File;

import finalProject.main.Preset;

public class FileUpload {
	private File file;
	private Preset uploadType;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Preset getUploadType() {
		return uploadType;
	}
	public void setUploadType(Preset uploadType) {
		this.uploadType = uploadType;
	}
}

package com.hongkuncheng.vcoin.action.member.system;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.helper.DateHelper;
import com.hongkuncheng.vcoin.helper.FileHelper;
import com.hongkuncheng.vcoin.helper.StringHelper;

/**
 * 文件处理
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbfileAction extends BaseAction {
    
	private File srcfile;
	private String srcfileFileName;
	private String srcfileContentType;
	private File[] srcfiles;
	private String[] srcfilesFileName;
	private String[] srcfilesContentType;

	public File getSrcfile() {
		return srcfile;
	}

	public void setSrcfile(File srcfile) {
		this.srcfile = srcfile;
	}

	public String getSrcfileFileName() {
		return srcfileFileName;
	}

	public void setSrcfileFileName(String srcfileFileName) {
		this.srcfileFileName = srcfileFileName;
	}

	public String getSrcfileContentType() {
		return srcfileContentType;
	}

	public void setSrcfileContentType(String srcfileContentType) {
		this.srcfileContentType = srcfileContentType;
	}

	public File[] getSrcfiles() {
		return srcfiles;
	}

	public void setSrcfiles(File[] srcfiles) {
		this.srcfiles = srcfiles;
	}

	public String[] getSrcfilesFileName() {
		return srcfilesFileName;
	}

	public void setSrcfilesFileName(String[] srcfilesFileName) {
		this.srcfilesFileName = srcfilesFileName;
	}

	public String[] getSrcfilesContentType() {
		return srcfilesContentType;
	}

	public void setSrcfilesContentType(String[] srcfilesContentType) {
		this.srcfilesContentType = srcfilesContentType;
	}
	
	
	/**
	 * 获取文件拓展名
	 * @return
	 */
	private String getExt(){
		return srcfileFileName.substring(srcfileFileName.indexOf("."));
	}

	/**
	 * 上传文件
	 * @return
	 */
	public void upload() {
		try {
			if (ImageIO.read(srcfile) == null) return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		String savepath = request.getParameter("savepath");
		if (savepath.contains(".")) {
			return;
		}
		savepath = "/uploads/" + savepath;
		String realpath = request.getRealPath(savepath);
		File destdir = new File(realpath);
		if (!destdir.exists()) destdir.mkdirs();
		String filename = null;
		String nametype = request.getParameter("nametype");
		if (nametype == null) {
			filename = DateHelper.getFilename(getExt());
		} else {
			switch (Integer.parseInt(nametype)) {
				case 1:
					filename = srcfileFileName;
					break;
				case 2:
					filename = request.getParameter("assignname");
					break;
			}
		}
		if (StringHelper.appearNumber(filename, ".") != 1 || (!filename.toLowerCase().endsWith(".jpg") && !filename.toLowerCase().endsWith(".png") && !filename.toLowerCase().endsWith(".gif"))) {
			return;
		}
		File destfile = new File(destdir, filename);
		try {
			FileUtils.copyFile(srcfile, destfile);
			print(FileHelper.Exists(realpath + "/" + filename) ? savepath + "/" + filename : "error");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传多个文件
	 * @return
	 */
	public void uploads() {
		for (int i = 0; i < srcfiles.length; i++) {
			this.srcfile = srcfiles[i];
			this.srcfileFileName = srcfilesFileName[i];
			this.srcfileContentType = srcfilesContentType[i];
			upload();
		}
	}
	
}

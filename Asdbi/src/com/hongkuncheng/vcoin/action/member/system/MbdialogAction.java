package com.hongkuncheng.vcoin.action.member.system;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hongkuncheng.vcoin.action.BaseAction;
import com.hongkuncheng.vcoin.model.Imagesize;

/**
 * 系统对话框
 * @author 洪坤成
 *
 */
@Controller
@Scope("prototype")
public class MbdialogAction extends BaseAction {
	
	//CNT充值确认
	public String rmbputinsure() {
		request.setAttribute("putinmethod", Integer.parseInt(request.getParameter("putinmethod")));
		request.setAttribute("money", request.getParameter("money"));
		return succeed();
	}
	
	////////////////////////////////////////  图片裁切    ////////////////////////////////////////
	
	//图片裁切
	public String imagecrop(){
		String filepath = request.getParameter("filepath");
		List<Imagesize> imagesizes = null;
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		if (width != null && height != null) {
			imagesizes = new ArrayList<Imagesize>();
			String description = request.getParameter("description");
			Imagesize imagesize = new Imagesize();
			imagesize.setWidth(Integer.parseInt(width));
			imagesize.setHeight(Integer.parseInt(height));
			imagesize.setDescription(description);
			imagesizes.add(imagesize);
		} else {
			imagesizes = factory.getCurrentSession().createCriteria(Imagesize.class).addOrder(Order.asc("id")).setCacheable(true).list();
		}
		request.setAttribute("filepath", filepath);
		float sizeX = (float)imagesizes.get(0).getWidth();
		float sizeY = (float)imagesizes.get(0).getHeight();
		request.setAttribute("rateX", sizeX * (1 / sizeY));
        File srcFile = new File(request.getRealPath(filepath));
        Image srcImg = null;
		try {
			srcImg = ImageIO.read(srcFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		float imgWidth = (float)srcImg.getWidth(null);
		float imgHeight = (float)srcImg.getHeight(null);
		request.setAttribute("size", imgWidth > imgHeight ? "width" : "height");
		request.setAttribute("marintop", imgWidth > imgHeight ? (300 - imgHeight * (300 / imgWidth)) / 2 : 0);
		request.setAttribute("imagesizes", imagesizes);
		return succeed();
	}
	
	//图片裁切处理
	public void imagecropcut(){
		String filepath = request.getParameter("filepath");
        int pnlWidth = Integer.parseInt(request.getParameter("pnlWidth"));
        int pnlHeight = Integer.parseInt(request.getParameter("pnlHeight"));
        int preWidth = Integer.parseInt(request.getParameter("preWidth"));
        int startX = Integer.parseInt(request.getParameter("startX"));
        int startY = Integer.parseInt(request.getParameter("startY"));
        int iWidth = Integer.parseInt(request.getParameter("iWidth"));
        OutputStream os = null;
        String srcPath = request.getRealPath(filepath);
        File srcFile = new File(srcPath);
        Image srcImg = null;
		try {
			srcImg = ImageIO.read(srcFile);
			int imgSrcWidth = srcImg.getWidth(null);
	        int imgSrcHeight = srcImg.getHeight(null);
	        float rate = (float)imgSrcWidth / preWidth;
	        BufferedImage buffImg = new BufferedImage(pnlWidth, pnlHeight, BufferedImage.TYPE_INT_RGB); 
	        Graphics2D g = buffImg.createGraphics();  
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
	        int imgScaledWidth = (int)(imgSrcWidth / ((float)iWidth / pnlWidth * rate));
	        int imgScaledHeight = (int)(imgSrcHeight / ((float)iWidth / pnlWidth * rate));
	        g.drawImage(srcImg.getScaledInstance(imgScaledWidth, imgScaledHeight, Image.SCALE_SMOOTH), 0 - (int)(imgScaledWidth * startX / (imgSrcWidth / rate)), 0 - (int)(imgScaledHeight * startY / (imgSrcHeight / rate)), null);
	        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));   
	        g.dispose();   
	        os = new FileOutputStream(srcPath);   
	        ImageIO.write(buffImg, "JPG", os);
	        os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}

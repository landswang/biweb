package com.hongkuncheng.vcoin.ueditor.hunter;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 水印处理
 * @author 洪坤成
 *
 */
public class WaterHunter {

	public void imageWaterMark(String scrImagePath, String waterImagePath, int position, int x, int y, double transparence) {   
        OutputStream os = null;
        File srcFile = new File(scrImagePath + ".tmp");
        try {   
            Image srcImg = ImageIO.read(srcFile);   
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            BufferedImage buffImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB); 
            // 得到画笔对象    
            Graphics2D g = buffImg.createGraphics();   
            // 设置对线段的锯齿状边缘处理   
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);   
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);   
            // 水印图象
            ImageIcon imgIcon = new ImageIcon(waterImagePath);   
            // 得到Image对象。   
            Image waterImg = imgIcon.getImage();
            int waterImgWidth = waterImg.getWidth(null);
            int waterImgHeight = waterImg.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float)transparence));   
            // 表示水印图片的位置   
            int waterX = 0; //水印x坐标
            int waterY = 0; //水印y坐标
            switch (position)
            {
                case 1:
                	waterX = x;
                	waterY = y;
                	break;
                case 2:
                	waterX = (srcImgWidth - waterImgWidth) / 2;
                	waterY = y;
                	break;
                case 3:
                	waterX = srcImgWidth - waterImgWidth - x;
                	waterY = y;
                	break;
                case 4:
                	waterX = x;
                	waterY = (srcImgHeight - waterImgHeight) / 2;
                	break;
                case 5:
                	waterX = (srcImgWidth - waterImgWidth) / 2;
                	waterY = (srcImgHeight - waterImgHeight) / 2;
                	break;
                case 6:
                	waterX = srcImgWidth - waterImgWidth - x;
                	waterY = (srcImgHeight - waterImgHeight) / 2;
                	break;
                case 7:
                	waterX = x;
                	waterY = srcImgHeight - waterImgHeight - y;
                	break;
                case 8:
                	waterX = (srcImgWidth - waterImgWidth) / 2;
                	waterY = srcImgHeight - waterImgHeight - y;
                	break;
                default:
                	waterX = srcImgWidth - waterImgWidth - x;
                	waterY = srcImgHeight - waterImgHeight - y;
                	break;
            }
            g.drawImage(waterImg, waterX, waterY, null);   
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));   
            g.dispose();   
            os = new FileOutputStream(scrImagePath);   
            // 生成图片   
            ImageIO.write(buffImg, "JPG", os);
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            try {   
                if (null != os) os.close();
            } catch (Exception e) {   
                e.printStackTrace();   
            }
            srcFile.delete();
        } 
    }   
	
}

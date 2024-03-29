package com.hongkuncheng.vcoin.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hongkuncheng.vcoin.helper.CookieHelper;

/**
 * 验证码
 */
public class ValidateCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ValidateCode() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//验证码图片宽度和高度 
		int width = Integer.parseInt(request.getParameter("width")); 
		int height = Integer.parseInt(request.getParameter("height")); 
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
		//2D绘画器
		Graphics2D g = buffImg.createGraphics(); 
		//创建一个随机数生成器类。 
		Random random = new Random(); 
		//设定图像背景色
		g.setColor(getRandColor(200, 250)); 
		g.fillRect(0, 0, width, height); 
		//创建字体，字体的大小应该根据图片的高度来定。 
		Font font = new Font("Times New Roman", Font.BOLD, (int)((double) width / 3.5)); 
		//设置字体。 
		g.setFont(font);        
		//随机产生干扰线，使图象中的认证码不易被其它程序探测到。 
		for (int i = 0; i < width; i++) { 
			g.setColor(getRandColor(120,200)); 
			int x = random.nextInt(width); 
			int y = random.nextInt(height); 
			int xl = random.nextInt(12); 
			int yl = random.nextInt(12); 
			g.drawLine(x, y, x + xl, y + yl); 
		} 
		//randomCode用于保存随机产生的验证码，以便用户登录后进行验证。 
		StringBuffer randomCode = new StringBuffer(); 
		//设置默认生成4个验证码 
		int length = 4; 
		//设置备选验证码:包括"a-z"和数字"0-9" 
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
		int size = base.length(); 
		//随机产生4位数字的验证码。 
		for (int i = 0; i < length; i++) { 
			//得到随机产生的验证码数字。 
			int start = random.nextInt(size); 
			String strRand = base.substring(start, start + 1);             
			//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成 
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110))); 
			g.drawString(strRand, i * (int)((double) width / 4.48) + (int)((double) width / 14), (int)((double) height
					/ 1.3)); 
			//将产生的四个随机数组合在一起。 
			randomCode.append(strRand); 
		} 
		//将四位数字的验证码保存到Cookie中。 
		CookieHelper.SetCookie(response, "checkcode", randomCode.toString(), true);
		//图象生效 
		g.dispose(); 
		//禁止图像缓存。 
		response.setHeader("Pragma", "no-cache"); 
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		//将图像输出到Servlet输出流中。
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.flush();
		sos.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
		fc = 255;
		if (bc > 255)bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
}

package org.spat.wf.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

import org.spat.wf.mvc.BeatContext;

public class Captcha {
	private final String[] fontarry = new String[]{"宋体","黑体","微软雅黑","微软正黑体","新宋体","仿宋","楷体"}; 
	private BeatContext beat;
	private int width;
	private int height;
	private int length;
	private int font_width;
	private int font_height;
	
	public Captcha(BeatContext beat, int width, int height, int length){
		this.beat = beat;
		this.height = height;
		this.width = width;
		this.length = length;
		this.font_width = width / (length + 1);
		this.font_height = height - 12;
	}
	
	public void create()
			throws IOException {
		beat.getResponse().setContentType("image/jpeg");
		beat.getResponse().setHeader("Pragma", "No-cache");
		beat.getResponse().setHeader("Cache-Control", "no-cache");
		beat.getResponse().setDateHeader("Expires", 0);
		Random random = new Random();
		
		String fontStyle = fontarry[random.nextInt(fontarry.length)];
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		g.setColor(Color.WHITE);// 设定背景色
		g.fillRect(0, 0, width, height);
		g.setFont(new Font(fontStyle, Font.PLAIN + Font.ITALIC,  height - 2));
		g.setColor(new Color(55, 55, 12));// 画边框
		g.drawRect(0, 0, width - 1, height - 1);
		g.setColor(getRandColor(160, 200));// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		for (int i = 0; i < 160; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		StringBuffer sRand = new StringBuffer();
		int red = 0, green = 0, blue = 0;
		for (int i = 0; i <length; i++) {
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			int wordType = random.nextInt(3);
			char retWord = 0;
			switch (wordType) {
			case 0:
				retWord = this.getSingleNumberChar();
				break;
			case 1:
				retWord = this.getLowerOrUpperChar(0);
				break;
			case 2:
				retWord = this.getLowerOrUpperChar(1);
				break;
			}
			sRand.append(retWord);
			g.setColor(new Color(red, green, blue));
			g.drawString(String.valueOf(retWord), i * font_width, font_height);

		}
		beat.setSession("_captcha", sRand.toString().toLowerCase().trim());
		g.dispose();
		ServletOutputStream responseOutputStream = beat.getResponse().getOutputStream();
		ImageIO.write(image, "JPEG", responseOutputStream);
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	public static boolean check(BeatContext beat, String code) {
		return code.toLowerCase().trim().equals(beat.getSession("_captcha"));
	}
	
	
	//----------------------------------------------------------------------------------------------------------------------//
	
	private char getSingleNumberChar() {
		Random random = new Random();
		int numberResult = random.nextInt(10);
		int ret = numberResult + 48;
		return (char) ret;
	}

	private char getLowerOrUpperChar(int upper) {
		Random random = new Random();
		int numberResult = random.nextInt(26);
		int ret = 0;
		if (upper == 0) {// 小写
			ret = numberResult + 97;
		} else if (upper == 1) {// 大写
			ret = numberResult + 65;
		}
		return (char) ret;
	}

	private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}

package com.flappygod.lipo.lxlibrary.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTool {
	/******** 获取img标签正则 ************/
	private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	/******** 获取src路径的正则 ************/
	private static final String IMGSRC_REG = "[^d]src=\"([^\"]+)\"";

	/******************
	 * 过滤掉html标签
	 * 
	 * @param content  过滤html文本内容
	 * @return
	 */
	public static String filterHTMLString(String content) {

		if (null == content)
			return "";

		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;

		try {

			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(content);
			content = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(content);
			content = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(content);

			content = m_html.replaceAll(""); // 过滤html标签
		} catch (Exception e) {
			return "";
		}
		return content;
	}

	/***
	 * 获取ImageUrl地址
	 * 
	 * @param HTML
	 *            需要解析的html
	 * @return 获得 img整个标签数据列表
	 * 
	 */
	public static List<String> getImageUrl(String HTML) {
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
		List<String> listImgUrl = new ArrayList<String>();
		while (matcher.find()) {
			listImgUrl.add(matcher.group());
		}
		return listImgUrl;
	}

	/***
	 * 获取ImageSrc地址
	 * 
	 * @param listImageUrl
	 *            需要解析的img列表
	 * @return
	 */
	public static List<String> getImageSrc(List<String> listImageUrl) {

		List<String> listImgSrc = new ArrayList<String>();
		for (String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				listImgSrc.add(matcher.group().substring(0,
						matcher.group().length() - 1));
			}
		}
		return listImgSrc;
	}

	/***
	 * 获取html中的image src列表
	 * 
	 * @param html
	 *            需要解析的html
	 * @return
	 */
	public static List<String> getImageSrc(String html) {
		List<String> listImageUrl = getImageUrl(html);
		List<String> listImgSrc = new ArrayList<String>();
		for (String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				String str = matcher.group();
				if (str.length() > 7)
					listImgSrc.add(str.substring(6,
							matcher.group().length() - 1));
			}
		}
		return listImgSrc;
	}
}

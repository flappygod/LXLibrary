package com.flappygod.lipo.lxlibrary.Widget.TextView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;

public class LTextViewLayout {

	//默认的宽度是文字宽度乘以此值，可以自行修改
	public final static float DEFAULT_LINE_WORD_PERCENTAGE = 11.0f / 10;

	/*************
	 * 计算无限制情况下的宽度
	 * 
	 * @param lineSpace
	 *            列间隔
	 * @param rowSpace
	 *            行间隔
	 * @param str
	 *            字符串
	 * @param paint
	 *            paint
	 * @return
	 */
	static int getLayoutWidth(int lineSpace, int rowSpace, String str,
			Paint paint) {
		if (str == null) {
			return 0;
		}
		/* 单个文字的宽度 */
		long oneWordLength = 0;
		/* 通过一个繁体字，来取得这个文字的大小，设置绘制的文字的大小 */
		Rect rt = new Rect();
		paint.getTextBounds("繁", 0, 1, rt);

		/* 计算单个文字的宽度 */
		oneWordLength = (long) (rt.width() * DEFAULT_LINE_WORD_PERCENTAGE + lineSpace);

		String strs[] = str.split("\n");
		int maxlenth = 0;
		for (int s = 0; s < strs.length; s++) {
			maxlenth = Math.max(maxlenth, strs[s].length());
		}
		return (int) (oneWordLength * maxlenth);
	}

	/**************
	 * 
	 * 在知道宽度的情况下获取文字所需高度
	 * 
	 * @param lineSpace
	 *            列间距
	 * @param rowSpace
	 *            行间距
	 * @param str
	 *            文字
	 * @param paint
	 *            paint
	 * @param width
	 *            宽度
	 * @return
	 */
	static int getLayoutHeight(int lineSpace, int rowSpace, String str,
							   Paint paint, int width) {
		if (str == null) {
			return 0;
		}

		/* 当前绘制的行数 */
		long nowerDrawingLine = 0;
		/* 当前行数惠子的文字个数 */
		long nowerDrawingLineWordnum = -1;
		/* 单行能够绘制的文字个数 */
		long oneLineMaxWordNum = 1;
		/* 单个文字的宽度 */
		long oneWordLength = 0;
		/* 转换为chararray */
		char contentArray[] = str.toCharArray();
		/* 获取font的Metrics */
		FontMetrics fm = paint.getFontMetrics();
		/* 加上行间距 */
		int oneWordHeight = (int) (fm.descent - fm.ascent) + rowSpace;
		/* 通过一个繁体字，来取得这个文字的大小，设置绘制的文字的大小 */
		Rect rt = new Rect();
		paint.getTextBounds("繁", 0, 1, rt);

		/* 计算单个文字的宽度 */
		oneWordLength = (long) (rt.width() * DEFAULT_LINE_WORD_PERCENTAGE + lineSpace);

		/*计算一行最大文字的个数 */
		oneLineMaxWordNum = (int) width / oneWordLength;

		for (int i = 0; i < contentArray.length; i++) {
			// 在没回车换行的情况下
			if (contentArray[i] != '\n' && contentArray[i] != '\r') {
				nowerDrawingLineWordnum++;
				if (nowerDrawingLineWordnum >= oneLineMaxWordNum) {
					nowerDrawingLineWordnum = 0;
					nowerDrawingLine++;
				}
			} else if (contentArray[i] == '\n') {
				nowerDrawingLineWordnum = -1;
				nowerDrawingLine++;


			} else if (contentArray[i] == '\r') {
				nowerDrawingLineWordnum = -1;
			}

		}
		return (int) ((nowerDrawingLine + 1) * oneWordHeight + (fm.bottom - fm.top-fm.descent+fm.ascent)*2);
	}

	/*************
	 * 将String绘画到canvas里面
	 * 
	 * @param canvas
	 *            canvas
	 * @param str
	 *            需要绘制的string
	 * @param lineSpace
	 *            列间距
	 * @param rowSpace
	 *            行间距
	 * @param rect
	 *            绘画在canvas中的区域
	 * @param paint
	 *            绘画paint
	 * @param num
	 *            绘制开始的offset相对于str
	 * @param center
	 *            是否需要居中显示
	 * @return
	 * @throws
	 */
	static long stringLayout(Canvas canvas, int lineSpace, int rowSpace,
							 String str, Rect rect, Paint paint, long num, boolean center) {

		return stringLayout(canvas, lineSpace, rowSpace, str, rect.left,
				rect.top, rect.right, rect.bottom, paint, num, center);
	}

	/*************
	 * 将String绘画到canvas里面
	 * 
	 * @param canvas
	 *            canvas
	 * @param lineSpace
	 *            列间距
	 * @param rowSpace
	 *            行间距
	 * @param str
	 *            需要绘制的string
	 * @param rect
	 *            绘画在canvas中的区域
	 * @param paint
	 *            绘画paint
	 * @param num
	 *            绘制开始的offset相对于str
	 * @return
	 * @throws
	 */
	static long stringLayout(Canvas canvas, int lineSpace, int rowSpace,
							 String str, Rect rect, Paint paint, long num) {
		return stringLayout(canvas, lineSpace, rowSpace, str, rect, paint, num,
				false);
	}

	/***********
	 * 绘制文字，这里同上
	 * 
	 * @param canvas
	 *            canvas
	 * @param lineSpace
	 *            列间距
	 * @param rowSpace
	 *            行间距
	 * @param str
	 *            文字
	 * @param left
	 *            左边距
	 * @param top
	 *            顶边距
	 * @param right
	 *            右rect位置
	 * @param bottom
	 *            下方位置
	 * @param paint
	 *            paint
	 * @param num
	 *            起始位置，从当前String的第几个开始绘制
	 * @param centerfix
	 *            是否居中
	 * @return 绘制的文字个数
	 * @throws
	 */
	static long stringLayout(Canvas canvas, int lineSpace, int rowSpace,
							 String str, float left, float top, float right, float bottom,
							 Paint paint, long num, boolean centerfix) {

		/**** 针对等于零的情况作出处理 ****/
		if (num < 0 || str == null || "".equals(str)) {
			return 0;
		}

		/**** 定义draw的数量，用于之后返回到底绘制了多少个字符 ****/
		long drawedNum = 0;
		/**** 当前正在绘制的行数 ，默认0为第一行 ****/
		long nowerDrawingLine = 0;
		/**** 当前行绘制到第几个，默认是-1，先判断是否绘制之后会对其值进行增加操作 ****/
		long nowerDrawingLineWordnum = -1;
		/**** 能够绘制的最大的line的数目 ****/
		long maxCandrawLineNum = 1;
		/**** 单行能够显示几个 ****/
		long oneLineMaxWordNum = 1;
		/**** 单个字符给予的宽度 ****/
		long oneWordLength = 0;

		/**** 单个字符给予的高度，这里加上了行间距 ****/
		char contentArray[] = str.toCharArray();
		FontMetrics fm = paint.getFontMetrics();
		long oneWordHeight = (int) (fm.descent - fm.ascent) + rowSpace;

		/***** 最大的line行数 ******/
		maxCandrawLineNum = (int) (bottom - top) / oneWordHeight;

		Rect rt = new Rect();
		paint.getTextBounds("繁", 0, 1, rt);
		/***** 计算出单个字符的宽度 ******/
		oneWordLength = (int) (rt.width() * DEFAULT_LINE_WORD_PERCENTAGE + lineSpace);
		/***** 计算出每一行最大能够绘制的文字个数 ******/
		oneLineMaxWordNum = (int) (right - left) / oneWordLength;

		/**** 判断是否需要居中 *****/
		if (centerfix) {
			left = (int) (left + (right - left) % oneWordLength / 2);
		}
		/****** 开始绘制 *******/
		for (int i = (int) num; i < contentArray.length; i++) {
			long x;
			long y;
			if (contentArray[i] != '\n' && contentArray[i] != '\r') {
				/****** i这个字符需要绘制 *******/
				nowerDrawingLineWordnum++;
				/****** 绘制到最后一个的时候 ，需要绘制的自然就是下一行的第一个 *******/
				if (nowerDrawingLineWordnum >= oneLineMaxWordNum) {
					nowerDrawingLineWordnum = 0;
					/**** 当前绘制的line，增加一个 *****/
					nowerDrawingLine++;
					/**** 当绘制的line比最大数量还要大的时候 *****/
					if (nowerDrawingLine >= maxCandrawLineNum) {
						break;
					}
				}
			} else if (contentArray[i] == '\n') {
				/**** 如果说正好是回车换行 *****/
				nowerDrawingLineWordnum = -1;
				/**** 绘制文字的个数增加一个，假装已经绘制了 ****/
				drawedNum++;

				/**** 如果第一个字符就是\n,不进行换行操作，否则就进行换行操作 ****/
				if (i != num)
					nowerDrawingLine++;

				/**** 当绘制的line比最大数量还要大的时候 *****/
				if (nowerDrawingLine >= maxCandrawLineNum) {
					break;
				}
			} else if (contentArray[i] == '\r') {
				/**** 如果说正好是回车换行 *****/
				nowerDrawingLineWordnum = -1;
				/**** 假装已经绘制，数量增加 *****/
				drawedNum++;
				/**** 当绘制的line比最大数量还要大的时候，按理讲根本不会执行到这里额 *****/
				if (nowerDrawingLine >= maxCandrawLineNum) {
					break;
				}
			}
			/**** 设置下绘制的x坐标 *****/
			x = (long) left + nowerDrawingLineWordnum * oneWordLength
					+ lineSpace / 2;
			;
			/**** 设置下绘制的Y坐标 *****/
			y = (long) (top + nowerDrawingLine * oneWordHeight + oneWordHeight
					- rowSpace / 2);
			;

			/**** 如果说不是回车换行符 *****/
			if (contentArray[i] != '\r' && contentArray[i] != '\n') {
				/**** 开始绘制 ****/
				canvas.drawText(contentArray, i, 1, x, y, paint);
				drawedNum++;
			}
			/**** 最大的时候break ****/
			if (nowerDrawingLine >= maxCandrawLineNum) {
				break;
			}
		}

		return drawedNum;
	}

	/**********************
	 * 这里进行反向绘制，
	 * 
	 * @param canvas
	 *            canvas
	 * @param lineSpace
	 *            列间距
	 * @param rowSpace
	 *            行间距
	 * @param str
	 *            string
	 * @param left
	 *            左边
	 * @param top
	 *            头部
	 * @param right
	 *            右边
	 * @param bottom
	 *            底部
	 * @param paint
	 *            paint
	 * @param num
	 *            起始位置
	 * @param centerfix
	 *            是否横向居中
	 * @return
	 * @throws
	 */
	static long stringLayoutBack(Canvas canvas, int lineSpace, int rowSpace,
								 String str, float left, float top, float right, float bottom,
								 Paint paint, long num, boolean centerfix) {

		/**** 定义draw的数量，用于之后返回到底绘制了多少个字符 ****/
		long drawedNum = 0;
		/**** 当前正在绘制的行数 ，默认0为第一行 ****/
		long nowerDrawingLine = 0;
		/**** 当前行绘制到第几个，默认是-1，先判断是否绘制之后会对其值进行增加操作 ****/
		long nowerDrawingLineWordnum = -1;
		/**** 能够绘制的最大的line的数目 ****/
		long maxCandrawLineNum = 1;
		/**** 单行能够显示几个 ****/
		long oneLineMaxWordNum = 1;
		/**** 单个字符给予的宽度 ****/
		long oneWordLength = 0;
		/**** 单个字符给予的高度，这里加上了行间距 ****/
		char contentArray[] = str.toCharArray();

		/***** 最大的line行数 ******/
		FontMetrics fm = paint.getFontMetrics();
		int oneWordHeight = (int) (fm.descent - fm.ascent) + rowSpace;
		maxCandrawLineNum = (int) (bottom - top) / oneWordHeight;

		/***** 计算出单个字符的宽度 ******/
		Rect rt = new Rect();
		paint.getTextBounds("繁", 0, 1, rt);
		oneWordLength = (long) (rt.width() * DEFAULT_LINE_WORD_PERCENTAGE + lineSpace);

		/***** 计算出每一行最大能够绘制的文字个数 ******/
		oneLineMaxWordNum = (int) (right - left) / oneWordLength;

		/**** 判断是否需要居中 *****/
		if (centerfix) {
			left = (int) (left + (right - left) % oneWordLength / 2);
		}

		/**** 从反向开始绘制，当前正在绘制最后一个数 *****/
		nowerDrawingLineWordnum = oneLineMaxWordNum;

		/**** 从反向开始绘制,当前正在绘制最后一行 *****/
		nowerDrawingLine = maxCandrawLineNum - 1;

		/**** 特殊情况不做处理 *****/
		if (num < 1 || str == null) {
			return 0;
		}
		if (num - 1 > contentArray.length || contentArray == null) {
			return 0;
		}
		/******* 因为有回车会导致最后的换行比较蛋疼，所以先开始计算出前一个换行的位置 ********/
		long backNum = oneLineMaxWordNum;

		for (int t = (int) (num - 1); t > 0; t--) {
			if (contentArray[t] == '\n') {

				backNum = ((num - 1) - t) % oneLineMaxWordNum;
				if (backNum == 0) {
					backNum = oneLineMaxWordNum;
				}
				break;
			} else if (t == 1) {
				backNum = (num - 1) % oneLineMaxWordNum;
				break;
			}
		}
		/*********** 开始进行反向绘制 ***********/
		nowerDrawingLineWordnum = backNum;
		for (int i = (int) num - 1; i >= 0; i--) {
			long x;
			long y;
			if (contentArray[i] != '\n' && contentArray[i] != '\r') {
				/***** 绘制的个数减去一 *****/
				nowerDrawingLineWordnum--;
				/***** 绘制到第一个的时候开始向上换行 *****/
				if (nowerDrawingLineWordnum < 0) {
					nowerDrawingLineWordnum = oneLineMaxWordNum - 1;
					nowerDrawingLine--;
					/***** 绘制完成了，终止 *****/
					if (nowerDrawingLine < 0) {
						break;
					}
				}
			} else if (contentArray[i] == '\n') {

				/***** 遇到换行，开始继续向上计算 *****/
				long remainNum = oneLineMaxWordNum;
				for (int t = i - 1; t > 0; t--) {
					if (contentArray[t] == '\n') {
						remainNum = (i - t - 1) % oneLineMaxWordNum;
						if (remainNum == 0) {
							remainNum = oneLineMaxWordNum;
						}
						break;
					} else if (t == 1) {
						remainNum = i % oneLineMaxWordNum;
						break;
					}
				}
				/***** 正在绘制的减去一 *****/
				nowerDrawingLineWordnum = remainNum;
				/***** 绘制的文字增加一个 *****/
				drawedNum++;
				/***** 如果绘制的第一个字正好是\n,不换行，否则就换行 *****/
				if (i != num - 1)
					nowerDrawingLine--;
				/***** 绘制完成了break *****/
				if (nowerDrawingLine < 0) {
					break;
				}
			} else if (contentArray[i] == '\r') {

				/***** 遇到\r，开始继续向上计算remain的numbeer *****/
				long remainNum = oneLineMaxWordNum;
				for (int t = i - 1; t > 0; t--) {
					if (contentArray[t] == '\n') {
						remainNum = (i - t - 1) % oneLineMaxWordNum;
						if (remainNum == 0) {
							remainNum = oneLineMaxWordNum;
						}
						break;
					} else if (t == 1) {
						remainNum = i % oneLineMaxWordNum;
						break;
					}
				}
				nowerDrawingLineWordnum = remainNum;
				drawedNum++;
				/***** 绘制完成了break,这句话去掉没问题 *****/
				if (nowerDrawingLine < 0) {
					break;
				}
			}

			/**** 设置下绘制的x坐标 *****/
			x = (long) left + nowerDrawingLineWordnum * oneWordLength
					+ lineSpace / 2;

			/**** 设置下绘制的Y坐标 *****/
			y =  (long) (top + nowerDrawingLine * oneWordHeight + oneWordHeight
					- rowSpace / 2);

			/**** 如果说不是回车换行符 *****/
			if (contentArray[i] != '\r' && contentArray[i] != '\n') {
				/**** 开始绘制 ****/
				canvas.drawText(contentArray, i, 1, x, y, paint);
				drawedNum++;
			}
		}
		return drawedNum;
	}
}

package cn.emay.framework.common.media;

import java.awt.image.BufferedImage;

public class ImageUitls {

	/**
	 * 强制设置灰度化的方法（效果相对就差） 图片灰化（效果不行，不建议。据说：搜索“Java实现灰度化”，十有八九都是一种方法）
	 * 
	 * @param bufferedImage
	 *            待处理图片
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage grayImage1(BufferedImage bufferedImage) throws Exception {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage grayBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grayBufferedImage.setRGB(x, y, bufferedImage.getRGB(x, y));
			}
		}
		return grayBufferedImage;
	}

	/**
	 * 加权法灰度化（效果较好） 图片灰化（参考：http://www.codeceo.com/article/java-image-gray.html）
	 * 
	 * @param bufferedImage
	 *            待处理图片
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage grayImage(BufferedImage bufferedImage) throws Exception {

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage grayBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// 计算灰度值
				final int color = bufferedImage.getRGB(x, y);
				final int r = (color >> 16) & 0xff;
				final int g = (color >> 8) & 0xff;
				final int b = color & 0xff;
				int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
				int newPixel = colorToRGB(255, gray, gray, gray);
				grayBufferedImage.setRGB(x, y, newPixel);
			}
		}

		return grayBufferedImage;

	}

	/**
	 * 颜色分量转换为RGB值
	 * 
	 * @param alpha
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	private static int colorToRGB(int alpha, int red, int green, int blue) {

		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;

	}

}

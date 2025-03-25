package com.caiths.oculichatback.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图像处理工具类、给接口添加可解释性和可视化功能
 *
 * @author poboll
 * @version 1.0
 * @since 2025-03-25
 */
@Slf4j
public class ImageProcessingUtils {

    /**
     * 提取图像的绿色通道
     */
    public static byte[] extractGreenChannel(byte[] imageData) {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(imageData));
            BufferedImage greenChannel = new BufferedImage(
                    original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {
                    Color pixelColor = new Color(original.getRGB(x, y));
                    // 保留绿色通道，移除红色和蓝色
                    int green = pixelColor.getGreen();
                    Color newColor = new Color(0, green, 0);
                    greenChannel.setRGB(x, y, newColor.getRGB());
                }
            }

            return bufferedImageToByteArray(greenChannel);
        } catch (Exception e) {
            log.error("提取绿色通道失败", e);
            return imageData; // 失败时返回原图
        }
    }

    /**
     * 创建无红图像（移除红色通道）
     */
    public static byte[] createRedFreeImage(byte[] imageData) {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(imageData));
            BufferedImage redFree = new BufferedImage(
                    original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {
                    Color pixelColor = new Color(original.getRGB(x, y));
                    // 保留绿色和蓝色通道，移除红色
                    int green = pixelColor.getGreen();
                    int blue = pixelColor.getBlue();
                    Color newColor = new Color(0, green, blue);
                    redFree.setRGB(x, y, newColor.getRGB());
                }
            }

            return bufferedImageToByteArray(redFree);
        } catch (Exception e) {
            log.error("创建无红图像失败", e);
            return imageData; // 失败时返回原图
        }
    }

    /**
     * 增强图像对比度
     */
    public static byte[] enhanceContrast(byte[] imageData) {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(imageData));
            BufferedImage enhanced = new BufferedImage(
                    original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

            // 简单对比度调整因子 - 可根据需要调整
            double contrastFactor = 1.5;

            for (int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {
                    Color pixelColor = new Color(original.getRGB(x, y));

                    // 应用对比度增强
                    int red = clamp((int) ((pixelColor.getRed() - 128) * contrastFactor + 128));
                    int green = clamp((int) ((pixelColor.getGreen() - 128) * contrastFactor + 128));
                    int blue = clamp((int) ((pixelColor.getBlue() - 128) * contrastFactor + 128));

                    Color newColor = new Color(red, green, blue);
                    enhanced.setRGB(x, y, newColor.getRGB());
                }
            }

            return bufferedImageToByteArray(enhanced);
        } catch (Exception e) {
            log.error("增强图像对比度失败", e);
            return imageData; // 失败时返回原图
        }
    }

    /**
     * 将BufferedImage转换为字节数组
     */
    private static byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

    /**
     * 限制值在0-255范围内
     */
    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}

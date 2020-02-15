/*
 *
 * Copyright 2015-2016 magiclen.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.magiclen.imagedifferencer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import org.magiclen.magicimage.ImageBuffer;
import org.magiclen.magicimage.ImageColor;
import org.magiclen.magicimage.ImageExport;

/**
 * 計算並取得兩張圖片檔案內容的差異。
 *
 * @author Magic Len
 */
public class ImageDifferencer {

    /**
     * <p>
     * 程式進入點。
     * </p>
     *
     * <p>
     * 若要傳入參數，必須傳入兩個輸入檔案路徑、一個輸出檔案路徑，以及輸出圖片的格式。輸出圖片將預設使用PNG。
     * </p>
     *
     * @param args 傳入三個檔案路徑和輸出圖片格式(PNG、JPEG)
     */
    public static void main(final String[] args) {
	File inputFile1 = null, inputFile2 = null, outputFile3 = null;
	String format = null;
	try {
	    if (args == null || args.length < 3) {
		throw new Exception();
	    }
	    inputFile1 = new File(args[0]);
	    System.out.println("The file path of the image you want to load: ".concat(inputFile1.getAbsolutePath()));
	    inputFile2 = new File(args[1]);
	    System.out.println("The file path of the other image you want to load: ".concat(inputFile2.getAbsolutePath()));
	    outputFile3 = new File(args[2]);
	    System.out.println("The file path where you want to ouput: ".concat(outputFile3.getAbsolutePath()));
	    if (args.length > 3) {
		format = args[3];
	    }
	} catch (final Exception ex) {
	    final Scanner sc = new Scanner(System.in);
	    while (inputFile1 == null) {
		System.out.print("Please input the file path of the image you want to load: ");
		final String path = sc.nextLine().trim();
		if (path.length() == 0) {
		    continue;
		}
		try {
		    inputFile1 = new File(path);
		} catch (final Exception ex2) {

		}
	    }
	    while (inputFile2 == null) {
		System.out.print("Please input the file path of the other image you want to load: ");
		final String path = sc.nextLine().trim();
		if (path.length() == 0) {
		    continue;
		}
		try {
		    inputFile2 = new File(path);
		} catch (final Exception ex2) {

		}
	    }
	    while (outputFile3 == null) {
		System.out.print("Please input the file path where you want to ouput: ");
		final String path = sc.nextLine().trim();
		if (path.length() == 0) {
		    continue;
		}
		try {
		    outputFile3 = new File(path);
		} catch (final Exception ex2) {

		}
	    }
	    System.out.print("Please input the image format you want to ouput: ");
	    format = sc.nextLine().trim();
	}

	if (format == null || format.length() == 0) {
	    format = "PNG";
	} else {
	    format = format.toUpperCase();
	}

	final File output = difference(inputFile1, inputFile2, outputFile3, format);
	if (output == null) {
	    System.out.println("Can't do this.");
	} else {
	    System.out.println("Output: ".concat(output.getAbsolutePath()));
	}
    }

    /**
     * 計算圖片檔案中圖片的差異，並輸出到檔案中。
     *
     * @param inputFile1 傳入第一個圖片檔案
     * @param inputFile2 傳入第二個圖片檔案
     * @param outputFile3 傳入輸出的圖片檔案
     * @param format 傳入輸出的圖片格式
     * @return 傳回真正輸出的檔案
     */
    private static File difference(final File inputFile1, final File inputFile2, final File outputFile3, final String format) {
	BufferedImage[] temp = ImageBuffer.getBufferedImages(inputFile1);
	if (temp == null || temp.length < 1) {
	    return null;
	}

	final BufferedImage bi1 = temp[0];

	temp = ImageBuffer.getBufferedImages(inputFile2);
	if (temp == null || temp.length < 1) {
	    return null;
	}

	final BufferedImage bi2 = temp[0];

	final BufferedImage biD12 = ImageColor.distinct(bi1, bi2);
	if (biD12 == null) {
	    return null;
	}
	bi1.flush();
	bi2.flush();

	final BufferedImage biD12M = ImageColor.maximize(biD12, true, false);
	if (biD12M == null) {
	    return null;
	}
	if (biD12M != biD12) {
	    biD12.flush();
	}
	switch (format) {

	    case "JPEG":
	    case "JPG":
		return ImageExport.exportToJPEG(biD12M, outputFile3, 0.8f, false);
	    case "TIFF":
		return ImageExport.exportToTIFF(biD12M, outputFile3, 0f, true, false);
	    case "GIF":
		return ImageExport.exportToGIF(biD12M, outputFile3, false);
	    case "BMP":
		return ImageExport.exportToBMP(biD12M, outputFile3, false);
	    default:
		return ImageExport.exportToPNG(biD12M, outputFile3, false);
	}

    }
}

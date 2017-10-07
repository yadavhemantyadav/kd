/**
 * 
 */
package utils;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.testng.Reporter;

/**
 * 
 * @author AkashAggarwal
 * @Description Image Related methods go in this class
 * 
 */
public class ImageComparator {

	BufferedImage original = null;
	BufferedImage Url = null;
	Raster origanalImagedata = null;
	Raster urlImagedata = null;

	/**
	 * Compares Two Images
	 * 
	 * @param URL
	 * @param filePath
	 * @return
	 */
	public boolean compareImages(String URL, String filePath) {
		Reporter.log("Comparing Images from " + URL + " and " + filePath);

		// TODO: REFACTOR THIS ASAP
		boolean ret = true;
		try {

			if (URL.startsWith("http")) {
				original = ImageIO.read(new File(filePath));
				Url = ImageIO.read(new URL(URL));

				origanalImagedata = original.getData();
				urlImagedata = Url.getData();
			} else {
				original = ImageIO.read(new File(filePath));
				Url = ImageIO.read(new File(URL));

				origanalImagedata = original.getData();
				urlImagedata = Url.getData();

			}
			// Comparing the the two images for number of bands,width &
			// height.
			if (origanalImagedata.getNumBands() != urlImagedata.getNumBands()
					|| origanalImagedata.getWidth() != urlImagedata.getWidth()
					|| origanalImagedata.getHeight() != urlImagedata.getHeight()) {
				ret = false;
			} else {
				// Once the band ,width & height matches, comparing the
				// images.

				search: for (int i = 0; i < origanalImagedata.getNumBands(); ++i) {
					for (int x = 0; x < origanalImagedata.getWidth(); ++x) {
						for (int y = 0; y < origanalImagedata.getHeight(); ++y) {
							if (origanalImagedata.getSample(x, y, i) != urlImagedata.getSample(x, y, i)) {
								// If one of the result is false setting
								// the result as false and breaking the
								// loop.
								ret = false;
								break search;
							}
						}
					}
				}
			}

			if (ret == true) {
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
	}
}

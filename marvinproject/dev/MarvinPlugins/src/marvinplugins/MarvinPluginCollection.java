package marvinplugins;

import java.awt.Color;
import java.util.List;

import marvin.image.MarvinBlobSegment;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.image.MarvinSegment;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class MarvinPluginCollection {

	private static MarvinImagePlugin	alphaBoundary,
										blackAndWhite,
										brightnessAndContrast,
										boundaryFill,
										colorChannel,
										combineByMask,
										combineByTransparency,
										convolution,
										crop,
										determineSceneBackground,
										emboss,
										findSubimage,
										findTextRegions,
										flip,
										floodfillSegmentation,
										gaussianBlur,
										grayScale,
										harris,
										histogramEqualization,
										halftoneCircles,
										halftoneDithering,
										halftoneErrorDiffusion,
										halftoneRylanders,
										imageSlicer,
										invertColors,
										iteratedFuncionSystems,
										juliaSet,
										lindenmayer,
										mandelbrot,
										mergePhotos,
										moravec,
										morphologicalBoundary,
										morphologicalClosing,
										morphologicalDilation,
										morphologicalErosion,
										morphologicalOpening,
										morphologicalThinning,
										mosaic,
										pixelize,
										prewitt,
										quantizationGrayScale,
										roberts,
										rotate,
										scale,
										sobel,
										skew,
										sepia,
										statisticalMaximum,
										statisticalMedian,
										statisticalMinimum,
										statisticalMode,
										subtract,
										susan,
										skinColorDetection,
										television,
										tileTexture,
										thresholding,
										thresholdingNeighborhood,
										watershed;
	
	private static boolean[][] boolean_3x3 = new boolean[][]{
		{true,true,true},
		{true,true,true},
		{true,true,true}
	};
	
	/*==============================================================================================
	  | ALPHA BOUNDARY
	  ==============================================================================================*/
	/**
	 * Applies a gradient transparency to boundary pixels
	 * @param imageIn		input image
	 * @param radius		transparency radius
	 */
	public static void alphaBoundary(MarvinImage imageIn, int radius){
		alphaBoundary(imageIn.clone(), imageIn, radius);
	}
	
	/**
	 * Applies a gradient transparency to boundary pixels
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param radius		transparency radius
	 */
	public static void alphaBoundary(MarvinImage imageIn, MarvinImage imageOut, int radius){
		alphaBoundary = checkAndLoadImagePlugin(alphaBoundary, "org.marvinproject.image.color.alphaBoundary");
		alphaBoundary.setAttribute("radius", radius);
		alphaBoundary.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | BLACK AND WHITE
	  ==============================================================================================*/
	
	/**
	 * Converts image to black and white.
	 * @param imageIn		input image
	 * @param level			black and white level
	 */
	public static void blackAndWhite(MarvinImage image, int level){
		blackAndWhite(image, image, level);
	}
	
	/**
	 * Converts image to black and white.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param level			black and white level
	 */
	public static void blackAndWhite(MarvinImage imageIn, MarvinImage imageOut, int level){
		blackAndWhite(imageIn, imageOut,  level, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Converts image to black and white.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param level			black and white level
	 * @param mask			pixel mask
	 */
	public static void blackAndWhite(MarvinImage imageIn, MarvinImage imageOut, int level, MarvinImageMask mask){
		blackAndWhite = checkAndLoadImagePlugin(blackAndWhite, "org.marvinproject.image.color.blackAndWhite");
		blackAndWhite.setAttribute("level", level);
		blackAndWhite.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | BOUNDARY FILL
	  ==============================================================================================*/
	/**
	 * Fills a closed region of an image, given a start x,y coordinate. The region might be filled with a color or a texture tile.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param x				fill start X
	 * @param y				fill start Y
	 * @param color			color
	 */
	public static void boundaryFill(MarvinImage imageIn, MarvinImage imageOut, int x, int y, Color color){
		boundaryFill(imageIn, imageOut, x, y, color, null, 0);
	}
	
	/**
	 * Fills a closed region of an image, given a start x,y coordinate. The region might be filled with a color or a texture tile.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param x				fill start X
	 * @param y				fill start Y
	 * @param color			color
	 * @param threshold		accepted threshold to the specified color.
	 */
	public static void boundaryFill(MarvinImage imageIn, MarvinImage imageOut, int x, int y, Color color, int threshold){
		boundaryFill(imageIn, imageOut, x, y, color, null, threshold);
	}
	
	/**
	 * Fills a closed region of an image, given a start x,y coordinate. The region might be filled with a color or a texture tile.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param x				fill start X
	 * @param y				fill start Y
	 * @param color			color
	 * @param tile			tile image for texture based filling
	 * @param threshold		accepted threshold to the specified color.
	 */
	public static void boundaryFill(MarvinImage imageIn, MarvinImage imageOut, int x, int y, Color color, MarvinImage tile, int threshold){
		boundaryFill = checkAndLoadImagePlugin(boundaryFill, "org.marvinproject.image.fill.boundaryFill");
		boundaryFill.setAttribute("x", x);
		boundaryFill.setAttribute("y", y);
		boundaryFill.setAttribute("color", color);
		boundaryFill.setAttribute("tile", tile);
		boundaryFill.setAttribute("threshold", threshold);
		boundaryFill.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | BRIGHTNESS AND CONTRAST
	  ==============================================================================================*/
	/**
	 * Manipulate image brightness and contrast
	 * @param imageIn		input image
	 * @param brightness	brightness variation [-127, 127]
	 * @param contrast		contrast variation	 [-127, 127]
	 */
	public static void brightnessAndContrast(MarvinImage image, int brightness, int contrast){
		brightnessAndContrast(image, image, brightness, contrast);
	}
	
	/**
	 * Manipulate image brightness and contrast
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param brightness	brightness variation [-127, 127]
	 * @param contrast		contrast variation	 [-127, 127]
	 */
	public static void brightnessAndContrast(MarvinImage imageIn, MarvinImage imageOut, int brightness, int contrast){
		brightnessAndContrast(imageIn, imageOut,  brightness, contrast, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Manipulate image brightness and contrast
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param brightness	brightness variation [-127, 127]
	 * @param contrast		contrast variation	 [-127, 127]
	 * @param mask			pixel mask
	 */
	public static void brightnessAndContrast(MarvinImage imageIn, MarvinImage imageOut, int brightness, int contrast, MarvinImageMask mask){
		brightnessAndContrast = checkAndLoadImagePlugin(brightnessAndContrast, "org.marvinproject.image.color.brightnessAndContrast");
		brightnessAndContrast.setAttribute("brightness", brightness);
		brightnessAndContrast.setAttribute("contrast", contrast);
		brightnessAndContrast.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | COMBINE BY MASK
	  ==============================================================================================*/
	/**
	 * Combine two images considering a given color as transparency.
	 * @param imageA		input image A
	 * @param imageB		input image B
	 * @param imageOut		output image
	 * @param x				position x of image B on image A
	 * @param y				position y of image B on image A
	 * @param colorMask		Transparent color
	 */
	public static void combineByMask(MarvinImage imageA, MarvinImage imageB, MarvinImage imageOut, int x, int y, Color colorMask){
		combineByMask = checkAndLoadImagePlugin(combineByMask, "org.marvinproject.image.combine.combineByMask");
		combineByMask.setAttribute("xi", x);
		combineByMask.setAttribute("yi", y);
		combineByMask.setAttribute("combinationImage", imageB);
		combineByMask.setAttribute("colorMask", colorMask);
		combineByMask.process(imageA, imageOut);
	}
	
	/*==============================================================================================
	  | COMBINE BY TRANSPARENCY
	  ==============================================================================================*/
	/**
	 * Combine two images having transparency.
	 * @param imageA		input image A
	 * @param imageB		input image B
	 * @param imageOut		output image
	 * @param x				position x of image B on image A
	 * @param y				position y of image B on image A
	 * @param transparency
	 */
	public static void combineByTransparency(MarvinImage imageA, MarvinImage imageB, MarvinImage imageOut, int x, int y, int transparency){
		combineByTransparency = checkAndLoadImagePlugin(combineByTransparency, "org.marvinproject.image.combine.combineByTransparency");
		combineByTransparency.setAttribute("xi", x);
		combineByTransparency.setAttribute("yi", y);
		combineByTransparency.setAttribute("combinationImage", imageB);
		combineByTransparency.setAttribute("transparency", transparency);
		combineByTransparency.process(imageA, imageOut);
	}
	
	/*==============================================================================================
	  | CONVOLUTION
	  ==============================================================================================*/
	/**
	 * Convolution
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param matrix		convolution matrix
	 */
	public static void convolution(MarvinImage imageIn, MarvinImage imageOut, double[][] matrix){
		convolution = checkAndLoadImagePlugin(convolution, "org.marvinproject.image.convolution");
		convolution.setAttribute("matrix", matrix);
		convolution.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | COLOR CHANNEL
	  ==============================================================================================*/
	
	/**
	 * Manipulates image color channels
	 * @param imageIn		input image
	 * @param red			red manipulation [-100, 100]
	 * @param green			green manipulation [-100, 100]
	 * @param blue			blue manipulation [-100, 100]
	 */
	public static void colorChannel(MarvinImage image, int red, int green, int blue){
		colorChannel(image, image, red, green, blue);
	}
	
	/**
	 * Manipulates image color channels
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param red			red manipulation [-100, 100]
	 * @param green			green manipulation [-100, 100]
	 * @param blue			blue manipulation [-100, 100]
	 */
	public static void colorChannel(MarvinImage imageIn, MarvinImage imageOut, int red, int green, int blue){
		colorChannel(imageIn, imageOut, red, green, blue, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Manipulates image color channels
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param red			red manipulation [-100, 100]
	 * @param green			green manipulation [-100, 100]
	 * @param blue			blue manipulation [-100, 100]
	 * @param mask			pixel mask
	 */
	public static void colorChannel(MarvinImage imageIn, MarvinImage imageOut, int red, int green, int blue, MarvinImageMask mask){
		colorChannel = checkAndLoadImagePlugin(colorChannel, "org.marvinproject.image.color.colorChannel");
		colorChannel.setAttribute("red", red);
		colorChannel.setAttribute("green", green);
		colorChannel.setAttribute("blue", blue);
		colorChannel.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | CROP
	  ==============================================================================================*/
	/**
	 * Image cropping
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param x				subimage x
	 * @param y				subimage y
	 * @param width			subimage width
	 * @param height		subimage height
	 */
	public static void crop(MarvinImage imageIn, MarvinImage imageOut, int x, int y, int width, int height){
		
		x = Math.min(Math.max(x, 0), imageIn.getWidth());
		y = Math.min(Math.max(y, 0), imageIn.getHeight());
		
		if(x + width > imageIn.getWidth()){
			width = imageIn.getWidth() - x;
		}
		if(y + height > imageIn.getHeight()){
			height = imageIn.getHeight() - y;
		}
		
		crop = checkAndLoadImagePlugin(crop, "org.marvinproject.image.segmentation.crop");
		crop.setAttribute("x", x);
		crop.setAttribute("y", y);
		crop.setAttribute("width", width);
		crop.setAttribute("height", height);
		crop.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | DETERMINE SCENE BACKGROUND
	  ==============================================================================================*/
	/**
	 * Estimate the background from a set of images from the same scene.
	 * @param images		list of images from the same scene
	 * @param imageOut		estimated background
	 * @param threshold		threshold considered by the pixel comparison.
	 */
	public static void determineSceneBackground(List<MarvinImage> images, MarvinImage imageOut, int threshold){
		determineSceneBackground = checkAndLoadImagePlugin(determineSceneBackground, "org.marvinproject.image.background.determineSceneBackground");
		determineSceneBackground.setAttribute("threshold", threshold);
		determineSceneBackground.process(images, imageOut);	
	}
	
	/*==============================================================================================
	  | FIND SUBIMAGE
	  ==============================================================================================*/
	/**
	 * Find instances of a given image in another image with perfect matching (exactly the same pixels value).
	 * @param subimage		image to be found
	 * @param original		input image
	 * @return				list of image segments.
	 */
	public static List<MarvinSegment> findAllSubimages(MarvinImage subimage, MarvinImage original){
		return findAllSubimages(subimage, original, 1.0);
	}
	
	/**
	 * Find instances of a given image in another image.
	 * @param subimage		image to be found
	 * @param original		input image
	 * @param similarity	match percentage [0.0, 1.0]
	 * @return				list of image segments.
	 */
	public static List<MarvinSegment> findAllSubimages(MarvinImage subimage, MarvinImage original, double similarity){
		findSubimage = checkAndLoadImagePlugin(flip, "org.marvinproject.image.pattern.findSubimage");
		findSubimage.setAttribute("subimage", subimage);
		findSubimage.setAttribute("similarity", similarity);
		MarvinAttributes output = new MarvinAttributes();
		findSubimage.process(original, null, output);
		return (List<MarvinSegment>)output.get("matches");
	}
	
	/**
	 * Find the first instance of a given image in another image.
	 * @param subimage		image to be found
	 * @param original		input image
	 * @param startX		search start x
	 * @param startY		search start y
	 * @return				the image segment of the first instance found.
	 */
	public static MarvinSegment findSubimage(MarvinImage subimage, MarvinImage original, int startX, int startY){
		return findSubimage(subimage, original, startX, startY, 1.0);
	}
	
	/**
	 * Find the first instance of a given image in another image.
	 * @param subimage		image to be found
	 * @param original		input image
	 * @param startX		search start x
	 * @param startY		search start y
	 * @param similarity	match percentage [0.0, 1.0]
	 * @return				the image segment of the first instance found.
	 */
	public static MarvinSegment findSubimage(MarvinImage subimage, MarvinImage original, int startX, int startY, double similarity){
		findSubimage = checkAndLoadImagePlugin(flip, "org.marvinproject.image.pattern.findSubimage");
		findSubimage.setAttribute("subimage", subimage);
		findSubimage.setAttribute("similarity", similarity);
		findSubimage.setAttribute("findAll", false);
		findSubimage.setAttribute("startX", startX);
		findSubimage.setAttribute("startY", startY);
		MarvinAttributes output = new MarvinAttributes();
		findSubimage.process(original, null, output);
		
		List<MarvinSegment> ret =  (List<MarvinSegment>)output.get("matches");
		if(ret.size() > 0){
			return ret.get(0);
		}
		return null;
	}
	
	/*==============================================================================================
	  | FIND TEXT REGIONS
	  ==============================================================================================*/
	/**
	 * Find text regions in a given image.
	 * @param imageIn				input image
	 * @param maxWhiteSpace			max number of white pixels in the text pattern
	 * @param maxFontLineWidth		max number of black pixels in the text pattern
	 * @param minTextWidth			min text width in pixels
	 * @param grayScaleThreshold	gray scale threshold for conveting image to black and white 
	 * @return						list of image segments containing texts.
	 */
	public static List<MarvinSegment> findTextRegions(MarvinImage imageIn, int maxWhiteSpace, int maxFontLineWidth, int minTextWidth, int grayScaleThreshold){
		findTextRegions = checkAndLoadImagePlugin(findTextRegions, "org.marvinproject.image.pattern.findTextRegions");
		findTextRegions.setAttribute("maxWhiteSpace", maxWhiteSpace);
		findTextRegions.setAttribute("maxFontLineWidth", maxFontLineWidth);
		findTextRegions.setAttribute("minTextWidth", minTextWidth);
		findTextRegions.setAttribute("grayScaleThreshold", grayScaleThreshold);
		MarvinAttributes output = new MarvinAttributes();
		findTextRegions.process(imageIn, null, output);
		return (List<MarvinSegment>)output.get("matches");
	}
	
	/*==============================================================================================
	  | EMBOSS
	  ==============================================================================================*/
	/**
	 * Emboss filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void emboss(MarvinImage imageIn, MarvinImage imageOut){
		emboss(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Emboss filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param mask			pixel mask			
	 */
	public static void emboss(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		emboss = checkAndLoadImagePlugin(emboss, "org.marvinproject.image.color.emboss");
		emboss.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | FLIP
	  ==============================================================================================*/
	/**
	 * Flips the image horizontally
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void flipHorizontally(MarvinImage imageIn, MarvinImage imageOut){
		flip = checkAndLoadImagePlugin(flip, "org.marvinproject.image.transform.flip");
		flip.setAttribute("flip", "horizontal");
		flip.process(imageIn, imageOut);
	}
	
	/**
	 * Flips the image vertically
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void flipVertically(MarvinImage imageIn, MarvinImage imageOut){
		flip = checkAndLoadImagePlugin(flip, "org.marvinproject.image.transform.flip");
		flip.setAttribute("flip", "vertical");
		flip.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | FLOODFILL SEGMENTATION
	  ==============================================================================================*/
	public static MarvinSegment[] floodfillSegmentation(MarvinImage imageIn){
		floodfillSegmentation = checkAndLoadImagePlugin(floodfillSegmentation, "org.marvinproject.image.segmentation.floodfillSegmentation");
		floodfillSegmentation.setAttribute("returnType", "MarvinSegment");
		MarvinAttributes output = new MarvinAttributes();
		floodfillSegmentation.process(imageIn, null, output);
		return (MarvinSegment[])output.get("segments");
	}
	
	public static MarvinBlobSegment[] floodfillSegmentationBlob(MarvinImage imageIn){
		floodfillSegmentation = checkAndLoadImagePlugin(floodfillSegmentation, "org.marvinproject.image.segmentation.floodfillSegmentation");
		floodfillSegmentation.setAttribute("returnType", "MarvinBlobSegment");
		MarvinAttributes output = new MarvinAttributes();
		floodfillSegmentation.process(imageIn, null, output);
		return (MarvinBlobSegment[])output.get("blobSegments");
	}

	/*==============================================================================================
	  | GAUSSIAN BLUR
	  ==============================================================================================*/
	/**
	 * Gaussian blur filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param radius		blur radius
	 */
	public static void gaussianBlur(MarvinImage imageIn, MarvinImage imageOut, int radius){
		gaussianBlur(imageIn, imageOut, radius, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Gaussian blur filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param radius		blur radius
	 * @param mask			pixel mask
	 */
	public static void gaussianBlur(MarvinImage imageIn, MarvinImage imageOut, int radius, MarvinImageMask mask){
		gaussianBlur = checkAndLoadImagePlugin(gaussianBlur, "org.marvinproject.image.blur.gaussianBlur");
		gaussianBlur.setAttribute("radius", radius);
		gaussianBlur.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | GRAY SCALE
	  ==============================================================================================*/
	/**
	 * Gray scale filter
	 * @param imageIn		input image
	 */
	public static void grayScale(MarvinImage image){
		grayScale(image, image);
	}
	
	/**
	 * Gray scale filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void grayScale(MarvinImage imageIn, MarvinImage imageOut){
		grayScale(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Gray scale filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param mask			pixel mask
	 */
	public static void grayScale(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		grayScale = checkAndLoadImagePlugin(grayScale, "org.marvinproject.image.color.grayScale");
		grayScale.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | HALFTONE CIRCLES
	  ==============================================================================================*/
	/**
	 * Image halftone using circles approach
	 * @param imageIn			input image
	 * @param imageOut			output image
	 * @param circleWidth		circle max width
	 * @param shift				circle shift
	 * @param circlesDistance	circles minimum distance
	 * @param mask				pixel mask
	 */
	public static void halftoneCircles(MarvinImage imageIn, MarvinImage imageOut, int circleWidth, int shift, int circlesDistance, MarvinImageMask mask){
		halftoneCircles = checkAndLoadImagePlugin(halftoneCircles, "org.marvinproject.image.halftone.circles");
		halftoneCircles.setAttribute("circleWidth", circleWidth);
		halftoneCircles.setAttribute("shift", shift);
		halftoneCircles.setAttribute("circlesDistance", circlesDistance);
		halftoneCircles.process(imageIn, imageOut, mask);
	}
	
	/**
	 * Image halftone using circles approach
	 * @param imageIn			input image
	 * @param imageOut			output image
	 * @param circleWidth		circle max width
	 * @param shift				circle shift
	 * @param circlesDistance	circles minimum distance
	 */
	public static void halftoneCircles(MarvinImage imageIn, MarvinImage imageOut, int circleWidth, int shift, int circlesDistance){
		halftoneCircles(imageIn, imageOut, circleWidth, shift, circlesDistance, MarvinImageMask.NULL_MASK);
	}
	
	/*==============================================================================================
	  | HALFTONE DITHERING
	  ==============================================================================================*/
	/**
	 * Image halftone using dithering approach
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void halftoneDithering(MarvinImage imageIn, MarvinImage imageOut){
		halftoneDithering = checkAndLoadImagePlugin(halftoneDithering, "org.marvinproject.image.halftone.dithering");
		halftoneDithering.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | HALFTONE ERROR DIFFUSION
	  ==============================================================================================*/
	/**
	 *  Image halftone using error diffusion approach
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void halftoneErrorDiffusion(MarvinImage imageIn, MarvinImage imageOut){
		halftoneErrorDiffusion = checkAndLoadImagePlugin(halftoneErrorDiffusion, "org.marvinproject.image.halftone.errorDiffusion");
		halftoneErrorDiffusion.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | HALFTONE RAYLANDERS
	  ==============================================================================================*/
	/**
	 *  Image halftone using raylander's approach
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void halftoneRaylanders(MarvinImage imageIn, MarvinImage imageOut){
		halftoneRylanders = checkAndLoadImagePlugin(halftoneRylanders, "org.marvinproject.image.halftone.rylanders");
		halftoneRylanders.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | HARRIS CORNER
	  ==============================================================================================*/
	/**
	 * Harris corner detector
	 * @param imageIn		input image
	 * @param matrixSize	matrix size
	 * @param threshold		corners estimation threshold
	 * @param k				param k
	 */
	public static void harrisCorner(MarvinImage imageIn, int matrixSize, int threshold, double k){
		harris = checkAndLoadImagePlugin(harris, "org.marvinproject.image.corner.harris");
		harris.setAttribute("matrixSize", matrixSize);
		harris.setAttribute("threshold", threshold);
		harris.setAttribute("k", k);
		harris.process(imageIn, imageIn);
	}
	
	/*==============================================================================================
	  | HISTOGRAM EQUALIZATION
	  ==============================================================================================*/
	/**
	 * Histogram based image equalization.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void histogramEqualization(MarvinImage imageIn, MarvinImage imageOut){
		histogramEqualization = checkAndLoadImagePlugin(histogramEqualization, "org.marvinproject.image.equalization.histogramEqualization");
		histogramEqualization.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | INVERT COLORS
	  ==============================================================================================*/
	/**
	 * Inverts image colors.
	 * @param imageIn		input image
	 */
	public static void invertColors(MarvinImage image){
		invertColors(image, image);
	}
	
	/**
	 * Inverts image colors.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void invertColors(MarvinImage imageIn, MarvinImage imageOut){
		invertColors(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Inverts image colors.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param mask			pixel mask
	 */
	public static void invertColors(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		invertColors = checkAndLoadImagePlugin(invertColors, "org.marvinproject.image.color.invert");
		invertColors.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | MERGE PHOTOS
	  ==============================================================================================*/
	/**
	 * Merge multiple photos from the same scene.
	 * @param images		input image
	 * @param imageOut		output image
	 * @param threshold		threshold considering in pixel comparison.
	 */
	public static void mergePhotos(List<MarvinImage> images, MarvinImage imageOut, int threshold){
		mergePhotos = checkAndLoadImagePlugin(mergePhotos, "org.marvinproject.image.combine.mergePhotos");
		mergePhotos.setAttribute("threshold", threshold);
		mergePhotos.process(images, imageOut);	
	}
	
	/*==============================================================================================
	  | MORAVEC
	  ==============================================================================================*/
	/**
	 * Moravec corner detection
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param matrixSize	matrix size
	 * @param threshold		corners estimation threshold
	 * @return				cornerness map
	 */
	public static int[][] moravec(MarvinImage imageIn, MarvinImage imageOut, int matrixSize, int threshold){
		moravec = checkAndLoadImagePlugin(moravec, "org.marvinproject.image.corner.moravec");
		moravec.setAttribute("matrixSize", matrixSize);
		moravec.setAttribute("threshold", threshold);
		MarvinAttributes ret = new MarvinAttributes();
		moravec.process(imageIn, imageOut, ret);
		return (int[][])ret.get("cornernessMap");
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL BOUNDARY
	  ==============================================================================================*/
	/**
	 * Morphological boundary operation
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void morphologicalBoundary(MarvinImage imageIn, MarvinImage imageOut){
		morphologicalBoundary = checkAndLoadImagePlugin(morphologicalBoundary, "org.marvinproject.image.morphological.boundary");
		morphologicalBoundary.setAttribute("matrix", boolean_3x3);
		morphologicalBoundary.process(imageIn, imageOut);
	}
	
	/**
	 * Morphological boundary operation
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param matrix		operation matrix.
	 */
	public static void morphologicalBoundary(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalBoundary = checkAndLoadImagePlugin(morphologicalBoundary, "org.marvinproject.image.morphological.boundary");
		morphologicalBoundary.setAttribute("matrix", matrix);
		morphologicalBoundary.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL CLOSING
	  ==============================================================================================*/
	/**
	 * Morphological closing operation
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param matrix		matrix
	 */
	public static void morphologicalClosing(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalClosing = checkAndLoadImagePlugin(morphologicalClosing, "org.marvinproject.image.morphological.closing");
		morphologicalClosing.setAttribute("matrix", matrix);
		morphologicalClosing.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL DILATION
	  ==============================================================================================*/
	/**
	 * Morphological dilation operation
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param matrix		matrix
	 */
	public static void morphologicalDilation(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalDilation = checkAndLoadImagePlugin(morphologicalDilation, "org.marvinproject.image.morphological.dilation");
		morphologicalDilation.setAttribute("matrix", matrix);
		morphologicalDilation.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL EROSION
	  ==============================================================================================*/
	/**
	 * Morphological erosion operation
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param matrix		matrix
	 */
	public static void morphologicalErosion(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalErosion = checkAndLoadImagePlugin(morphologicalErosion, "org.marvinproject.image.morphological.erosion");
		morphologicalErosion.setAttribute("matrix", matrix);
		morphologicalErosion.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL OPENING
	  ==============================================================================================*/
	/**
	 * Morphological opening operation
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param matrix		matrix
	 */
	public static void morphologicalOpening(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalOpening = checkAndLoadImagePlugin(morphologicalOpening, "org.marvinproject.image.morphological.opening");
		morphologicalOpening.setAttribute("matrix", matrix);
		morphologicalOpening.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL THINNING
	  ==============================================================================================*/
	/**
	 * Morphological thinning operation
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void morphologicalThinning(MarvinImage imageIn, MarvinImage imageOut){
		morphologicalThinning = checkAndLoadImagePlugin(morphologicalThinning, "org.marvinproject.image.morphological.thinning");
		morphologicalThinning.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MOSAIC
	  ==============================================================================================*/
	/**
	 * Mosaic artistic filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param shape			shape ("squares", "triangles")
	 * @param width			mosaic width
	 * @param border		has border?
	 */
	public static void mosaic(MarvinImage imageIn, MarvinImage imageOut, String shape, int width, boolean border){
		mosaic = checkAndLoadImagePlugin(mosaic, "org.marvinproject.image.artistic.mosaic");
		mosaic.setAttribute("shape", shape);
		mosaic.setAttribute("width", width);
		mosaic.setAttribute("border", border);
		mosaic.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | PIXELIZE
	  ==============================================================================================*/
	public static void pixelize(MarvinImage imageIn, MarvinImage imageOut, int squareSide){
		pixelize(imageIn, imageOut, squareSide, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Pixelize filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param squareSide	square side
	 * @param mask			pixel mask
	 */
	public static void pixelize(MarvinImage imageIn, MarvinImage imageOut, int squareSide, MarvinImageMask mask){
		pixelize = checkAndLoadImagePlugin(pixelize, "org.marvinproject.image.blur.pixelize");
		pixelize.setAttribute("squareSide", squareSide);
		pixelize.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | PREWITT
	  ==============================================================================================*/
	public static void prewitt(MarvinImage imageIn, MarvinImage imageOut){
		prewitt(imageIn, imageOut, 1, MarvinImageMask.NULL_MASK);
	}
	
	public static void prewitt(MarvinImage imageIn, MarvinImage imageOut, double intensity){
		prewitt(imageIn, imageOut, intensity, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Edge detection using prewitt approach
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param intensity		edge intensity
	 * @param mask			pixel mask
	 */
	public static void prewitt(MarvinImage imageIn, MarvinImage imageOut, double intensity, MarvinImageMask mask){
		prewitt = checkAndLoadImagePlugin(prewitt, "org.marvinproject.image.edge.prewitt");
		prewitt.setAttribute("intensity", intensity);
		prewitt.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | QUANTIZATION GRAY SCALE
	  ==============================================================================================*/
	/**
	 * Gray scale color quantization
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param shades		number of shades of gray
	 */
	public static void quantizationGrayScale(MarvinImage imageIn, MarvinImage imageOut, int shades){
		quantizationGrayScale = checkAndLoadImagePlugin(quantizationGrayScale, "org.marvinproject.image.quantization.grayScaleQuantization");
		quantizationGrayScale.setAttributes("shades", shades);
		quantizationGrayScale.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | ROBERTS
	  ==============================================================================================*/
	/**
	 * Edge detection using roberts approach
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void roberts(MarvinImage imageIn, MarvinImage imageOut){
		roberts(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Edge detection using roberts approach
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param mask			pixel mask
	 */
	public static void roberts(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		roberts = checkAndLoadImagePlugin(roberts, "org.marvinproject.image.edge.roberts");
		roberts.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SCALE
	  ==============================================================================================*/
	/**
	 * Scale image.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param width			new width
	 * @param height		new height
	 */
	public static void scale(MarvinImage imageIn, MarvinImage imageOut, int width, int height){
		scale = checkAndLoadImagePlugin(roberts, "org.marvinproject.image.transform.scale");
		scale.setAttribute("newWidth", width);
		scale.setAttribute("newHeight", height);
		scale.process(imageIn, imageOut);
	}
	
	/**
	 * Scale image.
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param width			new width. Height is set to be proportional to the original image aspect.
	 */
	public static void scale(MarvinImage imageIn, MarvinImage imageOut, int width){
		int cWidth = imageIn.getWidth();
		int cHeight = imageIn.getHeight();
		double cFactor = (double)cHeight/cWidth;
		int newHeight= (int)Math.ceil(cFactor*width);
		scale(imageIn, imageOut, width, newHeight);
	}
	
	
	/*==============================================================================================
	  | SEPIA
	  ==============================================================================================*/
	
	/**
	 * Sepia filter
	 * @param imageIn		input image
	 * @param intensity		filter intensity
	 */
	public static void sepia(MarvinImage image, int intensity){
		sepia(image, image, intensity);
	}
	
	/**
	 * Sepia filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param intensity		filter intensity
	 */
	public static void sepia(MarvinImage imageIn, MarvinImage imageOut, int intensity){
		sepia(imageIn, imageOut, intensity, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Sepia filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param intensity		filter intensity
	 * @param mask			pixel mask.
	 */
	public static void sepia(MarvinImage imageIn, MarvinImage imageOut, int intensity, MarvinImageMask mask){
		sepia = checkAndLoadImagePlugin(sepia, "org.marvinproject.image.color.sepia");
		sepia.setAttribute("intensity", intensity);
		sepia.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SOBEL
	  ==============================================================================================*/
	/**
	 * Edge detection using sobel approach
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void sobel(MarvinImage imageIn, MarvinImage imageOut){
		sobel(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Edge detection using sobel approach
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param mask			pixel mask
	 */
	public static void sobel(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		sobel = checkAndLoadImagePlugin(sobel, "org.marvinproject.image.edge.sobel");
		sobel.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SKIN COLOR DETECTION
	  ==============================================================================================*/
	
	/**
	 * Human skin color filter. 
	 * @param imageIn		input image
	 */
	public static void skinColorDetection(MarvinImage image){
		skinColorDetection(image, image);
	}
	
	/**
	 * Human skin color filter. 
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void skinColorDetection(MarvinImage imageIn, MarvinImage imageOut){
		skinColorDetection(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Human skin color filter. 
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param mask			pixel mask
	 */
	public static void skinColorDetection(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		skinColorDetection = checkAndLoadImagePlugin(skinColorDetection, "org.marvinproject.image.color.skinColorDetection");
		skinColorDetection.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SUSAN CORNER
	  ==============================================================================================*/
	/**
	 * Corner detection using susan approach
	 * @param imageIn		input image
	 * @param matrixSize	matrix size
	 * @param threshold		corner estimation threshold
	 */
	public static void susanCorner(MarvinImage imageIn, int matrixSize, int threshold){
		susan = checkAndLoadImagePlugin(susan, "org.marvinproject.image.corner.susan");
		susan.setAttribute("matrixSize", matrixSize);
		susan.setAttribute("threshold", threshold);
		susan.process(imageIn, imageIn);
	}
	
	/*==============================================================================================
	  | TELEVISION
	  ==============================================================================================*/
	/**
	 * Television artisic filter
	 * @param imageIn		input image
	 */
	public static void television(MarvinImage image){
		television(image, image);
	}
	
	/**
	 * Television artisic filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 */
	public static void television(MarvinImage imageIn, MarvinImage imageOut){
		television(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Television artisic filter
	 * @param imageIn		input image
	 * @param imageOut		output image
	 * @param mask			pixel mask
	 */
	public static void television(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		television = checkAndLoadImagePlugin(television, "org.marvinproject.image.artistic.television");
		television.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | THRESHOLDING
	  ==============================================================================================*/
	/**
	 * Converts pixels to black and white given a threshold
	 * @param imageIn			input image
	 * @param threshold			threshold
	 */
	public static void thresholding(MarvinImage image, int threshold){
		thresholding(image, image, threshold);
	}
	
	/**
	 * Converts pixels to black and white given a threshold
	 * @param imageIn			input image
	 * @param threshold			threshold
	 * @param thresholdRange	threshold values range
	 */
	public static void thresholding(MarvinImage image, int threshold, int thresholdRange){
		thresholding(image, image, threshold, thresholdRange);
	}
	
	/**
	 * Converts pixels to black and white given a threshold
	 * @param imageIn			input image
	 * @param imageOut			output image
	 * @param threshold			threshold
	 */
	public static void thresholding(MarvinImage imageIn, MarvinImage imageOut, int threshold){
		thresholding(imageIn, imageOut, threshold, -1);
	}
	
	/**
	 * Converts pixels to black and white given a threshold
	 * @param imageIn			input image
	 * @param imageOut			output image
	 * @param threshold			threshold
	 * @param thresholdRange	threshold values range
	 */
	public static void thresholding(MarvinImage imageIn, MarvinImage imageOut, int threshold, int thresholdRange){
		thresholding(imageIn, imageOut, threshold, thresholdRange, MarvinImageMask.NULL_MASK);
	}
	
	/**
	 * Converts pixels to black and white given a threshold
	 * @param imageIn			input image
	 * @param imageOut			output image
	 * @param threshold			threshold
	 * @param thresholdRange	threshold values range
	 * @param mask				pixel mask
	 */
	public static void thresholding(MarvinImage imageIn, MarvinImage imageOut, int threshold, int thresholdRange, MarvinImageMask mask){
		thresholding = checkAndLoadImagePlugin(thresholding, "org.marvinproject.image.color.thresholding");
		thresholding.setAttribute("threshold",threshold);
		thresholding.setAttribute("thresholdRange",thresholdRange);
		thresholding.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | THRESHOLDING NEIGHBORHOOD
	  ==============================================================================================*/
	/**
	 * Converts pixels to black and white considering a threshold to the average intensity of pixel neighbors
	 * @param imageIn							input image
	 * @param imageOut							output image
	 * @param thresholdPercentageOfAverage		threshold percentage of average intensity [0, 1.0]
	 * @param neighborhoodSide					side of the neighborhood square
	 * @param samplingPixelDistance				1 to process every pixel, higher values to skip pixels in the sampling process.
	 */
	public static void thresholdingNeighborhood(MarvinImage imageIn, MarvinImage imageOut, double thresholdPercentageOfAverage, int neighborhoodSide, int samplingPixelDistance){
		thresholdingNeighborhood = checkAndLoadImagePlugin(thresholding, "org.marvinproject.image.color.thresholdingNeighborhood");
		thresholdingNeighborhood.setAttribute("thresholdPercentageOfAverage",thresholdPercentageOfAverage);
		thresholdingNeighborhood.setAttribute("neighborhoodSide",neighborhoodSide);
		thresholdingNeighborhood.setAttribute("samplingPixelDistance",samplingPixelDistance);
		thresholdingNeighborhood.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | THUMBNAIL
	  ==============================================================================================*/
	public static void thumbnailByWidth(MarvinImage imageIn, MarvinImage imageOut, int newWidth){
		scale(imageIn, imageOut, newWidth);
	}
	
	public static void thumbnailByHeight(MarvinImage imageIn, MarvinImage imageOut, int newHeight){
		double factor = (double)imageIn.getWidth()/imageIn.getHeight();
		int newWidth = (int)(newHeight * factor);
		scale(imageIn, imageOut, newWidth);
	}
	
	/*==============================================================================================
	  | WATERSHED
	  ==============================================================================================*/
	/**
	 * Watershed transform
	 * @param imageIn		input image
	 * @return
	 */
	public static int[][] watershed(MarvinImage imageIn){
		watershed = checkAndLoadImagePlugin(watershed, "org.marvinproject.image.transform.watershed");
		MarvinAttributes attr = new MarvinAttributes();
		thresholding.process(imageIn, imageIn, attr);
		return (int[][])attr.get("labels");
	}
	
	private static MarvinImagePlugin checkAndLoadImagePlugin(MarvinImagePlugin ref, String pluginCanonicalName){
		// Plug-in already loaded
		if(ref != null){
			return ref;
		}
		return MarvinPluginLoader.loadImagePlugin(pluginCanonicalName);
	}
}

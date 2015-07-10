package marvin;

import java.awt.Color;
import java.util.List;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.image.MarvinSegment;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class MarvinPluginCollection {

	private static MarvinImagePlugin	blackAndWhite,
										brightnessAndContrast,
										boundaryFill,
										colorChannel,
										combineByMask,
										combineByTransparency,
										convolution,
										crop,
										determineSceneBackground,
										emboss,
										flip,
										floodfillSegmentation,
										gaussianBlur,
										grayScale,
										grayScaleQuantization,
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
										statisticalMaximum,
										statisticalMedian,
										statisticalMinimum,
										statisticalMode,
										mergePhotos,
										moravec,
										morphologicalBoundary,
										morphologicalClosing,
										morphologicalDilation,
										morphologicalErosion,
										morphologicalOpening,
										pixelize,
										prewitt,
										roberts,
										rotate,
										scale,
										sobel,
										skew,
										sepia,
										subtract,
										skinColorDetection,
										television,
										tileTexture,
										thresholding;
	
	/*==============================================================================================
	  | BLACK AND WHITE
	  ==============================================================================================*/
	public static void blackAndWhite(MarvinImage image, int level){
		blackAndWhite(image, image, level);
	}
	
	public static void blackAndWhite(MarvinImage imageIn, MarvinImage imageOut, int level){
		blackAndWhite(imageIn, imageOut,  level, MarvinImageMask.NULL_MASK);
	}
	
	public static void blackAndWhite(MarvinImage imageIn, MarvinImage imageOut, int level, MarvinImageMask mask){
		blackAndWhite = checkAndLoadImagePlugin(blackAndWhite, "org.marvinproject.image.color.blackAndWhite");
		blackAndWhite.setAttribute("level", level);
		blackAndWhite.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | BRIGHTNESS AND CONTRAST
	  ==============================================================================================*/
	public static void brightnessAndContrast(MarvinImage image, int brightness, int contrast){
		brightnessAndContrast(image, image, brightness, contrast);
	}
	
	public static void brightnessAndContrast(MarvinImage imageIn, MarvinImage imageOut, int brightness, int contrast){
		brightnessAndContrast(imageIn, imageOut,  brightness, contrast, MarvinImageMask.NULL_MASK);
	}
	
	public static void brightnessAndContrast(MarvinImage imageIn, MarvinImage imageOut, int brightness, int contrast, MarvinImageMask mask){
		brightnessAndContrast = checkAndLoadImagePlugin(brightnessAndContrast, "org.marvinproject.image.color.brightnessAndContrast");
		brightnessAndContrast.setAttribute("brightness", brightness);
		brightnessAndContrast.setAttribute("contrast", contrast);
		brightnessAndContrast.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | COMBINE BY MASK
	  ==============================================================================================*/
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
	public static void convolution(MarvinImage imageIn, MarvinImage imageOut, double[][] matrix){
		convolution = checkAndLoadImagePlugin(convolution, "org.marvinproject.image.convolution");
		convolution.setAttribute("matrix", matrix);
		convolution.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | COLOR CHANNEL
	  ==============================================================================================*/
	public static void colorChannel(MarvinImage image, int red, int green, int blue){
		colorChannel(image, image, red, green, blue);
	}
	
	public static void colorChannel(MarvinImage imageIn, MarvinImage imageOut, int red, int green, int blue){
		colorChannel(imageIn, imageOut, red, green, blue, MarvinImageMask.NULL_MASK);
	}
	
	public static void colorChannel(MarvinImage imageIn, MarvinImage imageOut, int red, int green, int blue, MarvinImageMask mask){
		colorChannel = checkAndLoadImagePlugin(colorChannel, "org.marvinproject.image.color.colorChannel");
		colorChannel.setAttribute("red", red);
		colorChannel.setAttribute("green", green);
		colorChannel.setAttribute("blue", blue);
		colorChannel.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | DETERMINE SCENE BACKGROUND
	  ==============================================================================================*/
	public static void determineSceneBackground(List<MarvinImage> images, MarvinImage imageOut, int threshold){
		determineSceneBackground = checkAndLoadImagePlugin(determineSceneBackground, "org.marvinproject.image.background.determineSceneBackground");
		determineSceneBackground.setAttribute("threshold", threshold);
		determineSceneBackground.process(images, imageOut);	
	}
	
	/*==============================================================================================
	  | EMBOSS
	  ==============================================================================================*/
	public static void emboss(MarvinImage imageIn, MarvinImage imageOut){
		emboss(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void emboss(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		emboss = checkAndLoadImagePlugin(emboss, "org.marvinproject.image.color.emboss");
		emboss.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | FLOODFILL SEGMENTATION
	  ==============================================================================================*/	
	public static MarvinSegment[] floodfillSegmentation(MarvinImage imageIn){
		floodfillSegmentation = checkAndLoadImagePlugin(emboss, "org.marvinproject.image.segmentation.floodfillSegmentation");
		MarvinAttributes output = new MarvinAttributes();
		floodfillSegmentation.process(imageIn, null, output);
		return (MarvinSegment[])output.get("segments");
	}

	/*==============================================================================================
	  | GAUSSIAN BLUR
	  ==============================================================================================*/
	public static void gaussianBlur(MarvinImage imageIn, MarvinImage imageOut, int radius){
		gaussianBlur(imageIn, imageOut, radius, MarvinImageMask.NULL_MASK);
	}
	
	public static void gaussianBlur(MarvinImage imageIn, MarvinImage imageOut, int radius, MarvinImageMask mask){
		gaussianBlur = checkAndLoadImagePlugin(gaussianBlur, "org.marvinproject.image.blur.gaussianBlur");
		gaussianBlur.setAttribute("radius", radius);
		gaussianBlur.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | GRAY SCALE
	  ==============================================================================================*/
	public static void grayScale(MarvinImage image){
		grayScale(image, image);
	}
	
	public static void grayScale(MarvinImage imageIn, MarvinImage imageOut){
		grayScale(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void grayScale(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		grayScale = checkAndLoadImagePlugin(grayScale, "org.marvinproject.image.color.grayScale");
		grayScale.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | HALFTONE CIRCLES
	  ==============================================================================================*/
	public static void halftoneCircles(MarvinImage imageIn, MarvinImage imageOut, int circleWidth, int shift, int circlesDistance, MarvinImageMask mask){
		halftoneCircles = checkAndLoadImagePlugin(halftoneCircles, "org.marvinproject.image.halftone.circles");
		halftoneCircles.setAttribute("circleWidth", circleWidth);
		halftoneCircles.setAttribute("shift", shift);
		halftoneCircles.setAttribute("circlesDistance", circlesDistance);
		halftoneCircles.process(imageIn, imageOut, mask);
	}
	
	public static void halftoneCircles(MarvinImage imageIn, MarvinImage imageOut, int circleWidth, int shift, int circlesDistance){
		halftoneCircles(imageIn, imageOut, circleWidth, shift, circlesDistance, MarvinImageMask.NULL_MASK);
	}
	
	/*==============================================================================================
	  | HALFTONE DITHERING
	  ==============================================================================================*/
	public static void halftoneDithering(MarvinImage imageIn, MarvinImage imageOut){
		halftoneDithering = checkAndLoadImagePlugin(halftoneDithering, "org.marvinproject.image.halftone.dithering");
		halftoneDithering.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | HALFTONE ERROR DIFFUSION
	  ==============================================================================================*/
	public static void halftoneErrorDiffusion(MarvinImage imageIn, MarvinImage imageOut){
		halftoneErrorDiffusion = checkAndLoadImagePlugin(halftoneErrorDiffusion, "org.marvinproject.image.halftone.errorDiffusion");
		halftoneErrorDiffusion.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | HALFTONE RAYLANDERS
	  ==============================================================================================*/
	public static void halftoneRaylanders(MarvinImage imageIn, MarvinImage imageOut){
		halftoneRylanders = checkAndLoadImagePlugin(halftoneRylanders, "org.marvinproject.image.halftone.rylanders");
		halftoneRylanders.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | INVERT COLORS
	  ==============================================================================================*/
	public static void invertColors(MarvinImage image){
		invertColors(image, image);
	}
	
	public static void invertColors(MarvinImage imageIn, MarvinImage imageOut){
		invertColors(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void invertColors(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		invertColors = checkAndLoadImagePlugin(invertColors, "org.marvinproject.image.color.invert");
		invertColors.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | MERGE PHOTOS
	  ==============================================================================================*/
	public static void mergePhotos(List<MarvinImage> images, MarvinImage imageOut, int threshold){
		mergePhotos = checkAndLoadImagePlugin(mergePhotos, "org.marvinproject.image.combine.mergePhotos");
		mergePhotos.setAttribute("threshold", threshold);
		mergePhotos.process(images, imageOut);	
	}
	
	/*==============================================================================================
	  | MORAVEC
	  ==============================================================================================*/
	public static MarvinAttributes moravec(MarvinImage imageIn, MarvinImage imageOut, int matrixSize, int threshold){
		moravec = checkAndLoadImagePlugin(moravec, "org.marvinproject.image.corner.moravec");
		moravec.setAttribute("matrixSize", matrixSize);
		moravec.setAttribute("threshold", threshold);
		MarvinAttributes ret = new MarvinAttributes();
		moravec.process(imageIn, imageOut, ret);
		return ret;
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL BOUNDARY
	  ==============================================================================================*/
	public static void morphologicalBoundary(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalBoundary = checkAndLoadImagePlugin(morphologicalBoundary, "org.marvinproject.image.morphological.boundary");
		morphologicalBoundary.setAttribute("matrix", matrix);
		morphologicalBoundary.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL CLOSING
	  ==============================================================================================*/
	public static void morphologicalClosing(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalClosing = checkAndLoadImagePlugin(morphologicalClosing, "org.marvinproject.image.morphological.closing");
		morphologicalClosing.setAttribute("matrix", matrix);
		morphologicalClosing.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL DILATION
	  ==============================================================================================*/
	public static void morphologicalDilation(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalDilation = checkAndLoadImagePlugin(morphologicalDilation, "org.marvinproject.image.morphological.dilation");
		morphologicalDilation.setAttribute("matrix", matrix);
		morphologicalDilation.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL EROSION
	  ==============================================================================================*/
	public static void morphologicalErosion(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalErosion = checkAndLoadImagePlugin(morphologicalErosion, "org.marvinproject.image.morphological.erosion");
		morphologicalErosion.setAttribute("matrix", matrix);
		morphologicalErosion.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | MORPHOLOCIAL OPENING
	  ==============================================================================================*/
	public static void morphologicalOpening(MarvinImage imageIn, MarvinImage imageOut, boolean[][] matrix){
		morphologicalOpening = checkAndLoadImagePlugin(morphologicalOpening, "org.marvinproject.image.morphological.opening");
		morphologicalOpening.setAttribute("matrix", matrix);
		morphologicalOpening.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | PIXELIZE
	  ==============================================================================================*/
	public static void pixelize(MarvinImage imageIn, MarvinImage imageOut, int squareSide){
		pixelize(imageIn, imageOut, squareSide, MarvinImageMask.NULL_MASK);
	}
	
	public static void pixelize(MarvinImage imageIn, MarvinImage imageOut, int squareSide, MarvinImageMask mask){
		pixelize = checkAndLoadImagePlugin(pixelize, "org.marvinproject.image.blur.pixelize");
		pixelize.setAttribute("squareSide", squareSide);
		pixelize.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | PREWITT
	  ==============================================================================================*/
	public static void prewitt(MarvinImage imageIn, MarvinImage imageOut){
		prewitt.process(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void prewitt(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		prewitt = checkAndLoadImagePlugin(prewitt, "org.marvinproject.image.edge.prewitt");
		prewitt.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | ROBERTS
	  ==============================================================================================*/
	public static void roberts(MarvinImage imageIn, MarvinImage imageOut){
		roberts.process(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void roberts(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		roberts = checkAndLoadImagePlugin(roberts, "org.marvinproject.image.edge.roberts");
		roberts.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SCALE
	  ==============================================================================================*/
	public static void scale(MarvinImage imageIn, MarvinImage imageOut, int width, int height){
		scale = checkAndLoadImagePlugin(roberts, "org.marvinproject.image.transform.scale");
		scale.setAttribute("newWidth", width);
		scale.setAttribute("newHeight", height);
		scale.process(imageIn, imageOut);
	}
	
	public static void scale(MarvinImage imageIn, MarvinImage imageOut, int width){
		int cWidth = imageIn.getWidth();
		int cHeight = imageIn.getHeight();
		double cFactor = (double)cHeight/cWidth;
		int newHeight= (int)(cFactor*width);
		scale(imageIn, imageOut, width, newHeight);
	}
	
	
	/*==============================================================================================
	  | SEPIA
	  ==============================================================================================*/
	public static void sepia(MarvinImage image, int intensity){
		sepia(image, image, intensity);
	}
	
	public static void sepia(MarvinImage imageIn, MarvinImage imageOut, int intensity){
		sepia(imageIn, imageOut, intensity, MarvinImageMask.NULL_MASK);
	}
	
	public static void sepia(MarvinImage imageIn, MarvinImage imageOut, int intensity, MarvinImageMask mask){
		sepia = checkAndLoadImagePlugin(sepia, "org.marvinproject.image.color.sepia");
		sepia.setAttribute("intensity", intensity);
		sepia.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SOBEL
	  ==============================================================================================*/
	public static void sobel(MarvinImage imageIn, MarvinImage imageOut){
		sobel.process(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void sobel(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		sobel = checkAndLoadImagePlugin(sobel, "org.marvinproject.image.edge.sobel");
		sobel.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SKIN COLOR DETECTION
	  ==============================================================================================*/
	public static void skinColorDetection(MarvinImage image){
		skinColorDetection(image, image);
	}
	
	public static void skinColorDetection(MarvinImage imageIn, MarvinImage imageOut){
		skinColorDetection(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void skinColorDetection(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		skinColorDetection = checkAndLoadImagePlugin(skinColorDetection, "org.marvinproject.image.color.skinColorDetection");
		skinColorDetection.process(imageIn, imageOut, mask);
	}
	
	
	/*==============================================================================================
	  | TELEVISION
	  ==============================================================================================*/
	public static void television(MarvinImage image){
		television(image, image);
	}
	
	public static void television(MarvinImage imageIn, MarvinImage imageOut){
		television(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void television(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		television = checkAndLoadImagePlugin(television, "org.marvinproject.image.artistic.television");
		television.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | THRESHOLDING
	  ==============================================================================================*/
	public static void thresholding(MarvinImage image, int threshold){
		thresholding(image, image, threshold);
	}
	
	public static void thresholding(MarvinImage imageIn, MarvinImage imageOut, int threshold){
		thresholding(imageIn, imageOut, threshold, MarvinImageMask.NULL_MASK);
	}
	
	public static void thresholding(MarvinImage imageIn, MarvinImage imageOut, int threshold, MarvinImageMask mask){
		thresholding = checkAndLoadImagePlugin(thresholding, "org.marvinproject.image.color.thresholding");
		thresholding.setAttribute("threshold",threshold);
		thresholding.process(imageIn, imageOut, mask);
	}
	
	
	private static MarvinImagePlugin checkAndLoadImagePlugin(MarvinImagePlugin ref, String pluginCanonicalName){
		// Plug-in already loaded
		if(ref != null){
			return ref;
		}
		return MarvinPluginLoader.loadImagePlugin(pluginCanonicalName);
	}
	
}

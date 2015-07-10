package org.marvinproject.image.combine.mergePhotos;

import java.util.List;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class MergePhotos extends MarvinAbstractImagePlugin{
	
	private MarvinImagePlugin	background;
	private int					threshold;
	
	@Override
	public void load() {
		background = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.background.determineSceneBackground");
		setAttribute("threshold", 30);
	}
	
	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
	
	@Override
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attrOut,
		MarvinImageMask mask,
		boolean preview
	) {
		
	}
	
	@Override
	public void process
	(
		List<MarvinImage> images,
		MarvinImage imageOut
	){
		if(!images.isEmpty()){
			threshold = (Integer)getAttribute("threshold");
			background.setAttribute("threshold", threshold);
			MarvinImage backgroundImage = images.get(0).clone();
			background.process(images, backgroundImage);
			mergePhotos(images, imageOut, backgroundImage, threshold);
		}
	}
	
	private void mergePhotos(List<MarvinImage> images, MarvinImage imageOut, MarvinImage background, int threshold){
		for(MarvinImage img:images){
			mergePhotos(img, imageOut, background, threshold);
		}
	}
	
	private void mergePhotos(MarvinImage imageA, MarvinImage imageB, MarvinImage imageBackground, int threshold){
			
		int rA, gA, bA, rB, gB, bB;
		for(int y=0; y<imageA.getHeight(); y++){
			for(int x=0; x<imageA.getWidth(); x++){
				
				rA = imageA.getIntComponent0(x, y);
				gA = imageA.getIntComponent1(x, y);
				bA = imageA.getIntComponent2(x, y);
				rB = imageBackground.getIntComponent0(x, y);
				gB = imageBackground.getIntComponent1(x, y);
				bB = imageBackground.getIntComponent2(x, y);
				
				if
				(
					Math.abs(rA-rB) > threshold || 
					Math.abs(gA-gB) > threshold ||
					Math.abs(bA-bB) > threshold
				){
					
					imageB.setIntColor(x, y, 255, rA, gA, bA);
				}
			}
		}
	}
	
}

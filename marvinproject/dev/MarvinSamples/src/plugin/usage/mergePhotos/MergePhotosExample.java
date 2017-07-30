package plugin.usage.mergePhotos;

import java.util.ArrayList;
import java.util.List;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;

import org.marvinproject.image.combine.mergePhotos.MergePhotos;

public class MergePhotosExample {

	public MergePhotosExample(){
		
		// 1. load images 01.jpg, 02.jpg, ..., 05.jpg into a List
		List<MarvinImage> images = new ArrayList<MarvinImage>();
		for(int i=1; i<=5; i++){
			images.add(MarvinImageIO.loadImage("./res/0"+i+".jpg"));
		}
		
		// 2. Load plug-in and process the image
		MarvinImagePlugin merge = new MergePhotos();
		merge.load();
		merge.setAttribute("threshold", 38);
		
		// 3. Process the image list and save the output
		MarvinImage output = images.get(0).clone();
		merge.process(images, output);
		MarvinImageIO.saveImage(output, "./res/merge_output.jpg");
	}
	
	public static void main(String[] args) {
		new MergePhotosExample();
		System.exit(0);
	}
}
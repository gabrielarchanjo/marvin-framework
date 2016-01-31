package org.marvinproject.image.transform.watershed;

/**
Marvin Project <2007-2016>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

//package org.marvinproject.image.corner.WaterShed;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * 
 * @author Martin Mihalyi
 */
public class Watershed extends MarvinAbstractImagePlugin {

	private MarvinImagePlugin gray;
	private List<List<WatershedPixel>> HeightMap;
	private WatershedPixel[][] Image;
	private int width, height;
	private List<WatershedPixel> query;
	private WatershedPixel dummyPixel;
	
	@Override
	public void load() {
		gray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale");

		HeightMap = new ArrayList<List<WatershedPixel>>(256);
		//initialize HeightMap
		for(int i = 0; i<256; i++)
		{
			HeightMap.add(new ArrayList<WatershedPixel>());
		}
		
		query = new LinkedList();
		
		dummyPixel = new WatershedPixel(0,0);
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
		boolean previewMode
	) {
		//pixel's lowest distance to the watershed pixel
		int CurrentDistance;
		int CurrentLabel = 0;
		//convert to grayscale
		MarvinImage tempImage = new MarvinImage(imageIn.getWidth(), imageIn.getHeight());
		gray.process(imageIn, tempImage);
		
		//temporary variables for faster processing
		width = tempImage.getWidth();
		height = tempImage.getHeight();
		
		//initailize image to easier access the pixels
		Image = new WatershedPixel[width][height];
		
		//array for labeled pixels
		int[][] labels = new int[width][height];
		
		//1. step build up the HeightMap
		//ordering the pixels by values	(LookUpTable)	
	    //in every line
	      for (int i = 0; i < width; i += 1)
	      {
	    	  //in every row
	         for (int j = 0; j < height; j += 1)
	         {
	        	 //create new watershed pixel 
	        	 WatershedPixel pixel = new WatershedPixel(i, j);
	        	 
	        	 //add the reference to the image array to the easiest access in case of neighbours search
	        	 Image[i][j] = pixel;
	        	 
	        	 //add it to the heightMap
	        	 HeightMap.get(tempImage.getIntComponent0(i, j)).add(pixel);
	         }
	         
	      }
	      
	      //2. step processing the heightmap and puting pixels in a query
	      //processing the pixels by the height
	      for (int h = 0; h < HeightMap.size(); h++)
	      {
	    	  //iterating through the pixels in the given h height
	    	  for(WatershedPixel wpixel : HeightMap.get(h))
	    	  {
	    		  //set label MASK to pixel
	    		  wpixel.setLabel(-2);
	    		  
	    		  //get all neighbour of the given pixel
	    		  List<WatershedPixel> neighbourPixels = Neighbours(wpixel);
	    		  
	    		  //iterating through the neighbour pixels searching for already labeled pixels
	    		  for(WatershedPixel neighbourPixel : neighbourPixels)
	    		  {
	    			  //check if neighbourpixel is allready labelled
	    			  if(neighbourPixel.getLabel() >= 0)
	    			  {
	    				  //at least one neighbouring pixel is watershed pixel, so the distance cannot be more than 1
	    				  wpixel.setDistance(1);
	    				  
	    				  //add to query
	    				  query.add(wpixel);
	    				  
	    				  //labeled pixel found, more iteration is not needed
	    				  break;
	    			  }
	    			  
	    		  }
	    	  }
	    	  
			  //add dummyPixel to query
			  query.add(dummyPixel);
	    	  //increase current distance
			  CurrentDistance = 1;
			  
			  //process the remainging element in the query if the query is not empty
			  while(!query.isEmpty())
			  {
				  //get the first element of query
				  WatershedPixel nextProccessPixel = query.remove(0);
				  
				  if(query.isEmpty())
				  {
					  break;
				  }
				  //check if all pixel with the same distance to the watershed pixel has been processed
				  else if(nextProccessPixel.equals(dummyPixel))
				  {
			    	  //increase current distance
					  CurrentDistance++;
					  
					  //put dummyPixel back to query
					  query.add(dummyPixel);
					  
					  //get the next pixel
					  nextProccessPixel = query.remove(0);
				  }
				  
	    		  //get all neighbour of the given pixel
	    		  List<WatershedPixel> neighbourPixels = Neighbours(nextProccessPixel);
	    		  
	    		  //iterating through the neighbour pixels searching for already labeled pixels
	    		  for(WatershedPixel neighbourPixel : neighbourPixels)
	    		  {
	    			  //check if the neighbour pixel is:
	    			  // - closer to a watershed pixer than the current distance AND
	    			  //-	neighbour pixel is allready labelled
	    			  if((neighbourPixel.getDistance() <= CurrentDistance) &&  (neighbourPixel.getLabel() >= 0) )
	    			  {
	    					if(neighbourPixel.getLabel() > 0)
	    					{
	    					    if(nextProccessPixel.getLabel() == -2)
	    					    {
	    				
	    					    	nextProccessPixel.setLabel(neighbourPixel.getLabel());
	    					    }
	    					    else if(nextProccessPixel.getLabel() != neighbourPixel.getLabel())
	    					    {
	  	    					  	nextProccessPixel.setLabel(0);
	    					    }
	    					} // end if lab>0
	    					else if(nextProccessPixel.getLabel() == -2)
	    					{
	    						nextProccessPixel.setLabel(0);
	    					}
	    				  
	    			  }
	    			  //the neighbour pixel has the same height as the currenct pixel (plain)
	    			  else if((neighbourPixel.getDistance() == 0) &&  (neighbourPixel.getLabel() == -2))
	    			  {
	    				  neighbourPixel.setDistance(CurrentDistance + 1);
	    				  //add to query
	    				  query.add(neighbourPixel);
	    			  }
	    			  
	    		  }
				  
			  }
			  
			  
			  //detect the unprocessed pixels at the given height
	    	  for(WatershedPixel wpixel : HeightMap.get(h))
	    	  {
	    		  //set the distance back to inital value
	    		  wpixel.setDistance(0);
	    		  
	    		  if(wpixel.getLabel() == -2)
	    		  {
	    			  CurrentLabel++;
	    			  wpixel.setLabel(CurrentLabel);
	    			  query.add(wpixel);
	    			  
	    			  //process the remainging element in the query if the query is not empty
	    			  while(!query.isEmpty())
	    			  {
	    				  //get the next element of query
	    				  WatershedPixel nextPixel = query.remove(0);
	    				  
	    	    		  //get all neighbour of the given pixel
	    	    		  List<WatershedPixel> neighbourPixels = Neighbours(nextPixel);
	    	    		  
	    	    		  //iterating through the neighbour pixels searching for MASK(-2) labelled pixels
	    	    		  for(WatershedPixel neighbourPixel : neighbourPixels)
	    	    		  {
	    	    			  if(neighbourPixel.getLabel() == -2)
	    	    			  {
	    	    				  neighbourPixel.setLabel(CurrentLabel);
	    	    				  query.add(neighbourPixel);
	    	    			  } 
	    	    		  } 
	    			  } 
	    		  }
	    	  }  
	      }
	      //3. step create an array to display the generated labels
	      	for(int i = 0; i < width; i++)
	      	{
	      		for(int j = 0; j < height; j++)
	      		{
	      			//copy to the output array
	      			labels[i][j] = Image[i][j].getLabel();
	      		}
	      		
	      	}
	      	
			if(attrOut != null){
				//return the pixel labels
				attrOut.set("imageLabels", labels);
			}
	      
	}
	
	private List<WatershedPixel> Neighbours(WatershedPixel p)
	{
		//get the position of pixels
		int x = p.getX();
		int y = p.getY();
		
		//temp array for the neighbours
		List<WatershedPixel> NPixels = new ArrayList<WatershedPixel>(4);
		
		//add every vertical/horrizontal neighbours
		if(x > 0)
			NPixels.add(Image[x-1][y]);
		if(x < width-1)
			NPixels.add(Image[x+1][y]);
		if (y > 0)
			NPixels.add(Image[x][y-1]);
		if(y < height -1)
			NPixels.add(Image[x][y+1]);

		return NPixels;
	}
}

//class for the watershed pixels
class WatershedPixel
{
	//position of the pixel in the image
	private int x, y;
	
	//indicating the status of the pixel
	private int label;
	
	//distance from the minimum
	private int distance;
	
	
	//getter for x position
	public int getX() {
		return x;
	}
	
	//getter for y position	
	public int getY() {
		return y;
	}

	//getter/setter of label
	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}
	
	//getter/setter of distance
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	//contructor
	public WatershedPixel(int x, int y)
	{
		//set the position of pixel
		this.x = x;
		this.y = y;
		
		//set to INIT value
		this.setLabel(-1);
	}
}

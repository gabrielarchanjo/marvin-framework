package net.marvinproject.framework.machinelearning.decisiontree;

import java.io.File;
import java.net.URI;
import java.util.Map;

import net.marvinproject.framework.util.MarvinFileUtils;
import net.marvinproject.framework.util.MarvinStringParser;

public class DecisionTree {

	private DecisionTreeNode root;
	
	public DecisionTree(){}
	
	public void parseWeka(String path){
		String s = null;
		try{
			s = MarvinFileUtils.readStringFile(path, "UTF-8");
		}
		catch(Exception e){	e.printStackTrace();}
		
		int idx = s.indexOf("=== Classifier model");
		idx = jumpText(s, "\n", idx, 5);
		
		int idx2 = s.indexOf("Number of Leaves");
		
		String strTree = s.substring(idx, idx2);
		
		parseTree(strTree);
	}
	
	private void parseTree(String tree){
		root = DecisionTreeNode.createRootNode();
		MarvinStringParser parser = new MarvinStringParser(tree);
		parseNode(root, parser, 0);
	}
	
	public String classify(Map<String, Object> sample){
		return root.interpret(sample);
	}
	
	private void parseNode(DecisionTreeNode parent, MarvinStringParser parser, int level){
	
		parser.jumpOver(new char[]{' ', '\n'});
		int currLvl = parser.countTextBefore("|", "\n");
		
		if(currLvl < level){
			int lvls = level-currLvl;
			for(int i=0; i<lvls; i++)
				parent = parent.getParent();
		}
		parser.jumpOver(new char[]{'|', ' ', '\n'});
		
		boolean hasColon = parser.hasBefore(":", "\n");
		String attr = parser.getNext(new char[]{' ', '\n', ':'});
		parser.incPosition(1);
		String cond = parser.getNext(new char[]{' ', '\n', ':'});
		parser.incPosition(1);
		String val = parser.getNext(new char[]{'\n', ':'});
		
		if(cond == null || cond.equals("")){
			return;
		}
	
		Object v;
		if(isStringNumeric(val)){
			v = Double.parseDouble(val.toString());
		} else{
			v = val.toString();
		}
		
		
		DecisionTreeNode node = new DecisionTreeNode(attr, cond, v);
		parent.addChild(node);
		
		if(hasColon){
			String res = parser.getNextBetween(":", "(");
			parser.gotoNextLine();
			node.setResult(res.trim());
			parseNode(parent, parser, currLvl);
		} else{
			parseNode(node, parser, currLvl+1);
		}
	}
	
	private int jumpText(String content, String text, int currentIndex, int times){
		
		int idx = currentIndex;
		for(int i=0; i<times && idx != -1; i++){
			idx = content.indexOf(text, idx+1);
		}
		return idx;
	}
	
	private static boolean isStringNumeric(String str)  {  
		try{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe){  
			return false;  
		}  
		return true;  
	}
	
	public static void main(String[] args) {
		File file = new File(URI.create("file:///onex/mlmodels/ingredient_classification_2.txt"));
		DecisionTree tree = new DecisionTree();
		tree.parseWeka(file.getAbsolutePath());
	}
}

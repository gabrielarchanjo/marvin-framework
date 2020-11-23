package net.marvinproject.framework.machinelearning.decisiontree;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTreeNode {

	public enum CONDITION{GT, GTE, LT, LTE, EQ, NEQ};
	
	private String 					attr;
	private CONDITION				condition;
	private Object					value;
	private String					result;
	private boolean					root;

	private DecisionTreeNode		parent;
	private List<DecisionTreeNode>	childs;
	
	public DecisionTreeNode(String attr, String condition, Object value){
		this.attr = attr;
		this.condition = getCondition(condition);
		this.value = value;
		this.childs = new ArrayList<DecisionTreeNode>();
	}
	
	private DecisionTreeNode(){
		this(null, null, null);
		this.root = true;
	}
	
	public static DecisionTreeNode createRootNode(){
		return new DecisionTreeNode();
	}
	
	public DecisionTreeNode getParent(){
		return parent;
	}
	
	private CONDITION getCondition(String cond){
		if("<".equals(cond))	return CONDITION.LT;
		if("<=".equals(cond))	return CONDITION.LTE;
		if(">".equals(cond))	return CONDITION.GT;
		if(">=".equals(cond))	return CONDITION.GTE;
		if("=".equals(cond))	return CONDITION.EQ;
		if("!=".equals(cond))	return CONDITION.NEQ;
		return null;
	}
	
	public void addChild(DecisionTreeNode node){
		childs.add(node);
		node.parent = this;
	}
	
	public void setResult(String result){
		this.result = result;
	}
	
	public String getResult(){
		return result;
	}
	
	public String toString(){
		return toString(0);
	}
	
	public String toString(int currLvl){
		
		String ret = null;
		
		if(root){
			ret = "[ROOT]\n";
		} else{
			ret = pp("---", currLvl) + "Node("+attr+","+condition.toString()+","+value+")\n";
		}
		
		for(DecisionTreeNode n:childs){
			ret += n.toString(currLvl+1);
		}
		return ret;
	}
	
	private String pp(String text, int times){
		String ret="";
		for(int i=0; i<times; i++){
			ret += text;
		}
		return ret;
	}

	public String interpret(Map<String, Object> map) {
		if(attr != null){
			if(eval(attr, map)){
				if(result != null){
					return result;
				} else{
					return interpretChilds(map);
				}
			}
			else{
				return null;
			}
		}
		else{
			return interpretChilds(map);
		}
	}
	
	public String interpretChilds(Map<String, Object> map){
		for(DecisionTreeNode n:childs){
			String ret = n.interpret(map);
			if(ret != null){
				return ret;
			}
		}
		return null;
	}
	
	public boolean eval(String attr, Map<String, Object> map){
		
		Object sValue = map.get(attr);
		
		if(sValue == null){
			throw new RuntimeException(attr+" is null");
		}
		
		if(isNumber(sValue)){
			return evalNumeric(attr, sValue);
		} else{
			return evalText(attr, sValue);
		}
	}
	
	private boolean evalNumeric(String attr, Object sValue){
		
		Double n1 = getNumeric(sValue);
		Double n2 = (Double)value;
		
		switch(condition){
			case EQ:		return n1 == n2;
			case GTE:		return n1 >= n2;
			case GT: 		return n1 > n2;
			case LT:		return n1 < n2;
			case LTE:		return n1 <= n2;
			case NEQ:		return n1 != n2;
		}
		return false;
	}
	
	private boolean evalText(String attr, Object sValue){
		String s1 = sValue.toString().trim();
		String s2 = ((String)value).trim();
		
		return s1.equals(s2);
	}
	
	private Double getNumeric(Object sValue){
		if(sValue.getClass() == Double.class){
			return ((Double)sValue);
		}
		if(sValue.getClass() == Integer.class){
			return ((Integer)sValue).doubleValue();
		}
		if(sValue.getClass() == Float.class){
			return ((Float)sValue).doubleValue();
		}
		return null;
	}
		
	private boolean isNumber(Object obj){
		if
		(
			obj instanceof Integer ||
			obj instanceof Double ||
			obj instanceof Float
		){
			return true;
		}
		return false;
	}
}

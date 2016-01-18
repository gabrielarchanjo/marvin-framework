package marvin.util;

public class MarvinStringParser {

	private int		currPosition;
	private String 	data;
	
	public MarvinStringParser(String s){
		this.data = s;
		this.currPosition = 0;
	}
	
	public int getCurrentPosition(){
		return currPosition;
	}
	
	public void incPosition(int inc){
		currPosition += inc;
	}
	
	public String getNext(char[] suffixes){
		int idx = indexOf(suffixes, data, currPosition);
		if(idx != -1){
			String ret = data.substring(currPosition, idx).trim();
			currPosition = idx;
			return ret;
		}
		return null;
	}
	
	public String getNextBetween(String prefix, String suffix){
		int idx1 = data.indexOf(prefix, currPosition);
		int idx2 = data.indexOf(suffix, currPosition);
		
		if(idx1 != -1 && idx2 != -1){
			currPosition = idx2;
			return data.substring(idx1+prefix.length(), idx2);
		}
		return null;
	}
	
	public boolean hasBefore(String text, String suffix){
		int idx = data.indexOf(suffix, currPosition);
		if(idx != -1){
			String sub = data.substring(currPosition, idx);
			return (sub.indexOf(text) != -1);
		}
		return false;
	}
	
	public String getCurrentLine(){
		int idx = data.indexOf('\n', currPosition);
		if(idx != -1){
			return data.substring(currPosition, idx);
		}
		return null;
	}
	public boolean gotoNextLine(){
		int idx = data.indexOf("\n", currPosition);
		if(idx != -1){
			currPosition = idx + 1;
			return true;
		}
		return false;
	}
	
	public int countTextBefore(String text, String suffix){
		int idx = data.indexOf(suffix, currPosition);
		if(idx != -1){
			
			String sub = data.substring(currPosition, idx);
			sub = sub.replace("#", "");
			sub = sub.replace(text, "#");
			return count('#', sub);
		}
		return -1;
	}
	
	public void jumpOver(char[] arr){
		for(int i=currPosition; i<data.length(); i++){
			if(!inArr(data.charAt(i), arr)){
				currPosition = i;
				break;
			}
		}
	}
	
	private boolean inArr(char achar, char[] arr){
		for(char c:arr){
			if(c == achar){
				return true;
			}
		}
		return false;
	}
	
	private int indexOf(char[] arr, String content, int pos){
		for(int i=pos; i<content.length(); i++){
			if(inArr(content.charAt(i), arr)){
				return i;
			}
		}
		return -1;
	}
	
	private int count(char c, String s){
		int total=0;
		for(int i=0; i<s.length(); i++){
			if(s.charAt(i) == c){
				total++;
			}
		}
		return total;
	}
}

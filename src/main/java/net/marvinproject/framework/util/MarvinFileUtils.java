package net.marvinproject.framework.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MarvinFileUtils {
	
	public static String readStringFile(String path, String encoding)  throws IOException {
		Charset enc = Charset.forName(encoding);
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, enc);
	}
	
	public static void writeStringFile(String content, String path) throws FileNotFoundException{
		PrintWriter out = new PrintWriter(path);
		out.print(content);
		out.close();
	}
}

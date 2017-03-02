package cn.com.jna.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
 
/** Simple example of native library declaration and usage. */
public class HelloWorld {
 
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
            Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),
                               CLibrary.class);
   
        void printf(String format, Object... args);
    }
 
    public static void main(String[] args) {
        /*CLibrary.INSTANCE.printf("Hello, World/n");
        for (int i=0;i < args.length;i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s/n", i, args[i]);
        }*/
    	String keywords = "ÎÒ´òµÄ¾ÍÊÇÂÒ";
    	Pattern pattern = Pattern.compile("[\\p{P}0-9a-zA-Z\u4e00-\u9fa5]*");
	    Matcher isNormal = pattern.matcher(keywords);
	    if(isNormal.matches()){
	    	System.out.println("不是乱码："+keywords);
	    }else{
	    	System.out.println("是乱码："+keywords);
	    }
    }
}
 
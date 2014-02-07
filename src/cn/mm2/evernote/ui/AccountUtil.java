package cn.mm2.evernote.ui;

import org.scribe.builder.api.EvernoteApi;

public class AccountUtil {
	
	public static final boolean ENV_TEST = true;

	public static String API_KEY = "xinmeng2011";
	public static String API_SECRET = "da15a26172f0f1e2";
	
	public static Class getApiClass(){
		if(ENV_TEST){
			return EvernoteApi.Sandbox.class;
		}else{
			return EvernoteApi.class;
		}
	}
	
	public static String getNoteStoreUrl(){
		if(ENV_TEST){
			return "https://sandbox.evernote.com/edam/user";
		}else{
			return "https://www.evernote.com/edam/user";
		}
	}
}

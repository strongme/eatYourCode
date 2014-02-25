package cn.mm2.evernote.ui;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.scribe.builder.api.EvernoteApi;
import org.scribe.builder.api.YinXiangApi;

import com.evernote.edam.userstore.BootstrapInfo;
import com.evernote.edam.userstore.BootstrapProfile;
import com.evernote.edam.userstore.UserStore;
import com.evernote.thrift.TException;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;

public class AccountUtil {
	/*
	 * 正式环境  huangyu2010@gmail.com qinG6979
	 */
	public static final boolean ENV_TEST = false;
	public static final boolean YINXIANG = true;

	public static String API_KEY = "xinmeng2011";
	public static String API_SECRET = "da15a26172f0f1e2";
	
	public static Class getEvernoteApiClass(){
		if(ENV_TEST){
			return EvernoteApi.Sandbox.class;
		}else{
			return EvernoteApi.class;
		}
	}
	
	public static Class getYinXiangApiClass(){
		if(ENV_TEST){
			return YinXiangApi.Sandbox.class;
		}else{
			return YinXiangApi.class;
		}
	}
	
	public static Class getApiClass(){
		if(YINXIANG){
			return getYinXiangApiClass();
		}else{
			return getEvernoteApiClass();
		}
	}
	
	public static String getNoteStoreUrl(){
		if(YINXIANG){
			return "https://app.yinxiang.com/edam/user";
		}else{		
			if(ENV_TEST){
				return "https://sandbox.evernote.com/edam/user";
			}else{
				return "https://www.evernote.com/edam/user";
			}
		}
	}
	
	private static String getLocale(){
		Locale locale = Locale.getDefault();
		return locale.getLanguage()+ "_" + locale.getCountry();
	}
	
	public static void getBoostUrl(){
		THttpClient userStoreTrans = null;
		try {
			userStoreTrans = new THttpClient(getNoteStoreUrl());
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		//userStoreTrans.setCustomHeader("User-Agent", userAgent);  
		TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans); 
		UserStore.Client tmpStore = new UserStore.Client(userStoreProt, userStoreProt);
		BootstrapInfo bootstrap = null;
		try {
			bootstrap = tmpStore.getBootstrapInfo(getLocale());
			List<BootstrapProfile> pros = bootstrap.getProfiles();
			int count = pros.size();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

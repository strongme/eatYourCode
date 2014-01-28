package cn.mm2.evernote.actions;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.evernote.edam.type.Notebook;

import cn.mm2.evernote.ui.BrowserExample;
import cn.mm2.evernote.ui.EvernoteInvoke;
import cn.mm2.evernote.ui.TokenStoreUtil;



/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	private OAuthService mService;
	private Token mRequestToken;
	private String key;
	

	/**
	 * The constructor.
	 */
	public SampleAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		String token = TokenStoreUtil.getToken();
		if(token != null && token.length() > 0){
			saveNote(token);
		}else{
			initOauth();	
		}
		getCurrentCode();
	}
	
	private void saveNote(String token){
		String title = getCurrentTitle();
		if(title.length() == 0){
			return;
		}
		String code = getCurrentCode();
		if(code.length() == 0){
			return;
		}
		EvernoteInvoke.setToken(token);
		EvernoteInvoke.getSingle().saveNote(title, code);
	}
	
	private void initOauth(){
		     mService = new ServiceBuilder()
	        .provider(EvernoteApi.Sandbox.class)
	        .apiKey("xinmeng2011")
	        .apiSecret("da15a26172f0f1e2").callback("www.baidu.com")
	        .build();
			 
			 mRequestToken = mService.getRequestToken();
			 key =null;
			 String authUrl = mService.getAuthorizationUrl(mRequestToken);
			 
			 try {
				//openWebpage(authUrl);
				 uiTest(authUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
//			 Verifier v = new Verifier("verifier you got from the user");
//			 Token accessToken = service.getAccessToken(requestToken, v); // the requestToken you had from step 2
//			 
//				MessageDialog.openInformation(
//						window.getShell(),
//						"Evernote",
//						accessToken.getToken());
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
	
	private void openWebpage(String url){
		IWorkbenchBrowserSupport browserSupport=PlatformUI.getWorkbench().getBrowserSupport();
		//browserSupport.
	    try {
			IWebBrowser browser=browserSupport.createBrowser("myid");
			browser.openURL(new URL(url));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 *         final Display display=Display.getDefault();
        final Shell shell=new Shell(); //shell是程序的主窗口
        shell.setSize(327,253);//设置主窗口的大小
        shell.setText("HelloWorld");//设置主窗口的标题
        Browser web = new Browser(shell, SWT.NONE);
        //-------创建窗口中的其他界面组件----------
        //.......
        //-------END---------
        shell.layout();//应用界面布局
        shell.open();//打开shell主窗口
        while (!shell.isDisposed()){//如果shell主窗口没有关闭，则一直循环
            if(!display.readAndDispatch()) //如果display不忙，就让display处于休眠状态
                display.sleep();
        }
        display.dispose(); //释放display资源
	 */
	private void uiTest(String url){        
		Display display = Display.getDefault();
	    Shell shell = new Shell(display);
	    shell.setLayout(new FillLayout());
	    shell.setText("Browser example");
	    BrowserExample instance = new BrowserExample(shell);
	    shell.open();
	    instance.goToUrl(url,mUrlListener);
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    instance.dispose();
	   // display.dispose();
	}
	
	private LocationListener mUrlListener = new LocationListener() {
		
		@Override
		public void changing(LocationEvent event) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void changed(LocationEvent event) {
			// TODO Auto-generated method stub
			String nowUrl = event.location;
			// oauth_verifier
			String tag = "oauth_verifier";
			if(nowUrl.contains(tag)&& key==null){
				int pos= nowUrl.indexOf(tag);
				int posbegin = pos + tag.length() +1;
				int posend = nowUrl.length();
				if(nowUrl.lastIndexOf("#") > posbegin){
					posend= nowUrl.lastIndexOf("#");
				}
                key = nowUrl.substring(posbegin,posend);
    			goToVerifier(key);
			}
		}
	};
	
	private void goToVerifier(String verifier){
		 Verifier v = new Verifier(verifier);
		 Token accessToken = mService.getAccessToken(mRequestToken, v); // the requestToken you had from step 2
		 
			MessageDialog.openInformation(
					window.getShell(),
					"Evernote",
					accessToken.getToken());
		 TokenStoreUtil.saveToken(accessToken.getToken());
		 EvernoteInvoke.setToken(accessToken.getToken());
		 EvernoteInvoke.getSingle().saveNote("123", "mm", null);
	}
	
	private  String getCurrentCode(){
		//取得工作台
		IWorkbench workbench = PlatformUI.getWorkbench();
		//取得工作台窗口
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		//取得工作台页面
		IWorkbenchPage page = window.getActivePage();
		//取得当前处于活动状态的编辑器窗口
		IEditorPart part = page.getActiveEditor();
		IEditorInput ei=  part.getEditorInput(); 
		IFileEditorInput ifile = (IFileEditorInput) ei;
		String content="";
		if(ifile != null){
			IFile file = 	ifile.getFile();
			IFileInfo info = null;
			try {
				info = org.eclipse.core.filesystem.EFS.getStore(ifile.getFile().getLocationURI()).fetchInfo();
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return "";
			}
			byte[] b=new byte[(int) info.getLength()];
			try {
				file.getContents().read(b);
				content=new String(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return content;
	}
	
	private String getCurrentTitle(){
		try{
			//取得工作台
			IWorkbench workbench = PlatformUI.getWorkbench();
			//取得工作台窗口
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			//取得工作台页面
			IWorkbenchPage page = window.getActivePage();
			//取得当前处于活动状态的编辑器窗口
			IEditorPart part = page.getActiveEditor();
			String title  = part.getTitle();
			return title;
		}catch(Exception e){
			return "";
		}
	}
	
}
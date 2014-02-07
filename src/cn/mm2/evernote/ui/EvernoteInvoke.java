package cn.mm2.evernote.ui;

import java.util.ArrayList;
import java.util.List;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.userstore.UserStore;
import com.evernote.thrift.TException;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;

public class EvernoteInvoke {
	
	private static String mToken = "";
	private NoteStore.Client mNoteStore;
	private UserStore.Client mUserStore;
	private static EvernoteInvoke mSingle;
	private static final String NOTEBOOK_NAME= "Elephant eat your code";
	public static void setToken(String token){
		if(mToken.length() == 0){
			mToken = token;
		}
	}
	
	static public EvernoteInvoke getSingle(){
		if(mSingle != null){
			return mSingle;
		}else{
			mSingle = new EvernoteInvoke(mToken);
		}
		return mSingle;
	}
	
	private EvernoteInvoke(String token){	  
		THttpClient userStoreTrans = null;
		try {
			userStoreTrans = new THttpClient(AccountUtil.getNoteStoreUrl());
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		//userStoreTrans.setCustomHeader("User-Agent", userAgent);  
		TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans); 
		mUserStore = new UserStore.Client(userStoreProt, userStoreProt);
		String noteStoreUrl = "";
		try {
			noteStoreUrl = mUserStore.getNoteStoreUrl(mToken);
			THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
			TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
			mNoteStore = new NoteStore.Client(noteStoreProt, noteStoreProt);
			Notebook book = mNoteStore.getDefaultNotebook(mToken);
		} catch (EDAMUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EDAMSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveNote(String noteTitle,  String noteBody, Notebook parentNotebook){
		noteBody = noteBody.replace("\r\n", "</div><div>");
		noteBody = "<div>" + noteBody +"</div>";
		String nBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		  nBody += "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">";
		  nBody += "<en-note>" + noteBody + "</en-note>";
		 
		  // Create note object
		  Note ourNote = new Note();
		  ourNote.setTitle(noteTitle);
		  ourNote.setContent(nBody);
		 
		  // parentNotebook is optional; if omitted, default notebook is used
		  if (parentNotebook != null && parentNotebook.isSetGuid()) {
		    ourNote.setNotebookGuid(parentNotebook.getGuid());
		  }
		 
		  // Attempt to create note in Evernote account
		  Note note = null;
		  try {
		    note = mNoteStore.createNote(mToken,ourNote);
		  } catch (EDAMUserException edue) {
		    // Something was wrong with the note data
		    // See EDAMErrorCode enumeration for error code explanation
		    // http://dev.evernote.com/documentation/reference/Errors.html#Enum_EDAMErrorCode
		    System.out.println("EDAMUserException: " + edue);
		  } catch (EDAMNotFoundException ednfe) {
		    // Parent Notebook GUID doesn't correspond to an actual notebook
		    System.out.println("EDAMNotFoundException: Invalid parent notebook GUID");
		  } catch (Exception e) {
		    // Other unexpected exceptions
		    e.printStackTrace();
		  }
		 
		  // Return created note object
		  return ;
	}
	
	private Notebook getEYCNotebook(){
		Notebook nb = new Notebook();
		Notebook nbRemote = null;
		nb.setName(NOTEBOOK_NAME);
		try {
			nbRemote = mNoteStore.createNotebook(mToken, nb);
		} catch (EDAMUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EDAMSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nbRemote;
	}
	
	private Notebook findEYCNotebook(){
		List<Notebook> nbs;
		try {
			nbs = mNoteStore.listNotebooks(mToken);
			if(nbs != null){
				for (int i = 0; i < nbs.size(); i++) {
					Notebook item = nbs.get(i);
					if(item.getName().equals(NOTEBOOK_NAME)){
						return item;
					}
				}
			}
		} catch (EDAMUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EDAMSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void saveNote(String title, String note){
		Notebook nb = getEYCNotebook();
		if(nb == null){
			nb = findEYCNotebook();
		}
		if(nb == null){
			return;
		}
		saveNote(title, note, nb);
	}
}

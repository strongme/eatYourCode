package cn.mm2.evernote.ui;

import java.text.MessageFormat;
import java.util.MissingResourceException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class BrowserExample {
  //static ResourceBundle resourceBundle =
  // ResourceBundle.getBundle("examples_browser");
  int index;

  boolean busy;

  Image images[];

  Text location;

  Browser browser;

  static final String[] imageLocations = { "eclipse01.bmp", "eclipse02.bmp",
      "eclipse03.bmp", "eclipse04.bmp", "eclipse05.bmp", "eclipse06.bmp",
      "eclipse07.bmp", "eclipse08.bmp", "eclipse09.bmp", "eclipse10.bmp",
      "eclipse11.bmp", "eclipse12.bmp", };

  static final String iconLocation = "java2s.gif";

  /**
   * Creates an instance of a ControlExample embedded inside the supplied
   * parent Composite.
   * 
   * @param parent
   *            the container of the example
   */
  public BrowserExample(Composite parent) {

    final Display display = parent.getDisplay();
    FormLayout layout = new FormLayout();
    parent.setLayout(layout);
    parent.setSize(480, 800);
//    ToolBar toolbar = new ToolBar(parent, SWT.NONE);
//    final ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
//    itemBack.setText(getResourceString("Back"));
//    final ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
//    itemForward.setText(getResourceString("Forward"));
//    final ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
//    itemStop.setText(getResourceString("Stop"));
//    final ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
//    itemRefresh.setText(getResourceString("Refresh"));
//    final ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
//    itemGo.setText(getResourceString("Go"));

    location = new Text(parent, SWT.BORDER);

    //images = new Image[] { new Image(display, "java2s.gif") };

    final Canvas canvas = new Canvas(parent, SWT.NO_BACKGROUND);
 //   final Rectangle rect = images[0].getBounds();
    canvas.addListener(SWT.Paint, new Listener() {
      public void handleEvent(Event e) {
        Point pt = canvas.getSize();
//        e.gc.drawImage(images[index], 0, 0, rect.width, rect.height, 0,
//            0, pt.x, pt.y);
      }
    });
    canvas.addListener(SWT.MouseDown, new Listener() {
      public void handleEvent(Event e) {
        browser.setUrl(getResourceString("Startup"));
      }
    });

    display.asyncExec(new Runnable() {
      public void run() {
        if (canvas.isDisposed())
          return;
        if (busy) {
//          index++;
//          if (index == images.length)
//            index = 0;
//          canvas.redraw();
        }
        display.timerExec(150, this);
      }
    });

    final Label status = new Label(parent, SWT.NONE);
    final ProgressBar progressBar = new ProgressBar(parent, SWT.NONE);

    FormData data = new FormData();
    data.top = new FormAttachment(0, 5);
  //  toolbar.setLayoutData(data);

    data = new FormData();
    data.left = new FormAttachment(0, 0);
    data.right = new FormAttachment(100, 0);
    data.top = new FormAttachment(canvas, 5, SWT.DEFAULT);
    data.bottom = new FormAttachment(status, -5, SWT.DEFAULT);
    try {
      browser = new Browser(parent, SWT.NONE);
      browser.setLayoutData(data);
    } catch (SWTError e) {
      /* Browser widget could not be instantiated */
      Label label = new Label(parent, SWT.CENTER | SWT.WRAP);
      label.setText(getResourceString("BrowserNotCreated"));
      label.setLayoutData(data);
    }

    data = new FormData();
    data.width = 24;
    data.height = 24;
    data.top = new FormAttachment(0, 5);
    data.right = new FormAttachment(100, -5);
    canvas.setLayoutData(data);

    data = new FormData();
//    data.top = new FormAttachment(toolbar, 0, SWT.TOP);
//    data.left = new FormAttachment(toolbar, 5, SWT.RIGHT);
    data.right = new FormAttachment(canvas, -5, SWT.DEFAULT);
    location.setLayoutData(data);
    location.setVisible(false);
    data = new FormData();
    data.left = new FormAttachment(0, 5);
    data.right = new FormAttachment(progressBar, 0, SWT.DEFAULT);
    data.bottom = new FormAttachment(100, -5);
    status.setLayoutData(data);

    data = new FormData();
    data.right = new FormAttachment(100, -5);
    data.bottom = new FormAttachment(100, -5);
    progressBar.setLayoutData(data);

    if (browser != null) {
//      itemBack.setEnabled(browser.isBackEnabled());
//      itemForward.setEnabled(browser.isForwardEnabled());
//
//      Listener listener = new Listener() {
//        public void handleEvent(Event event) {
//          ToolItem item = (ToolItem) event.widget;
//          if (item == itemBack)
//            browser.back();
//          else if (item == itemForward)
//            browser.forward();
//          else if (item == itemStop)
//            browser.stop();
//          else if (item == itemRefresh)
//            browser.refresh();
//          else if (item == itemGo)
//            browser.setUrl(location.getText());
//        }
//      };
      browser.addLocationListener(new LocationListener() {
        public void changed(LocationEvent event) {
          busy = true;
          if (event.top)
            location.setText(event.location);
        }

        public void changing(LocationEvent event) {
        }
      });
      browser.addProgressListener(new ProgressListener() {
        public void changed(ProgressEvent event) {
          if (event.total == 0)
            return;
          int ratio = event.current * 100 / event.total;
          progressBar.setSelection(ratio);
          busy = event.current != event.total;
          if (!busy) {
            index = 0;
            canvas.redraw();
          }
        }

        public void completed(ProgressEvent event) {
//          itemBack.setEnabled(browser.isBackEnabled());
//          itemForward.setEnabled(browser.isForwardEnabled());
          progressBar.setSelection(0);
          busy = false;
          index = 0;
          canvas.redraw();
        }
      });
      browser.addStatusTextListener(new StatusTextListener() {
        public void changed(StatusTextEvent event) {
          status.setText(event.text);
        }
      });
      if (parent instanceof Shell) {
        final Shell shell = (Shell) parent;
        browser.addTitleListener(new TitleListener() {
          public void changed(TitleEvent event) {
            shell.setText(event.title + " - "
                + getResourceString("window.title"));
          }
        });
      }
//      itemBack.addListener(SWT.Selection, listener);
//      itemForward.addListener(SWT.Selection, listener);
//      itemStop.addListener(SWT.Selection, listener);
//      itemRefresh.addListener(SWT.Selection, listener);
//      itemGo.addListener(SWT.Selection, listener);
      location.addListener(SWT.DefaultSelection, new Listener() {
        public void handleEvent(Event e) {
          browser.setUrl(location.getText());
        }
      });

      initialize(display, browser);
      browser.setUrl(getResourceString("Startup"));
    }
  }
  
  public void goToUrl(String url , LocationListener locationListener){
	  browser.setUrl(url);
	  browser.addLocationListener(locationListener);
  }

  /**
   * Gets a string from the resource bundle. We don't want to crash because of
   * a missing String. Returns the key if not found.
   */
  static String getResourceString(String key) {
    try {
      return "key";
    } catch (MissingResourceException e) {
      return key;
    } catch (NullPointerException e) {
      return "!" + key + "!";
    }
  }

  static void initialize(final Display display, Browser browser) {
    browser.addOpenWindowListener(new OpenWindowListener() {
      public void open(WindowEvent event) {
        System.out.println("Open");
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        //shell.setSize(460,240);
        Browser browser = new Browser(shell, SWT.NONE);
        initialize(display, browser);
        event.browser = browser;
      }
    });
    browser.addVisibilityWindowListener(new VisibilityWindowListener() {
      public void hide(WindowEvent event) {
      }

      public void show(WindowEvent event) {
        System.out.println("Show");
        Browser browser = (Browser) event.widget;
        Shell shell = browser.getShell();
        if (event.location != null)
          shell.setLocation(event.location);
        if (event.size != null) {
          Point size = event.size;
          shell.setSize(shell.computeSize(size.x, size.y));
        }
        shell.open();
      }
    });
    browser.addCloseWindowListener(new CloseWindowListener() {
      public void close(WindowEvent event) {
        System.out.println("Close");
        Browser browser = (Browser) event.widget;
        Shell shell = browser.getShell();
        shell.close();
      }
    });
  }

  /**
   * Disposes of all resources associated with a particular instance of the
   * BrowserExample.
   */
  public void dispose() {
    freeResources();
  }

  /**
   * Frees the resources
   */
  void freeResources() {
//    if (images != null) {
//      for (int i = 0; i < images.length; ++i) {
//        final Image image = images[i];
//        if (image != null)
//          image.dispose();
//      }
//      images = null;
    //}
  }

  /**
   * Grabs input focus.
   */
  public void setFocus() {
    location.setFocus();
  }
}
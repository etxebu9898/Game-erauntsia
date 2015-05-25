// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.baseapp.fwd;

import android.app.ActionBar;
import android.app.Activity;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.Bitmap;
import android.net.*;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.webkit.*;
import android.widget.ProgressBar;
import java.io.*;
import java.util.Locale;

// Referenced classes of package com.baseapp.fwd:
//            ApplicationContextHolder, AboutDialog

public class Dashboard extends Activity
{
    private class FixedWebViewClient extends WebViewClient
    {

        public boolean shouldOverrideUrlLoading(WebView webview, String s)
        {
            if(s.contains("unsupported") && s.contains(".pdf"))
                return true;
            if(s.endsWith("?download=true"))
            {
                try
                {
                    webview = new Intent("android.intent.action.VIEW", Uri.parse(s));
                    startActivity(webview);
                }
                // Misplaced declaration of an exception variable
                catch(WebView webview)
                {
                    return true;
                }
                return true;
            }
            if(s.endsWith(".pdf"))
            {
                s = (new StringBuilder()).append(s).append("?download=true").toString();
                webview.loadUrl((new StringBuilder()).append("https://docs.google.com/viewer?embedded=true&url=").append(s).toString());
                return true;
            }
            if(s.startsWith(mUrl) || s.startsWith("javascript:"))
                return false;
            if(s.startsWith("mailto:"))
            {
                s = MailTo.parse(s);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("message/rfc822");
                intent.putExtra("android.intent.extra.EMAIL", new String[] {
                    s.getTo()
                });
                intent.putExtra("android.intent.extra.SUBJECT", s.getSubject());
                intent.putExtra("android.intent.extra.CC", s.getCc());
                intent.putExtra("android.intent.extra.TEXT", s.getBody());
                webview.getContext().startActivity(intent);
                return true;
            }
            if(s.startsWith("tel:"))
            {
                webview = new Intent("android.intent.action.DIAL", Uri.parse(s));
                startActivity(webview);
                return true;
            } else
            {
                return false;
            }
        }

        final Dashboard this$0;

        private FixedWebViewClient()
        {
            this$0 = Dashboard.this;
            super();
        }

    }

    final class JavascriptInterface
    {

        public int getVersionCode()
        {
            return versionCode;
        }

        public void setEnablePreferencesMenu()
        {
            enablePreferencesMenu = true;
        }

        public void setModalIsVisible(boolean flag)
        {
            modalIsVisible = flag;
        }

        public void setUrlForSharing(String s)
        {
            urlForSharing = s;
        }

        public boolean enablePreferencesMenu;
        public boolean modalIsVisible;
        final Dashboard this$0;
        public String urlForSharing;
        public int versionCode;

        JavascriptInterface()
        {
            this$0 = Dashboard.this;
            super();
            enablePreferencesMenu = false;
            modalIsVisible = false;
            versionCode = 0;
            urlForSharing = null;
        }
    }


    public Dashboard()
    {
        mUrl = BASE_URL;
        mLanguage = "en";
        mPrevBackbuttonTouchTime = false;
        doubleBackToExitPressedOnce = false;
        cancel = false;
        mOrientation = null;
        actionBar = false;
        progressRunnable = null;
        handler = new Handler();
    }

    private String getAppName()
    {
        Object obj;
        PackageManager packagemanager;
        packagemanager = getPackageManager();
        obj = null;
        android.content.pm.ApplicationInfo applicationinfo = packagemanager.getApplicationInfo(getApplicationContext().getPackageName(), 0);
        obj = applicationinfo;
_L2:
        if(obj != null)
            obj = packagemanager.getApplicationLabel(((android.content.pm.ApplicationInfo) (obj)));
        else
            obj = getString(0x7f060000);
        return (String)(String)obj;
        android.content.pm.PackageManager.NameNotFoundException namenotfoundexception;
        namenotfoundexception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private WebView getEngine()
    {
        return (WebView)findViewById(0x7f090004);
    }

    public static boolean isDeviceOnline(Context context)
    {
        boolean flag;
        try
        {
            flag = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo().isConnected();
        }
        // Misplaced declaration of an exception variable
        catch(Context context)
        {
            return false;
        }
        return flag;
    }

    private boolean isNetworkAvailable()
    {
        NetworkInfo networkinfo = ((ConnectivityManager)getSystemService("connectivity")).getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnected();
    }

    private void loadParams()
    {
        InputStream inputstream;
        BufferedReader bufferedreader;
        inputstream = getAssets().open("params.txt");
        bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        mUrl = bufferedreader.readLine();
        mLanguage = bufferedreader.readLine();
        mOrientation = bufferedreader.readLine();
        if(mUrl == null || mUrl.trim().equals(""))
            mUrl = BASE_URL;
        if(mLanguage == null || mLanguage.trim().equals("") || mLanguage.trim().equals("choose"))
            mLanguage = "en";
        setLanguage();
        if(!"L".equals(mOrientation)) goto _L2; else goto _L1
_L1:
        setRequestedOrientation(0);
_L7:
        if(!"Y".equals(bufferedreader.readLine())) goto _L4; else goto _L3
_L3:
        engine.setScrollBarStyle(0);
_L5:
        bufferedreader.close();
        inputstream.close();
        return;
_L2:
        try
        {
            if("P".equals(mOrientation))
                setRequestedOrientation(1);
        }
        catch(Throwable throwable)
        {
            return;
        }
        continue; /* Loop/switch isn't completed */
_L4:
        engine.setVerticalScrollBarEnabled(false);
        engine.setScrollbarFadingEnabled(true);
          goto _L5
        if(true) goto _L7; else goto _L6
_L6:
    }

    private void setLanguage()
    {
        Locale locale = Locale.getDefault();
        String s = locale.getLanguage();
        if(mLanguage == null || s.equalsIgnoreCase(mLanguage))
        {
            return;
        } else
        {
            locale = new Locale(mLanguage, locale.getCountry());
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.locale = locale;
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
            return;
        }
    }

    public String getLanguage()
    {
        return mLanguage;
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        if(i != 1 || mUploadMessage == null)
            return;
        if(intent == null || j != -1)
            intent = null;
        else
            intent = intent.getData();
        mUploadMessage.onReceiveValue(intent);
        mUploadMessage = null;
    }

    public void onBackPressed()
    {
        cancel = false;
        if(doubleBackToExitPressedOnce)
        {
            showFinishDialog();
            mPrevBackbuttonTouchTime = true;
        }
        doubleBackToExitPressedOnce = true;
        (new Handler()).postDelayed(new Runnable() {

            public void run()
            {
label0:
                {
                    doubleBackToExitPressedOnce = false;
                    if(!mPrevBackbuttonTouchTime && !cancel)
                    {
                        if(!engine.canGoBack())
                            break label0;
                        engine.goBack();
                    }
                    return;
                }
                finish();
            }

            final Dashboard this$0;

            
            {
                this$0 = Dashboard.this;
                super();
            }
        }
, 1000L);
    }

    protected void onCreate(final Bundle progressBar)
    {
        super.onCreate(progressBar);
        App = ApplicationContextHolder.getAppInstance();
        boolean flag = getResources().getBoolean(0x7f040000);
        if(flag && android.os.Build.VERSION.SDK_INT >= 9)
            requestWindowFeature(8);
        setContentView(0x7f030001);
        if(flag)
        {
            if(android.os.Build.VERSION.SDK_INT >= 9)
            {
                progressBar = getActionBar();
                if(progressBar != null)
                {
                    progressBar.setDisplayHomeAsUpEnabled(true);
                    progressBar.setDisplayUseLogoEnabled(false);
                    actionBar = true;
                }
            }
        } else
        {
            getWindow().addFlags(1024);
        }
        engine = (WebView)findViewById(0x7f090004);
        engine.getSettings().setAppCacheMaxSize(0x500000L);
        engine.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        engine.getSettings().setAllowFileAccess(true);
        engine.getSettings().setAppCacheEnabled(true);
        engine.getSettings().setJavaScriptEnabled(true);
        engine.getSettings().setCacheMode(-1);
        if(!isNetworkAvailable())
            if(App.getisPageLoaded() != null && App.getisPageLoaded().equalsIgnoreCase("Yes"))
            {
                engine.getSettings().setCacheMode(1);
            } else
            {
                engine.setVisibility(4);
                showNetworkStatusDialog();
            }
        engine.setDownloadListener(new DownloadListener() {

            public void onDownloadStart(String s, String s1, String s2, String s3, long l)
            {
                if(s.contains("unsupported") && s.contains(".pdf"))
                    return;
                if(s.endsWith("?download=true"))
                    try
                    {
                        s = new Intent("android.intent.action.VIEW", Uri.parse(s));
                        startActivity(s);
                        return;
                    }
                    // Misplaced declaration of an exception variable
                    catch(String s)
                    {
                        return;
                    }
                if(s.endsWith(".pdf") || s3.contains("pdf"))
                {
                    s = (new StringBuilder()).append(s).append("?download=true").toString();
                    engine.loadUrl((new StringBuilder()).append("https://docs.google.com/viewer?embedded=true&url=").append(s).toString());
                    return;
                }
                try
                {
                    s = new Intent("android.intent.action.VIEW", Uri.parse(s));
                    startActivity(s);
                    return;
                }
                // Misplaced declaration of an exception variable
                catch(String s)
                {
                    return;
                }
            }

            final Dashboard this$0;

            
            {
                this$0 = Dashboard.this;
                super();
            }
        }
);
        progressBar = (ProgressBar)findViewById(0x7f090005);
        engine.setWebChromeClient(new WebChromeClient() {

            public void onGeolocationPermissionsShowPrompt(String s, android.webkit.GeolocationPermissions.Callback callback)
            {
                callback.invoke(s, true, false);
            }

            public void onProgressChanged(WebView webview, int i)
            {
                if(i == 100)
                {
                    App.saveIsPageLoaded("Yes");
                    progressBar.setVisibility(8);
                }
            }

            public void openFileChooser(ValueCallback valuecallback)
            {
                mUploadMessage = valuecallback;
                valuecallback = new Intent("android.intent.action.GET_CONTENT");
                valuecallback.addCategory("android.intent.category.OPENABLE");
                valuecallback.setType("image/*");
                startActivityForResult(Intent.createChooser(valuecallback, getString(0x7f060007)), 1);
            }

            public void openFileChooser(ValueCallback valuecallback, String s)
            {
                mUploadMessage = valuecallback;
                valuecallback = new Intent("android.intent.action.GET_CONTENT");
                valuecallback.addCategory("android.intent.category.OPENABLE");
                valuecallback.setType("*/*");
                startActivityForResult(Intent.createChooser(valuecallback, getString(0x7f060008)), 1);
            }

            public void openFileChooser(ValueCallback valuecallback, String s, String s1)
            {
                mUploadMessage = valuecallback;
                valuecallback = new Intent("android.intent.action.GET_CONTENT");
                valuecallback.addCategory("android.intent.category.OPENABLE");
                valuecallback.setType("image/*");
                startActivityForResult(Intent.createChooser(valuecallback, getString(0x7f060007)), 1);
            }

            final Dashboard this$0;
            final ProgressBar val$progressBar;

            
            {
                this$0 = Dashboard.this;
                progressBar = progressbar;
                super();
            }
        }
);
        engine.setWebViewClient(new FixedWebViewClient() {

            public void onPageFinished(WebView webview, String s)
            {
                if(progressRunnable != null)
                    handler.removeCallbacks(progressRunnable);
                progressBar.setVisibility(8);
            }

            public void onPageStarted(WebView webview, String s, Bitmap bitmap)
            {
                jsInterface.enablePreferencesMenu = false;
                jsInterface.modalIsVisible = false;
                jsInterface.urlForSharing = null;
                if(progressRunnable == null)
                    progressRunnable = new Runnable() {

                        public void run()
                        {
                            progressBar.setVisibility(0);
                        }

                        final _cls3 this$1;

            
            {
                this$1 = _cls3.this;
                super();
            }
                    }
;
                handler.postDelayed(progressRunnable, 500L);
            }

            public void onReceivedError(WebView webview, int i, String s, String s1)
            {
                engine.setVisibility(4);
                showCachedNetworkStatusDialog();
            }

            final Dashboard this$0;
            final ProgressBar val$progressBar;

            
            {
                this$0 = Dashboard.this;
                progressBar = progressbar;
                super();
            }
        }
);
        engine.getSettings().setJavaScriptEnabled(true);
        engine.getSettings().setLoadWithOverviewMode(true);
        engine.getSettings().setUseWideViewPort(true);
        jsInterface = new JavascriptInterface();
        try
        {
            progressBar = new ComponentName(this, com/baseapp/fwd/Dashboard);
            progressBar = getPackageManager().getPackageInfo(progressBar.getPackageName(), 0);
            jsInterface.versionCode = ((PackageInfo) (progressBar)).versionCode;
        }
        // Misplaced declaration of an exception variable
        catch(final Bundle progressBar) { }
        engine.addJavascriptInterface(jsInterface, "androidlearnscripture");
        loadParams();
        mAboutDialog = new AboutDialog(this, 0x7f070004);
        engine.setScrollbarFadingEnabled(true);
        engine.loadUrl(mUrl);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuinflater = getMenuInflater();
        if(actionBar)
            menuinflater.inflate(0x7f080001, menu);
        else
            menuinflater.inflate(0x7f080000, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        menuitem.getItemId();
        JVM INSTR tableswitch 2131296262 2131296265: default 36
    //                   2131296262 48
    //                   2131296263 66
    //                   2131296264 57
    //                   2131296265 42;
           goto _L1 _L2 _L3 _L4 _L5
_L1:
        return super.onOptionsItemSelected(menuitem);
_L5:
        finish();
        return true;
_L2:
        getEngine().reload();
        return true;
_L4:
        mAboutDialog.show();
        return true;
_L3:
        String s = getApplicationContext().getPackageName();
        s = (new StringBuilder()).append("https://play.google.com/store/apps/details?id=").append(s).toString();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        String s1 = getAppName();
        intent.putExtra("android.intent.extra.SUBJECT", (new StringBuilder()).append(getString(0x7f060006).trim()).append(" ").append(s1).append("!").toString());
        intent.putExtra("android.intent.extra.TEXT", s);
        startActivity(Intent.createChooser(intent, getString(0x7f060005)));
        if(true) goto _L1; else goto _L6
_L6:
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public void showCachedNetworkStatusDialog()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getAppName());
        builder.setMessage(0x7f060004);
        builder.setCancelable(false);
        builder.setPositiveButton(0x7f060001, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                App.saveIsPageLoaded("Yes");
                finish();
            }

            final Dashboard this$0;

            
            {
                this$0 = Dashboard.this;
                super();
            }
        }
);
        builder.show();
    }

    public void showFinishDialog()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getAppName());
        builder.setMessage(0x7f060003);
        builder.setCancelable(false);
        builder.setPositiveButton(0x7f060001, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                finish();
                mPrevBackbuttonTouchTime = false;
            }

            final Dashboard this$0;

            
            {
                this$0 = Dashboard.this;
                super();
            }
        }
);
        builder.setNegativeButton(0x7f060002, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                mPrevBackbuttonTouchTime = false;
                cancel = true;
            }

            final Dashboard this$0;

            
            {
                this$0 = Dashboard.this;
                super();
            }
        }
);
        builder.show();
    }

    public void showNetworkStatusDialog()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getAppName());
        builder.setMessage(0x7f060004);
        builder.setCancelable(false);
        builder.setPositiveButton(0x7f060001, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                App.saveIsPageLoaded("No");
                finish();
            }

            final Dashboard this$0;

            
            {
                this$0 = Dashboard.this;
                super();
            }
        }
);
        builder.show();
    }

    public static String BASE_URL = "http://baseurl.com/";
    private static final int FILECHOOSER_RESULTCODE = 1;
    ApplicationContextHolder App;
    private boolean actionBar;
    private boolean cancel;
    private boolean doubleBackToExitPressedOnce;
    private WebView engine;
    private Handler handler;
    private JavascriptInterface jsInterface;
    private AboutDialog mAboutDialog;
    private String mLanguage;
    private String mOrientation;
    private boolean mPrevBackbuttonTouchTime;
    private ValueCallback mUploadMessage;
    private String mUrl;
    private Runnable progressRunnable;




/*
    static ValueCallback access$102(Dashboard dashboard, ValueCallback valuecallback)
    {
        dashboard.mUploadMessage = valuecallback;
        return valuecallback;
    }

*/




/*
    static Runnable access$402(Dashboard dashboard, Runnable runnable)
    {
        dashboard.progressRunnable = runnable;
        return runnable;
    }

*/




/*
    static boolean access$602(Dashboard dashboard, boolean flag)
    {
        dashboard.mPrevBackbuttonTouchTime = flag;
        return flag;
    }

*/



/*
    static boolean access$702(Dashboard dashboard, boolean flag)
    {
        dashboard.cancel = flag;
        return flag;
    }

*/


/*
    static boolean access$802(Dashboard dashboard, boolean flag)
    {
        dashboard.doubleBackToExitPressedOnce = flag;
        return flag;
    }

*/

}

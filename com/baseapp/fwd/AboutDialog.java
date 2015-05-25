// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.baseapp.fwd;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.*;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

// Referenced classes of package com.baseapp.fwd:
//            Dashboard

public class AboutDialog extends Dialog
{

    public AboutDialog(Context context)
    {
        super(context);
    }

    public AboutDialog(Dashboard dashboard, int i)
    {
        super(dashboard, i);
        init(dashboard);
    }

    private void init(Dashboard dashboard)
    {
        Object obj;
        TextView textview;
        setContentView(0x7f030000);
        textview = (TextView)findViewById(0x7f090001);
        obj = "";
        Object obj1;
        obj1 = dashboard.getPackageManager().getPackageInfo(dashboard.getApplicationContext().getPackageName(), 0);
        obj1 = (new StringBuilder()).append(((PackageInfo) (obj1)).applicationInfo.loadLabel(dashboard.getPackageManager())).append(" ").append(((PackageInfo) (obj1)).versionName).toString();
        obj = obj1;
_L2:
        textview.setText(((CharSequence) (obj)));
        obj = (TextView)findViewById(0x7f090002);
        ((TextView) (obj)).setText(Html.fromHtml(dashboard.getString(0x7f060009).replace("ApkCreator", "<a href=\"https://play.google.com/store/apps/details?id=com.apkcreator.fwd\">ApkCreator</a>")));
        ((TextView) (obj)).setMovementMethod(LinkMovementMethod.getInstance());
        return;
        Exception exception;
        exception;
        if(true) goto _L2; else goto _L1
_L1:
    }

    public void showDialog()
    {
        show();
    }
}

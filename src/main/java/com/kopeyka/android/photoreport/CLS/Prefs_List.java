package com.kopeyka.android.photoreport.CLS;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public final class Prefs_List extends ListPreference
{

    public Prefs_List(final Context ctx, final AttributeSet attrs)
    {
        super(ctx, attrs);
    }
    public Prefs_List(final Context ctx)
    {
        super(ctx);
    }


    @Override
    public CharSequence getSummary ()
    {
        return getEntry();
    }
}



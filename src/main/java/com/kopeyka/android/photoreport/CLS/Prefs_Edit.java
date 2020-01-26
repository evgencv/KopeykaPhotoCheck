package com.kopeyka.android.photoreport.CLS;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public final class Prefs_Edit extends EditTextPreference
{

    public Prefs_Edit(final Context ctx, final AttributeSet attrs)
    {
        super(ctx, attrs);
    }
    public Prefs_Edit(final Context ctx)
    {
        super(ctx);
    }


    @Override
    public void setText(final String value)
    {
        super.setText(value);
        setSummary(getText());
    }
}



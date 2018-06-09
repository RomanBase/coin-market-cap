package com.ankhrom.base.interfaces;

import android.view.KeyEvent;

/**
 * Created by R' on 6/9/2018.
 */
public interface OnImeActionListener {

    boolean onImeAction(String value, int action, KeyEvent event);
}

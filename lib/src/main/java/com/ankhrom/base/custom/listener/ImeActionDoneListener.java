package com.ankhrom.base.custom.listener;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.ankhrom.base.interfaces.OnImeActionListener;

/**
 * Created by R' on 6/9/2018.
 */
public abstract class ImeActionDoneListener implements OnImeActionListener {

    protected abstract void onDone(String value);

    @Override
    public boolean onImeAction(String value, int action, KeyEvent event) {

        if (action == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            onDone(value);
            return true;
        }

        return false;
    }
}

package com.king.chatview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.king.chatview.utils.ContextHolder;

/**
 * Created by Administrator on 2015/11/10.
 */
public abstract class BaseFragment extends Fragment{
    protected abstract View onInitializeView(LayoutInflater inflater, ViewGroup container,
                                             Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return onInitializeView(inflater, container, savedInstanceState);
    }

    protected final Context getAppContext() {
        return ContextHolder.getInstance().get();
    }

    protected void hideSoftKeyboard()
    {
        final View currentRoot = getView();
        if (currentRoot != null)
        {
            final InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            currentRoot.postDelayed(new Runnable() {
                public void run() {
                    mgr.hideSoftInputFromWindow(currentRoot.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 100);
        }
    }

    protected void handleLeftAction() {
        if(false == handleBack()) {
//            finishActivity();
        }
    }

    public boolean handleBack()
    {
        return false;
    }
}

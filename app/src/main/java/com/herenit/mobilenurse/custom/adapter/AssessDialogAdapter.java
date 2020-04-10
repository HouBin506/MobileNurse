package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;

import com.herenit.arms.base.adapter.rv.MultiItemTypeAdapter;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.custom.adapter.delegate.AssessCalendarDelegate;
import com.herenit.mobilenurse.custom.adapter.delegate.AssessCheckBoxDelegate;
import com.herenit.mobilenurse.custom.adapter.delegate.AssessDoubleInputViewDelegate;
import com.herenit.mobilenurse.custom.adapter.delegate.AssessEditTextDelegate;
import com.herenit.mobilenurse.custom.adapter.delegate.AssessRadioGroupDelegate;
import com.herenit.mobilenurse.custom.adapter.delegate.AssessTextInputVewDelegate;

import java.util.List;

/**
 * Created by HouBin on 2018/9/6.
 */

public class AssessDialogAdapter extends MultiItemTypeAdapter<AssessViewItem> {

    public AssessDialogAdapter(Context context, List<AssessViewItem> datas) {
        super(context, datas);
        addItemViewDelegate(new AssessCalendarDelegate());
        addItemViewDelegate(new AssessCheckBoxDelegate(context, this));
        addItemViewDelegate(new AssessDoubleInputViewDelegate());
        addItemViewDelegate(new AssessTextInputVewDelegate());
        addItemViewDelegate(new AssessEditTextDelegate());
        addItemViewDelegate(new AssessRadioGroupDelegate(context, this));
    }
}

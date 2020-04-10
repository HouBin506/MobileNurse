package com.herenit.mobilenurse.custom.adapter.delegate;

import android.content.Context;

import com.herenit.arms.base.adapter.rv.base.ItemViewDelegate;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.custom.adapter.AssessDialogAdapter;
import com.herenit.mobilenurse.custom.widget.dialog.assess.AssessDialog;


/**
 * Created by HouBin on 2018/9/13.
 */

public abstract class AssessBaseDelegate implements ItemViewDelegate<AssessViewItem> {

    protected Context mContext;
    protected AssessDialogAdapter mAdapter;

    public AssessBaseDelegate(Context context, AssessDialogAdapter adapter) {
        this.mContext = context;
        this.mAdapter = adapter;
    }

    protected void showAddHandle(AssessViewItem model) {
        if (model.getSubModel() == null)
            return;
        final AssessDialog dialog = new AssessDialog(mContext, model, new AssessDialog.AssessSureClickListener() {
            @Override
            public void onSureClick(AssessViewItem model) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelClick(AssessViewItem model) {
            }
        });
        dialog.showDialog();
    }
}

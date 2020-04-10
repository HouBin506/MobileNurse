package com.herenit.mobilenurse.custom.widget.dialog.assess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.custom.adapter.AssessDialogAdapter;

/**
 * Created by HouBin on 2018/9/5.
 */

public class AssessDialog {
    private AssessViewItem mModel;
    private AssessViewItem mCloneModel;

    private RecyclerView mRecyclerView;
    private AssessDialogAdapter mAdapter;
    private AlertDialog.Builder builder;

    private AssessSureClickListener mListener;

    public AssessDialog(Context context, AssessViewItem model, AssessSureClickListener listener) {
        initView(context, model, listener);
    }

    private void initView(final Context context, AssessViewItem model, AssessSureClickListener listener) {
        this.mModel = model;
        this.mListener = listener;
        builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_assess, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_assess_dialog_subModel);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mCloneModel = AssessViewItem.clone(mModel);
        mAdapter = new AssessDialogAdapter(context, mCloneModel.getSubModel());
        mRecyclerView.setAdapter(mAdapter);
        builder.setView(view);
        builder.setTitle(mCloneModel.getText());
        //确定按钮
        builder.setPositiveButton(ArmsUtils.getString(context, R.string.btn_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AssessViewItem.assignModelValue(mModel, mCloneModel);
                mModel.buildItemValue();
                mListener.onSureClick(mModel);
            }
        });
        //取消按钮
        builder.setNegativeButton(ArmsUtils.getString(context, R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mListener.onCancelClick(mModel);
            }
        });
    }

    public void showDialog() {
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public interface AssessSureClickListener {
        void onSureClick(AssessViewItem model);

        void onCancelClick(AssessViewItem model);
    }
}

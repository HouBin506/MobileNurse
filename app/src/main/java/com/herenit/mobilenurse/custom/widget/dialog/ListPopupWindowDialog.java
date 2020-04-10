package com.herenit.mobilenurse.custom.widget.dialog;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.MNUtils;
import com.herenit.mobilenurse.app.utils.Utils;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.common.NameCode;
import com.herenit.mobilenurse.custom.adapter.NameCodeAdapter;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;

import java.util.List;
import java.util.jar.Attributes;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/29 11:27
 * desc: 账号核对弹窗
 */
public class ListPopupWindowDialog extends BaseDialog {


    @BindView(R2.id.tv_listPopupWindowDialog_message)
    TextView mTv_message;
    @BindView(R2.id.tv_listPopupWindowDialog_content)
    TextView mTv_content;
    private String mMessage;
    private boolean mMessageVisibility;
    private ListDialog mListDialog;
    private NameCodeAdapter mAdapter;

    private int mCurrentPosition = 0;

    private ListPopupWindowDialog(Builder builder) {
        super(builder);
    }


    @Override
    protected <T extends BaseDialog.Builder> void buildDialog(T builder) {
        Builder sBuilder = (Builder) builder;
        mMessage = sBuilder.message;
        mAdapter = sBuilder.adapter;
        mCurrentPosition = sBuilder.defaultPosition;
        mMessageVisibility = sBuilder.messageVisibility;
    }

    @Override
    protected void setView(View layout) {
        if (mAdapter != null)
            mListDialog = ViewUtils.createNoBottomListDialog(mContext, mTitle, mAdapter);
        if (mListDialog != null)
            mListDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mListDialog.dismiss();
                    mCurrentPosition = position;
                    NameCode item = (NameCode) mAdapter.getData().get(position);
                    mTv_content.setText(item.getName());
                }
            });
        if (!TextUtils.isEmpty(mMessage))
            mTv_message.setText(mMessage);
        if (mMessageVisibility)
            mTv_message.setVisibility(View.VISIBLE);
        else
            mTv_message.setVisibility(View.GONE);
        mTv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListDialog != null)
                    mListDialog.show();
            }
        });
        NameCode item = getCurrentItem();
        if (item != null)
            mTv_content.setText(item.getName());
    }

    private NameCode getCurrentItem() {
        if (mAdapter != null) {
            List data = mAdapter.getData();
            if (data != null && !data.isEmpty()) {
                NameCode item = (NameCode) data.get(mCurrentPosition);
                return item;
            }
        }
        return null;
    }

    /**
     * 设置确定取消按钮监听，如果确定按钮，需要将输入框中的数据返回给调用者
     *
     * @param listener
     */
    @Override
    public void setPositiveNegativeListener(PositiveNegativeListener listener) {
        mTv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NameCode item = getCurrentItem();
                listener.onPositive(item);
            }
        });
        mTv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegative();
            }
        });
    }

    public static final class Builder extends BaseDialog.Builder<Builder> {

        //弹窗描述
        private String message;
        //描述是否可见
        private boolean messageVisibility;

        private NameCodeAdapter adapter;

        private int defaultPosition;

        public Builder(Context context) {
            super(context, R.layout.dialog_list_popup_window);
            messageVisibility = false;
            defaultPosition = 0;
        }


        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder adapter(NameCodeAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public Builder defaultPosition(int defaultPosition) {
            this.defaultPosition = defaultPosition;
            return this;
        }

        public Builder messageVisibility(boolean messageVisibility) {
            this.messageVisibility = messageVisibility;
            return this;
        }


        @Override
        public ListPopupWindowDialog build() {
            return new ListPopupWindowDialog(this);
        }
    }
}

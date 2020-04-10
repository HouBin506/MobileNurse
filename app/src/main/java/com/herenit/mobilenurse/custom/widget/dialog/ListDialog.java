package com.herenit.mobilenurse.custom.widget.dialog;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.enums.ChoiceTypeEnum;
import com.herenit.mobilenurse.custom.extra.MultipleChoiceExtra;
import com.herenit.mobilenurse.custom.extra.WidgetExtra;
import com.herenit.mobilenurse.custom.listener.PositiveNegativeListener;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/1/18 13:41
 * desc: 列表弹窗
 */
public class ListDialog extends BaseDialog  {

    @BindView(R2.id.lv_listDialog_content)
    ListView mListView;
    private ListAdapter mAdapter;

    //选择类型
    private ChoiceTypeEnum mChoiceType;

    ListDialog(Builder builder) {
        super(builder);
    }

    @Override
    protected <T extends BaseDialog.Builder> void buildDialog(T builder) {
        Builder lBuilder = (Builder) builder;
        this.mAdapter = lBuilder.adapter;
        this.mChoiceType = lBuilder.choiceType;
    }

    @Override
    protected void setView(View layout) {
        mListView.setAdapter(mAdapter);
    }

    public void setOnItemClickListener(@NonNull AdapterView.OnItemClickListener onItemClickListener) {
        mListView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void setPositiveNegativeListener(@NonNull PositiveNegativeListener listener) {
        if (mChoiceType == ChoiceTypeEnum.MULTIPLE) {//多选的弹窗列表，最后要返回多选结果
            if (mAdapter instanceof MultipleChoiceExtra) {
                mTv_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onPositive(((MultipleChoiceExtra) mAdapter).getChoiceResult());
                    }
                });
            } else {//为了在ListDialog中获取到多选列表的选择结果，需要Adapter实现MultipleChoiceExtra，对外提供选择结果
                throw new ClassCastException("ChoiceType = MULTIPLE,adapter must implement " + MultipleChoiceExtra.class.getName());
            }
        } else if (mChoiceType == ChoiceTypeEnum.SINGLE) {
            mTv_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPositive();
                }
            });
        }
        mTv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegative();
            }
        });
    }

    public static final class Builder extends BaseDialog.Builder<Builder> {
        //ListDialog中ListView的适配器
        private ListAdapter adapter;
        private String title;
        private boolean titleVisibility;
        //选择类型
        private ChoiceTypeEnum choiceType;
        private Builder builder;

        public Builder(Context context) {
            super(context, R.layout.dialog_list);
            this.titleVisibility = true;
        }

        public Builder adapter(BaseAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        /**
         * 设置列表选择类型，单选、多选
         *
         * @param choiceType
         * @return
         */
        public Builder choiceType(ChoiceTypeEnum choiceType) {
            this.choiceType = choiceType;
            return this;
        }

        public ListDialog build() {
            return new ListDialog(this);
        }
    }
}

package com.herenit.mobilenurse.mvp.assess.health_edu.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herenit.arms.di.component.AppComponent;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.TimeUtils;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.AssessModel;
import com.herenit.mobilenurse.criteria.entity.HealthEduAssessModel;
import com.herenit.mobilenurse.criteria.entity.MultiListMenuItem;
import com.herenit.mobilenurse.criteria.entity.Order;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.SummaryDataModel;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;
import com.herenit.mobilenurse.criteria.entity.submit.HealthEduAssessParam;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.custom.widget.dialog.LoadingDialog;
import com.herenit.mobilenurse.custom.widget.layout.MNAssessListView;
import com.herenit.mobilenurse.di.component.DaggerHealthEduContentComponent;
import com.herenit.mobilenurse.mvp.assess.AssessHelper;
import com.herenit.mobilenurse.mvp.assess.health_edu.HealthEduActivity;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;
import com.herenit.mobilenurse.mvp.orders.OrdersHelp;
import com.herenit.mobilenurse.mvp.tool.order.OrdersViewerActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/9/16 11:09
 * desc:健康宣教内容
 */
public class HealthEduContentActivity extends BasicBusinessActivity<HealthEduContentPresenter> implements HealthEduContentContract.View {

    //当前患者
    private Sickbed mSickbed;
    //文档Id
    private String mDocId;
    @BindView(R2.id.srl_healthEduContent_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    //选中的宣教项目
    private List<MultiListMenuItem> mSelectedItemList = new ArrayList<>();

    @BindView(R2.id.listView_healthEduContent_result)
    MNAssessListView mLv_result;

    //已经选择的教育项目
    private TextView mTv_selectItem;
    //输入框输入的教育内容
    private EditText mEt_inputContent;
    //医嘱导入按钮
    private LinearLayout mLl_importOrder;
    //当前时间
    private Date mCurrentTime = TimeUtils.getCurrentDate();
    private LoadingDialog mLoadingDialog;
    //最终要返回给服务器端的参数
    HealthEduAssessParam mAssessParam = new HealthEduAssessParam();

    private final String INPUT_HEALTH_CONTENT_CODE = "健康教育内容_input";

    private int mOperationType;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHealthEduContentComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.TV_TV_TV;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_health_edu_content;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            //获取当前患者
            mSickbed = (Sickbed) getIntent().getSerializableExtra(KeyConstant.NAME_EXTRA_SICKBED);
            //获取上个页面选中的宣教项目
            List<MultiListMenuItem> selectedItemList = (List<MultiListMenuItem>) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_SELECTED_ITEM_LIST);
            mSelectedItemList.clear();
            if (selectedItemList != null)
                mSelectedItemList.addAll(selectedItemList);
            //获取当前页面的操作类型，默认为新增宣教，也可能是更新
            mOperationType = intent.getIntExtra(KeyConstant.NAME_EXTRA_OPERATION_TYPE, CommonConstant.OPERATION_TYPE_PERSIST);
            //获取文档号，如果是从历史记录列表进入的，则属于更新操作，需要带入文档号
            mDocId = intent.getStringExtra(KeyConstant.NAME_EXTRA_DOC_ID);
        }
        initView();
        initData();

    }

    private void initData() {
        mPresenter.getHealthEduResult(false, mDocId);
    }

    private void initView() {
        //标题栏
        setTitleBarLeftText(ArmsUtils.getString(mContext, R.string.title_module_healthEducation));
        setTitleBarLeftTextDrawableLeft(R.mipmap.ic_arrow_left_back);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {//点击左上角返回按钮
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setTitleBarRightOnClickListener1(new View.OnClickListener() {//点击保存按钮
            @Override
            public void onClick(View v) {
                //保存
                commitHealthEduContent();
            }
        });
        if (mSickbed != null)
            setTitleBarCenterText(mSickbed.getPatientTitle());
        setTitleBarRightText1(ArmsUtils.getString(mContext, R.string.btn_save));
        setTitleBarRightBackgroundResource1(R.drawable.shape_bg_text_transparent_solid_white_stroke);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh(CommonConstant.REFRESH_FAIL_TIMEOUT_MILLISECOND, false);
                mPresenter.getHealthEduResult(true, mDocId);
            }
        });

        //添加宣教内容UI
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View content = inflater.inflate(R.layout.layout_health_edu_content, null);
        mLv_result.addHeaderView(content);
        mTv_selectItem = content.findViewById(R.id.tv_healthEduContent_selectedItem);
        mEt_inputContent = content.findViewById(R.id.et_healthEduContent_inputContent);
        mLl_importOrder = content.findViewById(R.id.ll_healthEduContent_importOrder);
        //导入医嘱
        mLl_importOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrdersViewerActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, mSickbed);
                intent.putExtra(KeyConstant.NAME_EXTRA_OPERATION_TYPE, CommonConstant.OPERATION_TYPE_SELECT);
                startActivityForResult(intent, CommonConstant.REQUEST_CODE_SELECT_ORDERS);
            }
        });
        //进入宣教项目选择列表，重新选择宣教项目
        content.findViewById(R.id.img_healthEduContent_resetSelectedItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HealthEduActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_SICKBED, mSickbed);
                intent.putExtra(KeyConstant.NAME_EXTRA_OPERATION_TYPE, CommonConstant.OPERATION_TYPE_UPDATE);
                intent.putExtra(KeyConstant.NAME_EXTRA_SELECTED_ITEM_LIST, (Serializable) mSelectedItemList);
                startActivityForResult(intent, CommonConstant.REQUEST_CODE_MODIFY_CONDITION);
            }
        });

    }

    /**
     * 提交要保存的数据
     */
    private void commitHealthEduContent() {
        AssessHelper.buildAssessParamByAssessViewList(mAssessParam, mLv_result.getViewDataList(), mCurrentTime);
        //额外附加健康教育界面输入框内容
        SummaryDataModel healthEduInputContent = new SummaryDataModel();
        healthEduInputContent.setDataName(INPUT_HEALTH_CONTENT_CODE);
        healthEduInputContent.setDataValue(mEt_inputContent.getText().toString());
        healthEduInputContent.setDataType("");
        healthEduInputContent.setDataUnit("");
        try {
            healthEduInputContent.setDataTime(mCurrentTime.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SummaryDataModel> summaryDataModelList = mAssessParam.getDocSummaryDataPOJOList();
        if (summaryDataModelList == null)
            summaryDataModelList = new ArrayList<>();
        summaryDataModelList.add(healthEduInputContent);
        //设置健康教育选中的教育项目
        mAssessParam.setSelectedItemList(mSelectedItemList);
        mPresenter.commitHealthEduContent(mAssessParam);
    }

    /**
     * 操作完毕返回宣教内容页面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //医嘱导入
        if (requestCode == CommonConstant.REQUEST_CODE_SELECT_ORDERS && resultCode == CommonConstant.RESULT_CODE_SELECT_ORDERS) {
            //TODO 医嘱导入
            List<Order> orderList = (List<Order>) data.getSerializableExtra(KeyConstant.NAME_EXTRA_ORDER_LIST);
            importOrderList(orderList);
        }
    }

    /**
     * 导入医嘱列表
     *
     * @param orderList
     */
    private void importOrderList(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty())
            return;
        String orderListText = OrdersHelp.buildOrderTextByOrderList(orderList);
        if (TextUtils.isEmpty(orderListText))
            return;
        String eduContent = mEt_inputContent.getText().toString();
        eduContent = eduContent + "\n" + orderListText;
        mEt_inputContent.setText(eduContent);
    }

    /**
     * 显示健康教育结果列表
     *
     * @param assessModels
     */
    @Override
    public void showHealthEduResult(HealthEduAssessModel assessModels) {
        List<AssessViewItem> itemList = assessModels.getViewList();//获取页面样式列表
        List<SummaryDataModel> dataList = assessModels.getDataList();//获取页面数据列表
        List<MultiListMenuItem> selectedItemList = assessModels.getSelectedItemList();//获取健康宣教项目列表
        AssessHelper.resetAssessParam(mAssessParam, mSickbed, dataList);//重置、构建要传入服务器端的数据
        //显示宣教记录的数据
        mLv_result.show(AssessHelper.buildAssessViewItemListBySummaryData(itemList, dataList), mCurrentTime);
        String inputContent = "";
        String selectedItemName = "";
        if (mOperationType == CommonConstant.OPERATION_TYPE_UPDATE) {//当前页面是更新数据，则要根据服务器返回的数据，显示教育内容页面
            inputContent = getHealthEduInputContent(dataList);//填写输入框中的数据
            mSelectedItemList.clear();
            if (selectedItemList != null)
                mSelectedItemList.addAll(selectedItemList);
        } else {//当前页面是新增数据，则直接显示上个页面带入的数据即可
            inputContent = MultiListMenuItem.buildMultiLevelItemContent(mSelectedItemList);
        }
        showHealthEduInputContent(inputContent);
        selectedItemName = MultiListMenuItem.buildMultiLevelItemName(mSelectedItemList);
        showHealEduSelectedItem(selectedItemName);
    }

    /**
     * 展示健康宣教选中的项目
     *
     * @param selectedItem
     */
    private void showHealEduSelectedItem(String selectedItem) {
        if (mTv_selectItem == null)
            return;
        //展示选中的宣教项
        if (!TextUtils.isEmpty(selectedItem)) {
            mTv_selectItem.setText(selectedItem);
            mTv_selectItem.setVisibility(View.VISIBLE);
        } else {
            mTv_selectItem.setVisibility(View.GONE);
        }
    }

    /**
     * 展示健康宣教中，输入框显示的内容
     */
    private void showHealthEduInputContent(String content) {
        if (mEt_inputContent == null)
            return;
        //展示宣教内容
        if (!TextUtils.isEmpty(content)) {
            mEt_inputContent.setText(content);
        } else {
            mEt_inputContent.setText("");
        }
    }

    /**
     * 从SummaryDataModel数据列表中，获取健康教育输入框中的数据
     *
     * @param dataList
     * @return
     */
    private String getHealthEduInputContent(List<SummaryDataModel> dataList) {
        if (dataList == null || dataList.isEmpty())
            return "";
        for (SummaryDataModel dataModel : dataList) {
            if (dataModel.getDataName().equals(INPUT_HEALTH_CONTENT_CODE)) {
                return dataModel.getDataValue();
            }
        }
        return "";
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = createLoadingDialog();
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }

    @Override
    public void showError(@NonNull String message) {
        showErrorToast(message);
    }

    @Override
    public void showMessage(@NonNull String message) {
        showToast(message);
    }
}

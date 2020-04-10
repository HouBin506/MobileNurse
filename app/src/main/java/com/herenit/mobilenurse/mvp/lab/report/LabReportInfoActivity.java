package com.herenit.mobilenurse.mvp.lab.report;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.herenit.arms.mvp.IView;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.Interaction.js.CommonJsInterface;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.api.ApiManager;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;
import com.herenit.mobilenurse.criteria.constant.KeyConstant;
import com.herenit.mobilenurse.criteria.entity.lab.CommonLabReport;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.lab.LabReport;
import com.herenit.mobilenurse.criteria.entity.lab.MicroorganismLabReport;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.mvp.base.BasicWebViewActivity;
import com.herenit.mobilenurse.mvp.tool.FullScreenWebViewActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/8/28 14:04
 * desc: 检验报告详情页面，显示检验详细数据
 */
public class LabReportInfoActivity extends BasicWebViewActivity implements IView {

    @BindView(R2.id.srl_labReportInfo_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private Sickbed mSickbed;
    private LabReport mLabReport;

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_lab_report_info;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            mSickbed = (Sickbed) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_SICKBED);
            mLabReport = (LabReport) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_LAB_REPORT);
            if (mSickbed != null)
                setTitleBarRightText1(mSickbed.getPatientTitle());
            if (mLabReport != null)
                loadWebLabReport();
        }

    }

    /**
     * 加载网页检验报告
     */
    private void loadWebLabReport() {
        if (mLabReport instanceof CommonLabReport) {//普通检验报告
            load(ApiManager.getLabReportPagerUrl(mLabReport.getLabNo()), CommonConstant.MEDIA_TYPE_URL);//TODO 加载检验报告网页
        } else if (mLabReport instanceof MicroorganismLabReport) {//微生物检验
            load(ApiManager.getMicroorganismLabReportPagerUrl(mLabReport.getLabNo()), CommonConstant.MEDIA_TYPE_URL);//TODO 加载检验报告网页
        }
    }

    /**
     * 页面加载完成
     *
     * @param view
     * @param url
     */
    @Override
    protected void onPageLoadFinished(WebView view, String url) {
        mRefreshLayout.finishRefresh();
    }

    private void initView() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mLabReport != null)
                    loadWebLabReport();//TODO 重新加载检验报告页面
            }
        });
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {//返回按钮
            @Override
            public void onClick(View v) {
                LabReportInfoActivity.super.onBackPressed();
            }
        });
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_module_labReport));
        addJavascriptInterface(new CommonJsInterface(new CommonJsInterface.CommonJsInterfaceListener() {
            //打开全屏WebView加载html
            @Override
            public void openFullWebView(String html, String mediaType) {
                Intent intent = new Intent(mContext, FullScreenWebViewActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_HTML, html);
                intent.putExtra(KeyConstant.NAME_EXTRA_MEDIA_TYPE, mediaType);
                ArmsUtils.startActivity(intent);
            }
        }), KeyConstant.NAME_JS_INTERFACE_COMMON);
    }
}

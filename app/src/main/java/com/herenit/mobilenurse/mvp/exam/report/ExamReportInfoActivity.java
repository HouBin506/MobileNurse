package com.herenit.mobilenurse.mvp.exam.report;

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
import com.herenit.mobilenurse.criteria.entity.ExamReport;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.mvp.base.BasicWebViewActivity;
import com.herenit.mobilenurse.mvp.tool.FullScreenWebViewActivity;
import com.herenit.mobilenurse.mvp.tool.PDFViewActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/8/28 14:04
 * desc: 检查报告详情页面，显示某一项检查的结果、报告
 */
public class ExamReportInfoActivity extends BasicWebViewActivity implements IView {

    @BindView(R2.id.srl_examReportInfo_refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private Sickbed mSickbed;
    private ExamReport mExamReport;

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_TV;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_exam_report_info;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            mSickbed = (Sickbed) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_SICKBED);
            mExamReport = (ExamReport) intent.getSerializableExtra(KeyConstant.NAME_EXTRA_EXAM_REPORT);
            if (mSickbed != null)
                setTitleBarRightText1(mSickbed.getPatientTitle());
            if (mExamReport != null)
                load(ApiManager.getExamReportPagerUrl(mExamReport.getExamNo()), CommonConstant.MEDIA_TYPE_URL);
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
                if (mExamReport != null)
                    load(ApiManager.getExamReportPagerUrl(mExamReport.getExamNo()), CommonConstant.MEDIA_TYPE_URL);
            }
        });
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {//返回按钮
            @Override
            public void onClick(View v) {
                ExamReportInfoActivity.super.onBackPressed();
            }
        });
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_module_examReport));

        addJavascriptInterface(new CommonJsInterface(new CommonJsInterface.CommonJsInterfaceListener() {
            @Override
            public void openPDF(String url) {//打开pdf
                //TODO 本地加载服务器的PDF文件
                Intent intent = new Intent(mContext, PDFViewActivity.class);
                intent.putExtra(KeyConstant.NAME_EXTRA_URL, ApiManager.baseServiceAddress() + url);
                intent.putExtra(KeyConstant.NAME_EXTRA_COMMON_BOOLEAN, true);//是否来源于服务器
                ArmsUtils.startActivity(intent);
            }

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

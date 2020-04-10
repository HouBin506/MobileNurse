package com.herenit.mobilenurse.mvp.assess;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.entity.SummaryDataModel;
import com.herenit.mobilenurse.criteria.entity.submit.AssessParam;
import com.herenit.mobilenurse.criteria.entity.view.AssessViewItem;
import com.herenit.mobilenurse.datastore.tempcache.UserTemp;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.herenit.mobilenurse.criteria.entity.view.AssessViewItem.MODEL_TYPE_SUBVALUE;
import static com.herenit.mobilenurse.criteria.entity.view.AssessViewItem.MODEL_TYPE_VALUE;

/**
 * author: HouBin
 * date: 2019/9/25 15:47
 * desc:评估帮助类
 */
public class AssessHelper {
    private AssessHelper() {
    }


    /**
     * 根据评估数据，构建评估界面所需的数据
     *
     * @param viewList 评估界面样式
     * @param dataList 评估数据
     * @return
     */
    public static List<AssessViewItem> buildAssessViewItemListBySummaryData(List<AssessViewItem> viewList, List<SummaryDataModel> dataList) {
        if (dataList != null && dataList.size() > 0 && viewList != null && !viewList.isEmpty()) {//给UI数据赋值
            Map<String, SummaryDataModel> summaryDataModelMap = new HashMap<>();
            for (SummaryDataModel summaryDataModel : dataList) {
                summaryDataModelMap.put(summaryDataModel.getDataName(), summaryDataModel);
            }
            assignSummaryDataToAssessData(viewList, summaryDataModelMap);
        }
        return viewList;
    }

    /**
     * 将数据库中SummaryData的值赋值给UI数据AssessModel，用于界面显示
     *
     * @param assessModelList
     * @param summaryDataModelMap
     * @return
     */
    private static List<AssessViewItem> assignSummaryDataToAssessData(List<AssessViewItem> assessModelList, Map<String, SummaryDataModel> summaryDataModelMap) {
        if (assessModelList == null)
            return assessModelList;
        for (AssessViewItem model : assessModelList) {
            SummaryDataModel summaryDataModel = summaryDataModelMap.get(model.getId());
            //只给Value和SubValue赋值
            if (summaryDataModel != null && (MODEL_TYPE_VALUE.equals(model.getModelType()) || MODEL_TYPE_SUBVALUE.equals(model.getModelType()))) {
                model.setDataValue(summaryDataModel.getDataValue());
                if (AssessViewItem.DATA_VALUE_TRUE.equals(summaryDataModel.getDataValue())) {
                    model.setChecked(true);
                    model.setShowDataValue(model.getText());
                } else {
                    model.setShowDataValue(model.parseShowDataValue());
                }
            }
            if (model.getSubModel() != null)
                assignSummaryDataToAssessData(model.getSubModel(), summaryDataModelMap);
            model.buildItemValue();
        }
        return assessModelList;
    }

    /**
     * 根据用户的操作结果，构建要传入服务器的数据
     *
     * @param viewList   操作的界面列表
     * @param recordTime 记录时间
     */
    public static<T extends AssessParam> void buildAssessParamByAssessViewList(@NonNull T assessParam, List<AssessViewItem> viewList, @NonNull Date recordTime) {
        Map<String, SummaryDataModel> assessResultMap = new HashMap<>();
        buildSummaryData(assessResultMap, viewList, recordTime);
        if (assessResultMap == null || assessResultMap.size() == 0)
            return;
        List<SummaryDataModel> result = new ArrayList<>();
        for (Map.Entry<String, SummaryDataModel> entry : assessResultMap.entrySet()) {
            SummaryDataModel summaryDataModel = entry.getValue();
            result.add(summaryDataModel);
        }
        if (result.size() <= 0)
            return;
        try {
            assessParam.setRecordTime(recordTime.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assessParam.setDocSummaryDataPOJOList(result);
    }

    /**
     * 构建数据列表，用于提交到服务器端（评估完成，数据保存的准备工作）
     *
     * @param modelList
     * @return
     */
    private static void buildSummaryData(Map<String, SummaryDataModel> assessResultMap, List<AssessViewItem> modelList, @NonNull Date recordTime) {
        if (modelList == null || modelList.size() == 0)
            return;
        for (AssessViewItem model : modelList) {
            if (!TextUtils.isEmpty(model.getDataValue()) && !TextUtils.isEmpty(model.getId()) &&
                    (MODEL_TYPE_VALUE.equals(model.getModelType()) || MODEL_TYPE_SUBVALUE.equals(model.getModelType()))) {
                SummaryDataModel summaryData = new SummaryDataModel();
                summaryData.setDataName(model.getId());
                summaryData.setDataValue(model.getDataValue());
                summaryData.setDataType(model.getDataType());
                summaryData.setDataUnit(model.getUnit());
                try {
                    summaryData.setDataTime(recordTime.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                assessResultMap.put(model.getId(), summaryData);
            }
            if (model.getSubModel() != null)
                buildSummaryData(assessResultMap, model.getSubModel(), recordTime);
        }
    }


    /**
     * 创建评估数据参数，提交服务器要用
     *
     * @param param    要返回的评估参数
     * @param sickbed  当前患者
     * @param dataList 当前数据列表
     * @return
     */
    public static <T extends AssessParam> void resetAssessParam(@NotNull T param, Sickbed sickbed, List<SummaryDataModel> dataList) {
        if (param == null)
            return;
        param.clear();
        param.setPatientId(sickbed.getPatientId());
        param.setPatientName(sickbed.getPatientName());
        try {
            param.setVisitTime(sickbed.getVisitTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        param.setUserId(UserTemp.getInstance().getUserId());
        param.setUserName(UserTemp.getInstance().getUserName());
        param.setVisitId(sickbed.getVisitId());
        param.setWardCode(UserTemp.getInstance().getGroupCode());
        param.setWardName(UserTemp.getInstance().getGroupName());
        if (dataList != null && !dataList.isEmpty()) {
            SummaryDataModel summaryDataModel = dataList.get(0);
            param.setRecordId(summaryDataModel.getRecordId());
            param.setDocTypeId(summaryDataModel.getDocTypeId());
            param.setDocId(summaryDataModel.getDocId());
            param.setSubId(summaryDataModel.getSubId());
        }
    }
}

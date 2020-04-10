package com.herenit.mobilenurse.custom.adapter;

import android.content.Context;

import com.herenit.arms.base.adapter.rv.CommonAdapter;
import com.herenit.arms.base.adapter.rv.ViewHolder;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.utils.ViewUtils;
import com.herenit.mobilenurse.criteria.entity.Sickbed;
import com.herenit.mobilenurse.criteria.enums.SickbedFlagEnum;
import com.herenit.mobilenurse.datastore.tempcache.DictTemp;

import java.util.List;
import java.util.Map;

/**
 * author: HouBin
 * date: 2019/2/26 10:22
 * desc: 床位列表的Adapter
 */
public class SickbedAdapter extends CommonAdapter<Sickbed> {

    public SickbedAdapter(Context context, List<Sickbed> datas) {
        super(context, R.layout.item_sickbed, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Sickbed sickbed, int position) {
        holder.setText(R.id.tv_item_sickbed_bedLabel, sickbed.getBedLabel());//床标号
        holder.setText(R.id.tv_item_sickbed_patientName, sickbed.getPatientName());//姓名
        holder.setText(R.id.tv_item_sickbed_sex, sickbed.getPatientSex());//性别
        //病情等级
        String patientCondition = DictTemp.getInstance().getPatientConditionNameByCode(sickbed.getPatientCondition());
        if (SickbedFlagEnum.isDangerCondition(patientCondition)) {//病危
            holder.setVisible(R.id.img_item_sickbed_patientCondition, true);
            holder.setImageResource(R.id.img_item_sickbed_patientCondition, R.mipmap.ic_bed_flag_danger);
        } else if (SickbedFlagEnum.isIntensiveCondition(patientCondition)) {
            holder.setVisible(R.id.img_item_sickbed_patientCondition, true);
            holder.setImageResource(R.id.img_item_sickbed_patientCondition, R.mipmap.ic_bed_flag_intensive);
        } else {
            holder.setVisible(R.id.img_item_sickbed_patientCondition, false);
        }

        String nursingClass = DictTemp.getInstance().getNursingClassNameByCode(sickbed.getNursingClass());

        holder.setText(R.id.tv_item_sickbed_nursingClass, nursingClass);
        holder.setTextColor(R.id.tv_item_sickbed_nursingClass, SickbedFlagEnum.getNursingClassTextColorByName(nursingClass));
        holder.setTextColor(R.id.tv_item_sickbed_bedLabel, SickbedFlagEnum.getNursingClassTextColorByName(nursingClass));
        //护理等级
        holder.setBackgroundColor(R.id.ll_item_sickbed_nursingClassBg, SickbedFlagEnum.getNursingClassColorByName(nursingClass));

        if (SickbedFlagEnum.isIsolationSign(sickbed.getIsolationSign())) {//隔离
            holder.setVisible(R.id.img_item_sickbed_isolation, true);
        } else {
            holder.setVisible(R.id.img_item_sickbed_isolation, false);
        }
        if (SickbedFlagEnum.isFastingSign(sickbed.getFastingSign())) {//禁食
            holder.setVisible(R.id.img_item_sickbed_fasting, true);
        } else {
            holder.setVisible(R.id.img_item_sickbed_fasting, false);
        }
        /**
         * 是否绑定监护仪
         */
        holder.setVisible(R.id.img_item_sickbed_bindMonitor, sickbed.getIsBindMonitor());

    }

    @Override
    protected void convert(ViewHolder holder, Map<String, Object> stringObjectMap, int position) {

    }
}

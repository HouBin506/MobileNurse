package com.herenit.mobilenurse.criteria.enums;

/**
 * author: HouBin
 * date: 2019/1/31 16:46
 * desc: 标题栏类型
 */
public enum TitleBarTypeEnum {
    NONE,//不使用标题栏
    TV_TV_TV,//左、中、右：TextView、TextView、TextView
    IMG_TV_TV,//左、中、右：ImageView、TextView、TextView
    IMG_TV_TVTV,//左、中、右：ImageView、TextView、TextView+TextView
    IMG_TV_IMG,//左、中、右：ImageView、TextView、ImageView
    IMG_TV_IMGTV,//左、中、右：ImageView、TextView、ImageView+TextView
    IMG_TV_IMGIMG,//左、中、右：ImageView、TextView、ImageView+ImageView
    TV_NULL_TV,//左、中、右：TextView、无、TextView
    TV_NULL_TVIMGTV,//左、中、右：TextView、无、TextView+ImageView+TextView
    IMG_TV_NULL,//左、中、右：ImageView、TextView、无
    TV_TV_TVIMG,//左、中、右：TextView、TextView、TextView+ImageView
    TV_TV_IMG,//左、中、右：TextView、TextView、ImageView
    IMG_TV_TVIMG;//左、中、右：ImageView、TextView、TextView+ImageView
}

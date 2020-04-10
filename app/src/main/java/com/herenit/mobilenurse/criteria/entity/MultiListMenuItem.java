package com.herenit.mobilenurse.criteria.entity;

import android.text.TextUtils;

import com.herenit.mobilenurse.criteria.common.CommonNameCode;
import com.herenit.mobilenurse.criteria.common.NameCode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.greenrobot.greendao.annotation.Generated;

/**
 * author: HouBin
 * date: 2019/9/10 10:03
 * desc:多级列表实体类
 */
@Entity(indexes = {@Index(value = "docType DESC,code DESC", unique = true)})
public class MultiListMenuItem implements Cloneable, Serializable {
    private static final long serialVersionUID = 3374712829086681820L;

    @Id
    private Long _id;

    //文档类型
    @NotNull
    private String docType;

    protected String name;
    @NotNull
    protected String code;

    //层级关系（列表专用）
    @NotNull
    private String path;
    @Transient
    private boolean checked;
    //该项目附带的内容
    private String content;
    //是否含有子项
    private boolean hasSubItem;
    //是否可选择
    private boolean selectable;

    @Generated(hash = 1910658486)
    public MultiListMenuItem(Long _id, @NotNull String docType, String name, @NotNull String code, @NotNull String path,
            String content, boolean hasSubItem, boolean selectable) {
        this._id = _id;
        this.docType = docType;
        this.name = name;
        this.code = code;
        this.path = path;
        this.content = content;
        this.hasSubItem = hasSubItem;
        this.selectable = selectable;
    }

    @Generated(hash = 40551994)
    public MultiListMenuItem() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isHasSubItem() {
        return hasSubItem;
    }

    public void setHasSubItem(boolean hasSubItem) {
        this.hasSubItem = hasSubItem;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取最大层级数
     *
     * @return
     */
    public static int getMaxLevelNum(List<MultiListMenuItem> itemList) {
        int maxLevel = 0;
        if (itemList == null || itemList.isEmpty())
            return maxLevel;
        for (MultiListMenuItem item : itemList) {
            if (TextUtils.isEmpty(item.getPath()))
                continue;
            String[] level = item.getPath().split("/");
            if (level.length > maxLevel)
                maxLevel = level.length;
        }
        return maxLevel;
    }

    /**
     * 构建多级列表的项目名称字符串
     *
     * @param itemList 多级列表的部分ItemList、
     * @return
     */
    public static String buildMultiLevelItemName(List<MultiListMenuItem> itemList) {
        if (itemList == null || itemList.isEmpty())
            return null;
        LinkedHashMap<String, List<MultiListMenuItem>> mainPathItemMap = buildMainPathItemMap(itemList);
        if (mainPathItemMap == null || mainPathItemMap.isEmpty())
            return null;
        StringBuilder nameBuilder = new StringBuilder();
        for (Map.Entry<String, List<MultiListMenuItem>> entry : mainPathItemMap.entrySet()) {
            if (!TextUtils.isEmpty(nameBuilder))
                nameBuilder.append("；");
            List<MultiListMenuItem> mainPathItemList = entry.getValue();
            for (int x = 0; x < mainPathItemList.size(); x++) {
                MultiListMenuItem item = mainPathItemList.get(x);
                nameBuilder.append(item.getName());
                if (x != mainPathItemList.size() - 1)
                    nameBuilder.append("，");
            }
        }
        nameBuilder.append("。");
        return nameBuilder.toString();
    }

    /**
     * 构建多级列表的项目附带内容字符串
     *
     * @param itemList 多级列表的部分ItemList、
     * @return
     */
    public static String buildMultiLevelItemContent(List<MultiListMenuItem> itemList) {
        if (itemList == null || itemList.isEmpty())
            return null;
        LinkedHashMap<String, List<MultiListMenuItem>> mainPathItemMap = buildMainPathItemMap(itemList);
        if (mainPathItemMap == null || mainPathItemMap.isEmpty())
            return null;
        StringBuilder contentBuilder = new StringBuilder();
        for (Map.Entry<String, List<MultiListMenuItem>> entry : mainPathItemMap.entrySet()) {
            if (!TextUtils.isEmpty(contentBuilder))
                contentBuilder.append("；");
            List<MultiListMenuItem> mainPathItemList = entry.getValue();
            for (int x = 0; x < mainPathItemList.size(); x++) {
                MultiListMenuItem item = mainPathItemList.get(x);
                String content = item.getContent();
                if (TextUtils.isEmpty(content))
                    continue;
                contentBuilder.append(item.getContent());
                if (x != mainPathItemList.size() - 1)
                    contentBuilder.append("，");
            }
        }
        contentBuilder.append("。");
        return contentBuilder.toString();
    }

    /**
     * 根据多及列表的Item集合，按照顺序总结、构建出每一条主线上的元素
     *
     * @param itemList
     * @return
     */
    public static LinkedHashMap<String, List<MultiListMenuItem>> buildMainPathItemMap(List<MultiListMenuItem> itemList) {
        if (itemList == null || itemList.isEmpty())
            return null;
        List<MultiListMenuItem> itemListCopy = new ArrayList<>();
        for (MultiListMenuItem copy : itemList) {
            itemListCopy.add(copy.clone());
        }
        //以一级路径为主线，获取所有主线
        List<String> pathList = new ArrayList<>();
        for (MultiListMenuItem item : itemList) {
            String path = item.getPath();
            if (TextUtils.isEmpty(path))
                continue;
            String mainPath = path.split("/")[0];
            if (!pathList.contains(mainPath))
                pathList.add(mainPath);
        }
        LinkedHashMap<String, List<MultiListMenuItem>> pathItemMap = new LinkedHashMap<>();
        for (String mainPath : pathList) {//遍历主线，找到主线下的所有子项，并按顺序排好
            Iterator<MultiListMenuItem> iterator = itemListCopy.iterator();
            while (iterator.hasNext()) {//为了节省性能，遍历到需要的，添加完成之后要删除
                MultiListMenuItem item = iterator.next();
                String path = item.getPath();
                //与当前主线不匹配，遍历下一项
                if (TextUtils.isEmpty(path) || !path.startsWith(mainPath))
                    continue;
                List<MultiListMenuItem> pathItemList = pathItemMap.get(mainPath);
                if (pathItemList == null) {//该条主线为空，则直接添加即可
                    pathItemList = new ArrayList<>();
                    pathItemList.add(item);
                    pathItemMap.put(mainPath, pathItemList);
                } else {
                    insertItemToMainPath(pathItemList, item);
                }
                iterator.remove();
            }
        }
        return pathItemMap;
    }

    /**
     * 将某一项插入到某一条主线下的列表中。根据顺序插入
     *
     * @param pathItemList 主线列表 顺序为 [“一级x”,“二级A（一级x子项）”,"三级1（二级A子项）",,"三级2（二级A子项）",“二级B”,"三级3（二级B子项）",,"三级4（二级B子项）",]
     * @param item         要插入的条目
     */
    private static void insertItemToMainPath(List<MultiListMenuItem> pathItemList, MultiListMenuItem item) {
        if (pathItemList == null)
            return;
        //如果该主线是空的列表，则直接添加进去
        if (pathItemList.isEmpty()) {
            pathItemList.add(item);
            return;
        }
        String path = item.getPath();
        if (TextUtils.isEmpty(path))
            return;
        String[] pathArr = path.split("/");
        String[] descPathArr;
        if (pathItemList.size() == 1) {//如果该主线只有一项则根据路径判断添加
            String descPath = pathItemList.get(0).getPath();
            if (TextUtils.isEmpty(descPath)) {
                pathItemList.add(item);
                return;
            }
            descPathArr = descPath.split("/");
            if (descPathArr.length <= pathArr.length)
                pathItemList.add(item);
            else
                pathItemList.add(0, item);
            return;
        }
        int index = -1;
        for (int x = 0; x < pathItemList.size(); x++) {
            index = -1;//默认角标为-1
            MultiListMenuItem pathItem = pathItemList.get(x);
            String descPath = pathItem.getPath();
            if (TextUtils.isEmpty(descPath))
                continue;
            descPathArr = descPath.split("/");
            if (pathArr.length < descPathArr.length) {//遍历到的Path层级大于要插入的Item的层级
                if (descPath.startsWith(path)) {
                    index = x - 1;
                    break;
                } else {
                    continue;
                }
            } else if (descPathArr.length == pathArr.length) {//同级
                if (descPath.equals(path))
                    return;
                if (descPathArr.length == 1)//长度为1时无法比较，继续遍历
                    continue;
                if (path.substring(0, path.length() - pathArr[pathArr.length - 1].length()).
                        equals(descPath.substring(0, descPath.length() - descPathArr[descPathArr.length - 1].length())))//同级同组添加到后面
                    index = x + 1;
                break;
            } else {//遍历到的Path层级小于于要插入的Item的层级
                if (x == pathItemList.size() - 1) //恰好遍历到了最后
                    break;
                //继续往后遍历找到他的组
                continue;
            }
        }
        if (index < 0)
            pathItemList.add(item);
        else
            pathItemList.add(index, item);
    }

    /**
     * 获取一级目录下的列表
     *
     * @return
     */
    public static List<MultiListMenuItem> getLevel1List(List<MultiListMenuItem> itemList) {
        if (itemList == null)
            return null;
        List<MultiListMenuItem> level1List = new ArrayList<>();
        for (MultiListMenuItem item : itemList) {
            if (!item.getPath().contains("/")) {
                level1List.add(item);
            }
        }
        return level1List;
    }

    /**
     * 获取下一级列表
     *
     * @param path 当前级的路径
     * @return
     */
    public static List<MultiListMenuItem> getNextLevelList(String path, List<MultiListMenuItem> itemList) {
        if (itemList == null || TextUtils.isEmpty(path))
            return null;
        List<MultiListMenuItem> childList = new ArrayList<>();
        for (MultiListMenuItem item : itemList) {
            if (item.getPath().startsWith(path) && !item.getPath().equals(path)) {//path下面的
                String[] parentLevel = path.split("/");
                String[] childLevel = item.getPath().split("/");
                if (parentLevel.length + 1 == childLevel.length) {//层级数比Path大一，就是我们要找的下一级
                    childList.add(item);
                }
            }
        }
        return childList;
    }

    @Override
    public MultiListMenuItem clone() {
        try {
            return (MultiListMenuItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getHasSubItem() {
        return this.hasSubItem;
    }

    public boolean getSelectable() {
        return this.selectable;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}

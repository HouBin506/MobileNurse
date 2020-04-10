package com.herenit.mobilenurse.datastore.orm.greendao.daopackage;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.herenit.mobilenurse.criteria.entity.User.GroupListStringConvert;
import com.herenit.mobilenurse.criteria.entity.User.MnUserVPOJOBean;
import com.herenit.mobilenurse.criteria.entity.User.UserStringConvert;
import java.util.List;

import com.herenit.mobilenurse.criteria.entity.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Void> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, String.class, "userId", false, "USER_ID");
        public final static Property MnUserVPOJO = new Property(1, String.class, "mnUserVPOJO", false, "MN_USER_VPOJO");
        public final static Property MnUserVsGroupVPOJOList = new Property(2, String.class, "mnUserVsGroupVPOJOList", false, "MN_USER_VS_GROUP_VPOJOLIST");
    }

    private final UserStringConvert mnUserVPOJOConverter = new UserStringConvert();
    private final GroupListStringConvert mnUserVsGroupVPOJOListConverter = new GroupListStringConvert();

    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"USER_ID\" TEXT," + // 0: userId
                "\"MN_USER_VPOJO\" TEXT," + // 1: mnUserVPOJO
                "\"MN_USER_VS_GROUP_VPOJOLIST\" TEXT);"); // 2: mnUserVsGroupVPOJOList
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_USER_USER_ID_DESC ON \"USER\"" +
                " (\"USER_ID\" DESC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        MnUserVPOJOBean mnUserVPOJO = entity.getMnUserVPOJO();
        if (mnUserVPOJO != null) {
            stmt.bindString(2, mnUserVPOJOConverter.convertToDatabaseValue(mnUserVPOJO));
        }
 
        List mnUserVsGroupVPOJOList = entity.getMnUserVsGroupVPOJOList();
        if (mnUserVsGroupVPOJOList != null) {
            stmt.bindString(3, mnUserVsGroupVPOJOListConverter.convertToDatabaseValue(mnUserVsGroupVPOJOList));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        MnUserVPOJOBean mnUserVPOJO = entity.getMnUserVPOJO();
        if (mnUserVPOJO != null) {
            stmt.bindString(2, mnUserVPOJOConverter.convertToDatabaseValue(mnUserVPOJO));
        }
 
        List mnUserVsGroupVPOJOList = entity.getMnUserVsGroupVPOJOList();
        if (mnUserVsGroupVPOJOList != null) {
            stmt.bindString(3, mnUserVsGroupVPOJOListConverter.convertToDatabaseValue(mnUserVsGroupVPOJOList));
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : mnUserVPOJOConverter.convertToEntityProperty(cursor.getString(offset + 1)), // mnUserVPOJO
            cursor.isNull(offset + 2) ? null : mnUserVsGroupVPOJOListConverter.convertToEntityProperty(cursor.getString(offset + 2)) // mnUserVsGroupVPOJOList
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setMnUserVPOJO(cursor.isNull(offset + 1) ? null : mnUserVPOJOConverter.convertToEntityProperty(cursor.getString(offset + 1)));
        entity.setMnUserVsGroupVPOJOList(cursor.isNull(offset + 2) ? null : mnUserVsGroupVPOJOListConverter.convertToEntityProperty(cursor.getString(offset + 2)));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(User entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(User entity) {
        return null;
    }

    @Override
    public boolean hasKey(User entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
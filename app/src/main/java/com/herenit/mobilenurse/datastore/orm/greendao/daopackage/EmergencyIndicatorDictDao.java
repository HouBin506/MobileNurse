package com.herenit.mobilenurse.datastore.orm.greendao.daopackage;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.herenit.mobilenurse.criteria.entity.dict.EmergencyIndicatorDict;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EMERGENCY_INDICATOR_DICT".
*/
public class EmergencyIndicatorDictDao extends AbstractDao<EmergencyIndicatorDict, Long> {

    public static final String TABLENAME = "EMERGENCY_INDICATOR_DICT";

    /**
     * Properties of entity EmergencyIndicatorDict.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property EmergencyIndicatorName = new Property(1, String.class, "emergencyIndicatorName", false, "EMERGENCY_INDICATOR_NAME");
        public final static Property EmergencyIndicatorCode = new Property(2, String.class, "emergencyIndicatorCode", false, "EMERGENCY_INDICATOR_CODE");
    }


    public EmergencyIndicatorDictDao(DaoConfig config) {
        super(config);
    }
    
    public EmergencyIndicatorDictDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EMERGENCY_INDICATOR_DICT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"EMERGENCY_INDICATOR_NAME\" TEXT," + // 1: emergencyIndicatorName
                "\"EMERGENCY_INDICATOR_CODE\" TEXT);"); // 2: emergencyIndicatorCode
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EMERGENCY_INDICATOR_DICT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EmergencyIndicatorDict entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String emergencyIndicatorName = entity.getEmergencyIndicatorName();
        if (emergencyIndicatorName != null) {
            stmt.bindString(2, emergencyIndicatorName);
        }
 
        String emergencyIndicatorCode = entity.getEmergencyIndicatorCode();
        if (emergencyIndicatorCode != null) {
            stmt.bindString(3, emergencyIndicatorCode);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EmergencyIndicatorDict entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String emergencyIndicatorName = entity.getEmergencyIndicatorName();
        if (emergencyIndicatorName != null) {
            stmt.bindString(2, emergencyIndicatorName);
        }
 
        String emergencyIndicatorCode = entity.getEmergencyIndicatorCode();
        if (emergencyIndicatorCode != null) {
            stmt.bindString(3, emergencyIndicatorCode);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public EmergencyIndicatorDict readEntity(Cursor cursor, int offset) {
        EmergencyIndicatorDict entity = new EmergencyIndicatorDict( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // emergencyIndicatorName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // emergencyIndicatorCode
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EmergencyIndicatorDict entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEmergencyIndicatorName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEmergencyIndicatorCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(EmergencyIndicatorDict entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(EmergencyIndicatorDict entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EmergencyIndicatorDict entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

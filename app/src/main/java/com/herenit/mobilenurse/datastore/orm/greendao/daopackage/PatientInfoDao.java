package com.herenit.mobilenurse.datastore.orm.greendao.daopackage;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.herenit.mobilenurse.criteria.entity.PatientInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PATIENT_INFO".
*/
public class PatientInfoDao extends AbstractDao<PatientInfo, Long> {

    public static final String TABLENAME = "PATIENT_INFO";

    /**
     * Properties of entity PatientInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DeptCode = new Property(1, String.class, "deptCode", false, "DEPT_CODE");
        public final static Property BedNo = new Property(2, int.class, "bedNo", false, "BED_NO");
        public final static Property BedLabel = new Property(3, String.class, "bedLabel", false, "BED_LABEL");
        public final static Property BedStatus = new Property(4, String.class, "bedStatus", false, "BED_STATUS");
        public final static Property BabyNo = new Property(5, String.class, "babyNo", false, "BABY_NO");
        public final static Property PatientId = new Property(6, String.class, "patientId", false, "PATIENT_ID");
        public final static Property VisitId = new Property(7, String.class, "visitId", false, "VISIT_ID");
        public final static Property PatientName = new Property(8, String.class, "patientName", false, "PATIENT_NAME");
        public final static Property PatientAge = new Property(9, String.class, "patientAge", false, "PATIENT_AGE");
        public final static Property PatientSex = new Property(10, String.class, "patientSex", false, "PATIENT_SEX");
        public final static Property PatientCondition = new Property(11, String.class, "patientCondition", false, "PATIENT_CONDITION");
        public final static Property Diagnosis = new Property(12, String.class, "diagnosis", false, "DIAGNOSIS");
        public final static Property FastingSign = new Property(13, String.class, "fastingSign", false, "FASTING_SIGN");
        public final static Property IsolationSign = new Property(14, String.class, "isolationSign", false, "ISOLATION_SIGN");
        public final static Property NursingClass = new Property(15, String.class, "nursingClass", false, "NURSING_CLASS");
        public final static Property WardCode = new Property(16, String.class, "wardCode", false, "WARD_CODE");
        public final static Property WardName = new Property(17, String.class, "wardName", false, "WARD_NAME");
        public final static Property DeptName = new Property(18, String.class, "deptName", false, "DEPT_NAME");
        public final static Property InpNo = new Property(19, String.class, "inpNo", false, "INP_NO");
        public final static Property VisitTime = new Property(20, long.class, "visitTime", false, "VISIT_TIME");
        public final static Property AdmWardTime = new Property(21, long.class, "admWardTime", false, "ADM_WARD_TIME");
        public final static Property AdmissionDateCount = new Property(22, int.class, "admissionDateCount", false, "ADMISSION_DATE_COUNT");
        public final static Property VisitNo = new Property(23, String.class, "visitNo", false, "VISIT_NO");
        public final static Property IdNo = new Property(24, String.class, "idNo", false, "ID_NO");
        public final static Property PatientClass = new Property(25, String.class, "patientClass", false, "PATIENT_CLASS");
        public final static Property PatientIdentity = new Property(26, String.class, "patientIdentity", false, "PATIENT_IDENTITY");
        public final static Property PhoneNumber = new Property(27, String.class, "phoneNumber", false, "PHONE_NUMBER");
        public final static Property Nation = new Property(28, String.class, "nation", false, "NATION");
        public final static Property BirthTime = new Property(29, long.class, "birthTime", false, "BIRTH_TIME");
        public final static Property BirthPlaceCode = new Property(30, String.class, "birthPlaceCode", false, "BIRTH_PLACE_CODE");
        public final static Property BirthPlaceName = new Property(31, String.class, "birthPlaceName", false, "BIRTH_PLACE_NAME");
        public final static Property PresentAddressCode = new Property(32, String.class, "presentAddressCode", false, "PRESENT_ADDRESS_CODE");
        public final static Property PresentAddressName = new Property(33, String.class, "presentAddressName", false, "PRESENT_ADDRESS_NAME");
        public final static Property MaritalStatus = new Property(34, String.class, "maritalStatus", false, "MARITAL_STATUS");
        public final static Property NextOfKin = new Property(35, String.class, "nextOfKin", false, "NEXT_OF_KIN");
        public final static Property NextOfKinPhone = new Property(36, String.class, "nextOfKinPhone", false, "NEXT_OF_KIN_PHONE");
        public final static Property AllergenList = new Property(37, String.class, "allergenList", false, "ALLERGEN_LIST");
        public final static Property ByNurseName = new Property(38, String.class, "byNurseName", false, "BY_NURSE_NAME");
        public final static Property ChiefDoctorId = new Property(39, String.class, "chiefDoctorId", false, "CHIEF_DOCTOR_ID");
        public final static Property ChiefDoctor = new Property(40, String.class, "chiefDoctor", false, "CHIEF_DOCTOR");
        public final static Property ChargerDoctorId = new Property(41, String.class, "chargerDoctorId", false, "CHARGER_DOCTOR_ID");
        public final static Property ChargerDoctorName = new Property(42, String.class, "chargerDoctorName", false, "CHARGER_DOCTOR_NAME");
        public final static Property AttendingDoctorId = new Property(43, String.class, "attendingDoctorId", false, "ATTENDING_DOCTOR_ID");
        public final static Property AttendingDoctor = new Property(44, String.class, "attendingDoctor", false, "ATTENDING_DOCTOR");
        public final static Property ChargeType = new Property(45, String.class, "chargeType", false, "CHARGE_TYPE");
        public final static Property ZipCode = new Property(46, String.class, "zipCode", false, "ZIP_CODE");
        public final static Property Prepayments = new Property(47, double.class, "prepayments", false, "PREPAYMENTS");
        public final static Property TotalCosts = new Property(48, double.class, "totalCosts", false, "TOTAL_COSTS");
        public final static Property OperatingDataCount = new Property(49, int.class, "operatingDataCount", false, "OPERATING_DATA_COUNT");
        public final static Property Poor = new Property(50, String.class, "poor", false, "POOR");
    }


    public PatientInfoDao(DaoConfig config) {
        super(config);
    }
    
    public PatientInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PATIENT_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"DEPT_CODE\" TEXT NOT NULL ," + // 1: deptCode
                "\"BED_NO\" INTEGER NOT NULL ," + // 2: bedNo
                "\"BED_LABEL\" TEXT," + // 3: bedLabel
                "\"BED_STATUS\" TEXT," + // 4: bedStatus
                "\"BABY_NO\" TEXT," + // 5: babyNo
                "\"PATIENT_ID\" TEXT," + // 6: patientId
                "\"VISIT_ID\" TEXT," + // 7: visitId
                "\"PATIENT_NAME\" TEXT," + // 8: patientName
                "\"PATIENT_AGE\" TEXT," + // 9: patientAge
                "\"PATIENT_SEX\" TEXT," + // 10: patientSex
                "\"PATIENT_CONDITION\" TEXT," + // 11: patientCondition
                "\"DIAGNOSIS\" TEXT," + // 12: diagnosis
                "\"FASTING_SIGN\" TEXT," + // 13: fastingSign
                "\"ISOLATION_SIGN\" TEXT," + // 14: isolationSign
                "\"NURSING_CLASS\" TEXT," + // 15: nursingClass
                "\"WARD_CODE\" TEXT," + // 16: wardCode
                "\"WARD_NAME\" TEXT," + // 17: wardName
                "\"DEPT_NAME\" TEXT," + // 18: deptName
                "\"INP_NO\" TEXT," + // 19: inpNo
                "\"VISIT_TIME\" INTEGER NOT NULL ," + // 20: visitTime
                "\"ADM_WARD_TIME\" INTEGER NOT NULL ," + // 21: admWardTime
                "\"ADMISSION_DATE_COUNT\" INTEGER NOT NULL ," + // 22: admissionDateCount
                "\"VISIT_NO\" TEXT," + // 23: visitNo
                "\"ID_NO\" TEXT," + // 24: idNo
                "\"PATIENT_CLASS\" TEXT," + // 25: patientClass
                "\"PATIENT_IDENTITY\" TEXT," + // 26: patientIdentity
                "\"PHONE_NUMBER\" TEXT," + // 27: phoneNumber
                "\"NATION\" TEXT," + // 28: nation
                "\"BIRTH_TIME\" INTEGER NOT NULL ," + // 29: birthTime
                "\"BIRTH_PLACE_CODE\" TEXT," + // 30: birthPlaceCode
                "\"BIRTH_PLACE_NAME\" TEXT," + // 31: birthPlaceName
                "\"PRESENT_ADDRESS_CODE\" TEXT," + // 32: presentAddressCode
                "\"PRESENT_ADDRESS_NAME\" TEXT," + // 33: presentAddressName
                "\"MARITAL_STATUS\" TEXT," + // 34: maritalStatus
                "\"NEXT_OF_KIN\" TEXT," + // 35: nextOfKin
                "\"NEXT_OF_KIN_PHONE\" TEXT," + // 36: nextOfKinPhone
                "\"ALLERGEN_LIST\" TEXT," + // 37: allergenList
                "\"BY_NURSE_NAME\" TEXT," + // 38: byNurseName
                "\"CHIEF_DOCTOR_ID\" TEXT," + // 39: chiefDoctorId
                "\"CHIEF_DOCTOR\" TEXT," + // 40: chiefDoctor
                "\"CHARGER_DOCTOR_ID\" TEXT," + // 41: chargerDoctorId
                "\"CHARGER_DOCTOR_NAME\" TEXT," + // 42: chargerDoctorName
                "\"ATTENDING_DOCTOR_ID\" TEXT," + // 43: attendingDoctorId
                "\"ATTENDING_DOCTOR\" TEXT," + // 44: attendingDoctor
                "\"CHARGE_TYPE\" TEXT," + // 45: chargeType
                "\"ZIP_CODE\" TEXT," + // 46: zipCode
                "\"PREPAYMENTS\" REAL NOT NULL ," + // 47: prepayments
                "\"TOTAL_COSTS\" REAL NOT NULL ," + // 48: totalCosts
                "\"OPERATING_DATA_COUNT\" INTEGER NOT NULL ," + // 49: operatingDataCount
                "\"POOR\" TEXT);"); // 50: poor
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_PATIENT_INFO_BED_NO_DESC_PATIENT_ID_DESC_VISIT_ID_DESC ON \"PATIENT_INFO\"" +
                " (\"BED_NO\" DESC,\"PATIENT_ID\" DESC,\"VISIT_ID\" DESC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PATIENT_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PatientInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getDeptCode());
        stmt.bindLong(3, entity.getBedNo());
 
        String bedLabel = entity.getBedLabel();
        if (bedLabel != null) {
            stmt.bindString(4, bedLabel);
        }
 
        String bedStatus = entity.getBedStatus();
        if (bedStatus != null) {
            stmt.bindString(5, bedStatus);
        }
 
        String babyNo = entity.getBabyNo();
        if (babyNo != null) {
            stmt.bindString(6, babyNo);
        }
 
        String patientId = entity.getPatientId();
        if (patientId != null) {
            stmt.bindString(7, patientId);
        }
 
        String visitId = entity.getVisitId();
        if (visitId != null) {
            stmt.bindString(8, visitId);
        }
 
        String patientName = entity.getPatientName();
        if (patientName != null) {
            stmt.bindString(9, patientName);
        }
 
        String patientAge = entity.getPatientAge();
        if (patientAge != null) {
            stmt.bindString(10, patientAge);
        }
 
        String patientSex = entity.getPatientSex();
        if (patientSex != null) {
            stmt.bindString(11, patientSex);
        }
 
        String patientCondition = entity.getPatientCondition();
        if (patientCondition != null) {
            stmt.bindString(12, patientCondition);
        }
 
        String diagnosis = entity.getDiagnosis();
        if (diagnosis != null) {
            stmt.bindString(13, diagnosis);
        }
 
        String fastingSign = entity.getFastingSign();
        if (fastingSign != null) {
            stmt.bindString(14, fastingSign);
        }
 
        String isolationSign = entity.getIsolationSign();
        if (isolationSign != null) {
            stmt.bindString(15, isolationSign);
        }
 
        String nursingClass = entity.getNursingClass();
        if (nursingClass != null) {
            stmt.bindString(16, nursingClass);
        }
 
        String wardCode = entity.getWardCode();
        if (wardCode != null) {
            stmt.bindString(17, wardCode);
        }
 
        String wardName = entity.getWardName();
        if (wardName != null) {
            stmt.bindString(18, wardName);
        }
 
        String deptName = entity.getDeptName();
        if (deptName != null) {
            stmt.bindString(19, deptName);
        }
 
        String inpNo = entity.getInpNo();
        if (inpNo != null) {
            stmt.bindString(20, inpNo);
        }
        stmt.bindLong(21, entity.getVisitTime());
        stmt.bindLong(22, entity.getAdmWardTime());
        stmt.bindLong(23, entity.getAdmissionDateCount());
 
        String visitNo = entity.getVisitNo();
        if (visitNo != null) {
            stmt.bindString(24, visitNo);
        }
 
        String idNo = entity.getIdNo();
        if (idNo != null) {
            stmt.bindString(25, idNo);
        }
 
        String patientClass = entity.getPatientClass();
        if (patientClass != null) {
            stmt.bindString(26, patientClass);
        }
 
        String patientIdentity = entity.getPatientIdentity();
        if (patientIdentity != null) {
            stmt.bindString(27, patientIdentity);
        }
 
        String phoneNumber = entity.getPhoneNumber();
        if (phoneNumber != null) {
            stmt.bindString(28, phoneNumber);
        }
 
        String nation = entity.getNation();
        if (nation != null) {
            stmt.bindString(29, nation);
        }
        stmt.bindLong(30, entity.getBirthTime());
 
        String birthPlaceCode = entity.getBirthPlaceCode();
        if (birthPlaceCode != null) {
            stmt.bindString(31, birthPlaceCode);
        }
 
        String birthPlaceName = entity.getBirthPlaceName();
        if (birthPlaceName != null) {
            stmt.bindString(32, birthPlaceName);
        }
 
        String presentAddressCode = entity.getPresentAddressCode();
        if (presentAddressCode != null) {
            stmt.bindString(33, presentAddressCode);
        }
 
        String presentAddressName = entity.getPresentAddressName();
        if (presentAddressName != null) {
            stmt.bindString(34, presentAddressName);
        }
 
        String maritalStatus = entity.getMaritalStatus();
        if (maritalStatus != null) {
            stmt.bindString(35, maritalStatus);
        }
 
        String nextOfKin = entity.getNextOfKin();
        if (nextOfKin != null) {
            stmt.bindString(36, nextOfKin);
        }
 
        String nextOfKinPhone = entity.getNextOfKinPhone();
        if (nextOfKinPhone != null) {
            stmt.bindString(37, nextOfKinPhone);
        }
 
        String allergenList = entity.getAllergenList();
        if (allergenList != null) {
            stmt.bindString(38, allergenList);
        }
 
        String byNurseName = entity.getByNurseName();
        if (byNurseName != null) {
            stmt.bindString(39, byNurseName);
        }
 
        String chiefDoctorId = entity.getChiefDoctorId();
        if (chiefDoctorId != null) {
            stmt.bindString(40, chiefDoctorId);
        }
 
        String chiefDoctor = entity.getChiefDoctor();
        if (chiefDoctor != null) {
            stmt.bindString(41, chiefDoctor);
        }
 
        String chargerDoctorId = entity.getChargerDoctorId();
        if (chargerDoctorId != null) {
            stmt.bindString(42, chargerDoctorId);
        }
 
        String chargerDoctorName = entity.getChargerDoctorName();
        if (chargerDoctorName != null) {
            stmt.bindString(43, chargerDoctorName);
        }
 
        String attendingDoctorId = entity.getAttendingDoctorId();
        if (attendingDoctorId != null) {
            stmt.bindString(44, attendingDoctorId);
        }
 
        String attendingDoctor = entity.getAttendingDoctor();
        if (attendingDoctor != null) {
            stmt.bindString(45, attendingDoctor);
        }
 
        String chargeType = entity.getChargeType();
        if (chargeType != null) {
            stmt.bindString(46, chargeType);
        }
 
        String zipCode = entity.getZipCode();
        if (zipCode != null) {
            stmt.bindString(47, zipCode);
        }
        stmt.bindDouble(48, entity.getPrepayments());
        stmt.bindDouble(49, entity.getTotalCosts());
        stmt.bindLong(50, entity.getOperatingDataCount());
 
        String poor = entity.getPoor();
        if (poor != null) {
            stmt.bindString(51, poor);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PatientInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getDeptCode());
        stmt.bindLong(3, entity.getBedNo());
 
        String bedLabel = entity.getBedLabel();
        if (bedLabel != null) {
            stmt.bindString(4, bedLabel);
        }
 
        String bedStatus = entity.getBedStatus();
        if (bedStatus != null) {
            stmt.bindString(5, bedStatus);
        }
 
        String babyNo = entity.getBabyNo();
        if (babyNo != null) {
            stmt.bindString(6, babyNo);
        }
 
        String patientId = entity.getPatientId();
        if (patientId != null) {
            stmt.bindString(7, patientId);
        }
 
        String visitId = entity.getVisitId();
        if (visitId != null) {
            stmt.bindString(8, visitId);
        }
 
        String patientName = entity.getPatientName();
        if (patientName != null) {
            stmt.bindString(9, patientName);
        }
 
        String patientAge = entity.getPatientAge();
        if (patientAge != null) {
            stmt.bindString(10, patientAge);
        }
 
        String patientSex = entity.getPatientSex();
        if (patientSex != null) {
            stmt.bindString(11, patientSex);
        }
 
        String patientCondition = entity.getPatientCondition();
        if (patientCondition != null) {
            stmt.bindString(12, patientCondition);
        }
 
        String diagnosis = entity.getDiagnosis();
        if (diagnosis != null) {
            stmt.bindString(13, diagnosis);
        }
 
        String fastingSign = entity.getFastingSign();
        if (fastingSign != null) {
            stmt.bindString(14, fastingSign);
        }
 
        String isolationSign = entity.getIsolationSign();
        if (isolationSign != null) {
            stmt.bindString(15, isolationSign);
        }
 
        String nursingClass = entity.getNursingClass();
        if (nursingClass != null) {
            stmt.bindString(16, nursingClass);
        }
 
        String wardCode = entity.getWardCode();
        if (wardCode != null) {
            stmt.bindString(17, wardCode);
        }
 
        String wardName = entity.getWardName();
        if (wardName != null) {
            stmt.bindString(18, wardName);
        }
 
        String deptName = entity.getDeptName();
        if (deptName != null) {
            stmt.bindString(19, deptName);
        }
 
        String inpNo = entity.getInpNo();
        if (inpNo != null) {
            stmt.bindString(20, inpNo);
        }
        stmt.bindLong(21, entity.getVisitTime());
        stmt.bindLong(22, entity.getAdmWardTime());
        stmt.bindLong(23, entity.getAdmissionDateCount());
 
        String visitNo = entity.getVisitNo();
        if (visitNo != null) {
            stmt.bindString(24, visitNo);
        }
 
        String idNo = entity.getIdNo();
        if (idNo != null) {
            stmt.bindString(25, idNo);
        }
 
        String patientClass = entity.getPatientClass();
        if (patientClass != null) {
            stmt.bindString(26, patientClass);
        }
 
        String patientIdentity = entity.getPatientIdentity();
        if (patientIdentity != null) {
            stmt.bindString(27, patientIdentity);
        }
 
        String phoneNumber = entity.getPhoneNumber();
        if (phoneNumber != null) {
            stmt.bindString(28, phoneNumber);
        }
 
        String nation = entity.getNation();
        if (nation != null) {
            stmt.bindString(29, nation);
        }
        stmt.bindLong(30, entity.getBirthTime());
 
        String birthPlaceCode = entity.getBirthPlaceCode();
        if (birthPlaceCode != null) {
            stmt.bindString(31, birthPlaceCode);
        }
 
        String birthPlaceName = entity.getBirthPlaceName();
        if (birthPlaceName != null) {
            stmt.bindString(32, birthPlaceName);
        }
 
        String presentAddressCode = entity.getPresentAddressCode();
        if (presentAddressCode != null) {
            stmt.bindString(33, presentAddressCode);
        }
 
        String presentAddressName = entity.getPresentAddressName();
        if (presentAddressName != null) {
            stmt.bindString(34, presentAddressName);
        }
 
        String maritalStatus = entity.getMaritalStatus();
        if (maritalStatus != null) {
            stmt.bindString(35, maritalStatus);
        }
 
        String nextOfKin = entity.getNextOfKin();
        if (nextOfKin != null) {
            stmt.bindString(36, nextOfKin);
        }
 
        String nextOfKinPhone = entity.getNextOfKinPhone();
        if (nextOfKinPhone != null) {
            stmt.bindString(37, nextOfKinPhone);
        }
 
        String allergenList = entity.getAllergenList();
        if (allergenList != null) {
            stmt.bindString(38, allergenList);
        }
 
        String byNurseName = entity.getByNurseName();
        if (byNurseName != null) {
            stmt.bindString(39, byNurseName);
        }
 
        String chiefDoctorId = entity.getChiefDoctorId();
        if (chiefDoctorId != null) {
            stmt.bindString(40, chiefDoctorId);
        }
 
        String chiefDoctor = entity.getChiefDoctor();
        if (chiefDoctor != null) {
            stmt.bindString(41, chiefDoctor);
        }
 
        String chargerDoctorId = entity.getChargerDoctorId();
        if (chargerDoctorId != null) {
            stmt.bindString(42, chargerDoctorId);
        }
 
        String chargerDoctorName = entity.getChargerDoctorName();
        if (chargerDoctorName != null) {
            stmt.bindString(43, chargerDoctorName);
        }
 
        String attendingDoctorId = entity.getAttendingDoctorId();
        if (attendingDoctorId != null) {
            stmt.bindString(44, attendingDoctorId);
        }
 
        String attendingDoctor = entity.getAttendingDoctor();
        if (attendingDoctor != null) {
            stmt.bindString(45, attendingDoctor);
        }
 
        String chargeType = entity.getChargeType();
        if (chargeType != null) {
            stmt.bindString(46, chargeType);
        }
 
        String zipCode = entity.getZipCode();
        if (zipCode != null) {
            stmt.bindString(47, zipCode);
        }
        stmt.bindDouble(48, entity.getPrepayments());
        stmt.bindDouble(49, entity.getTotalCosts());
        stmt.bindLong(50, entity.getOperatingDataCount());
 
        String poor = entity.getPoor();
        if (poor != null) {
            stmt.bindString(51, poor);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PatientInfo readEntity(Cursor cursor, int offset) {
        PatientInfo entity = new PatientInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // deptCode
            cursor.getInt(offset + 2), // bedNo
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // bedLabel
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // bedStatus
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // babyNo
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // patientId
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // visitId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // patientName
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // patientAge
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // patientSex
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // patientCondition
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // diagnosis
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // fastingSign
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // isolationSign
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // nursingClass
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // wardCode
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // wardName
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // deptName
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // inpNo
            cursor.getLong(offset + 20), // visitTime
            cursor.getLong(offset + 21), // admWardTime
            cursor.getInt(offset + 22), // admissionDateCount
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // visitNo
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // idNo
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // patientClass
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // patientIdentity
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // phoneNumber
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // nation
            cursor.getLong(offset + 29), // birthTime
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // birthPlaceCode
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // birthPlaceName
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // presentAddressCode
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // presentAddressName
            cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34), // maritalStatus
            cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35), // nextOfKin
            cursor.isNull(offset + 36) ? null : cursor.getString(offset + 36), // nextOfKinPhone
            cursor.isNull(offset + 37) ? null : cursor.getString(offset + 37), // allergenList
            cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38), // byNurseName
            cursor.isNull(offset + 39) ? null : cursor.getString(offset + 39), // chiefDoctorId
            cursor.isNull(offset + 40) ? null : cursor.getString(offset + 40), // chiefDoctor
            cursor.isNull(offset + 41) ? null : cursor.getString(offset + 41), // chargerDoctorId
            cursor.isNull(offset + 42) ? null : cursor.getString(offset + 42), // chargerDoctorName
            cursor.isNull(offset + 43) ? null : cursor.getString(offset + 43), // attendingDoctorId
            cursor.isNull(offset + 44) ? null : cursor.getString(offset + 44), // attendingDoctor
            cursor.isNull(offset + 45) ? null : cursor.getString(offset + 45), // chargeType
            cursor.isNull(offset + 46) ? null : cursor.getString(offset + 46), // zipCode
            cursor.getDouble(offset + 47), // prepayments
            cursor.getDouble(offset + 48), // totalCosts
            cursor.getInt(offset + 49), // operatingDataCount
            cursor.isNull(offset + 50) ? null : cursor.getString(offset + 50) // poor
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PatientInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeptCode(cursor.getString(offset + 1));
        entity.setBedNo(cursor.getInt(offset + 2));
        entity.setBedLabel(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBedStatus(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBabyNo(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPatientId(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setVisitId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPatientName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPatientAge(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPatientSex(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPatientCondition(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setDiagnosis(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setFastingSign(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setIsolationSign(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setNursingClass(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setWardCode(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setWardName(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setDeptName(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setInpNo(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setVisitTime(cursor.getLong(offset + 20));
        entity.setAdmWardTime(cursor.getLong(offset + 21));
        entity.setAdmissionDateCount(cursor.getInt(offset + 22));
        entity.setVisitNo(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setIdNo(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setPatientClass(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setPatientIdentity(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setPhoneNumber(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setNation(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setBirthTime(cursor.getLong(offset + 29));
        entity.setBirthPlaceCode(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setBirthPlaceName(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setPresentAddressCode(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setPresentAddressName(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setMaritalStatus(cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
        entity.setNextOfKin(cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35));
        entity.setNextOfKinPhone(cursor.isNull(offset + 36) ? null : cursor.getString(offset + 36));
        entity.setAllergenList(cursor.isNull(offset + 37) ? null : cursor.getString(offset + 37));
        entity.setByNurseName(cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38));
        entity.setChiefDoctorId(cursor.isNull(offset + 39) ? null : cursor.getString(offset + 39));
        entity.setChiefDoctor(cursor.isNull(offset + 40) ? null : cursor.getString(offset + 40));
        entity.setChargerDoctorId(cursor.isNull(offset + 41) ? null : cursor.getString(offset + 41));
        entity.setChargerDoctorName(cursor.isNull(offset + 42) ? null : cursor.getString(offset + 42));
        entity.setAttendingDoctorId(cursor.isNull(offset + 43) ? null : cursor.getString(offset + 43));
        entity.setAttendingDoctor(cursor.isNull(offset + 44) ? null : cursor.getString(offset + 44));
        entity.setChargeType(cursor.isNull(offset + 45) ? null : cursor.getString(offset + 45));
        entity.setZipCode(cursor.isNull(offset + 46) ? null : cursor.getString(offset + 46));
        entity.setPrepayments(cursor.getDouble(offset + 47));
        entity.setTotalCosts(cursor.getDouble(offset + 48));
        entity.setOperatingDataCount(cursor.getInt(offset + 49));
        entity.setPoor(cursor.isNull(offset + 50) ? null : cursor.getString(offset + 50));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PatientInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PatientInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PatientInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

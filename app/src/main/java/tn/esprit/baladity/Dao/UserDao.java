package tn.esprit.baladity.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.baladity.entities.User;
import tn.esprit.baladity.helper.UserHelper;
import tn.esprit.baladity.utils.UserContract;


public class UserDao {

    UserHelper mDbHelper;

    public UserDao(Context context) {
        mDbHelper = new UserHelper(context);
    }

    public long insertUser(User user){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserContract.UserEntry.NOM_COLUMN, user.getNom());
        cv.put(UserContract.UserEntry.PRENOM_COLUMN, user.getPrenom());
        cv.put(UserContract.UserEntry.PASSWORD_COLUMN, user.getPassword());
        cv.put(UserContract.UserEntry.EMAIL_COLUMN, user.getEmail());
        cv.put(UserContract.UserEntry.NUMTEL_COLUMN, user.getNumTel());
        return  db.insert(UserContract.UserEntry.TABLE_NAME, null, cv);
    }

    public List<User> findAllUser(){
        List<User> UserList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User user = new User();
            //ON RECUPERE L'INDEX DE LA COLONNNE SUIVANT SON NOM
            user.setNom(cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.NOM_COLUMN)));
            user.setPrenom(cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.PRENOM_COLUMN)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.EMAIL_COLUMN)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.PASSWORD_COLUMN)));
            user.setNumTel(cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.NUMTEL_COLUMN)));
            UserList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return UserList;
    }
}

package tn.esprit.baladity.utils;

import android.provider.BaseColumns;


public final class UserContract {

    public UserContract() {
    }

    public static class UserEntry implements BaseColumns {
        public static final String NOM_COLUMN = "nom";
        public static final String PRENOM_COLUMN = "prenom";
        public static final String EMAIL_COLUMN = "email";
        public static final String NUMTEL_COLUMN = "numtel";
        public static final String PASSWORD_COLUMN = "password";
        public static final String TABLE_NAME = "user";
    }

}

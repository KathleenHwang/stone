package snsproject.com.naver.cafe.stone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import snsproject.com.naver.cafe.stone.board.GiboInfo;

/**
 * Created by sjhwang on 2015-03-07.
 */
public class DatabaseManager {
    private static final String DB_PATH = "/data/data/snsproject.com.naver.cafe.stone/databases/";
    /**
     * 낮은 중국식 기본형
     */
    private static final String DATABASE_NAME = "anal_1.db";
    /**
     * 기본형
     */
    private static final String ANAL_1_TABLE = "anal_0";
    private static final int DATABASE_VERSION = 1;

    /**
     * Columns
     */
    private static final String COL_TYPE_NAME = "TYPE_NAME";
    private static final String COL_COLOR = "COLOR";
    private static final String COL_X = "X";
    private static final String COL_Y = "Y";
    private static final String COL_CNT = "CNT";
    private static final String COL_ID = "ID";
    private static final String COL_PARENT_ID = "PARENT_ID";

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public DatabaseManager(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            mHelperContext = context;

            boolean dbexist = checkdatabase();
            if (!dbexist) {

                try {
                    createdatabase();
                } catch (Exception e) {
                    e.getMessage();
                }
            }


            opendatabase();
        }

        public void createdatabase() throws IOException {
            boolean dbexist = checkdatabase();
            if (dbexist) {
                //System.out.println(" Database exists.");
            } else {
                this.getReadableDatabase();
                try {
                    copydatabase();
                } catch (IOException e) {
                    throw new Error("Error copying database");
                }
            }
        }

        private boolean checkdatabase() {
            //SQLiteDatabase checkdb = null;
            boolean checkdb = false;
            try {
                String myPath = DB_PATH + DATABASE_NAME;
                File dbfile = new File(myPath);
                //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
                checkdb = dbfile.exists();
            } catch (SQLiteException e) {
                System.out.println("Database doesn't exist");
            }
            return checkdb;
        }

        private void copydatabase() throws IOException {
            //Open your local db as the input stream
            InputStream myinput = mHelperContext.getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outfilename = DB_PATH + DATABASE_NAME;

            //Open the empty db as the output stream
            OutputStream myoutput = new FileOutputStream(outfilename);

            // transfer byte to inputfile to outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myinput.read(buffer)) > 0) {
                myoutput.write(buffer, 0, length);
            }

            //Close the streams
            myoutput.flush();
            myoutput.close();
            myinput.close();
        }

        public void opendatabase() {
            //Open the database
            String mypath = DB_PATH + DATABASE_NAME;
            mDatabase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
        }

        public synchronized void close() {
            if (mDatabase != null) {
                mDatabase.close();
            }
            super.close();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public List<GiboInfo> getGiboInfo(int parentId) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(ANAL_1_TABLE);

        String selection = COL_PARENT_ID + " =  ?";
        String[] selectionArgs = new String[]{Integer.toString(parentId)};

        Cursor cursor = builder.query(mDatabaseOpenHelper.mDatabase,
                null, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else {
            if (cursor.moveToFirst()) {
                ArrayList<GiboInfo> giboInfos = new ArrayList<>();

                while (cursor.isAfterLast() == false) {
                    float typeName = cursor.getFloat(cursor.getColumnIndex(COL_TYPE_NAME));
                    String color = cursor.getString(cursor.getColumnIndex(COL_COLOR));
                    int x = cursor.getInt(cursor.getColumnIndex(COL_X));
                    int y = cursor.getInt(cursor.getColumnIndex(COL_Y));
                    int cnt = cursor.getInt(cursor.getColumnIndex(COL_CNT));
                    int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                    int pId = cursor.getInt(cursor.getColumnIndex(COL_PARENT_ID));
                    giboInfos.add(new GiboInfo(typeName, color.charAt(0), x, y, cnt, id, pId));
                    cursor.moveToNext();
                }

                cursor.close();
                return giboInfos;
            }
        }
        return null;
    }
}

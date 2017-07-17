package com.boostcamp.miniproject2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.boostcamp.miniproject2.R;
import com.boostcamp.miniproject2.model.Restaurant;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by moon on 2017. 7. 14..
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper databaseHelper;
    private SQLiteDatabase db = getWritableDatabase();

    public static final String TABLE_NAME = "restaurant";
    public static final String DB_NAME = "RESTAURANT.db";
    public static final int VERSION = 100;

    public static final int ORDER_BY_NEARBY = 101;
    public static final int ORDER_BY_POPULAR = 102;
    public static final int ORDER_BY_RECENT = 103;

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(TABLE_NAME);
        sb.append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append("name TEXT, ");
        sb.append("thumb INTEGER, ");
        sb.append("info TEXT, ");
        sb.append("checked INTEGER, ");
        sb.append("nearby INTEGER, ");
        sb.append("popular INTEGER, ");
        sb.append("recent INTEGER)");


        db.execSQL(sb.toString());

        this.db = db;

        Log.d("Test", "Create DB");

        initData();
    }

    private void initData() {
        Log.d("Test", "Init DB Item");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("해우소");
        restaurant.setInfo("꿀막걸리가 인기메뉴고 맛있는 음식을 저렴한 가격에 즐길 수 있는 해우소입니다.");
        restaurant.setImage(R.drawable.ic_haewoosau);
        restaurant.setChecked(false);

        insert(restaurant);

        restaurant = new Restaurant();
        restaurant.setName("삼교리동치미막국수");
        restaurant.setInfo("막국수를 비빔으로 먹을지 물로 먹을지 고객이 만들어 먹는 삼교리동치미막국수입니다.");
        restaurant.setImage(R.drawable.ic_samgyori);
        restaurant.setChecked(false);


        insert(restaurant);

        restaurant = new Restaurant();
        restaurant.setName("오야오야");
        restaurant.setInfo("밥과 소주를 부르는 맛! 돼지고기에 감자, 호박, 양파 넣고 고추장 된장으로 끓입니다.");
        restaurant.setImage(R.drawable.ic_oyaoya);
        restaurant.setChecked(false);


        insert(restaurant);

        restaurant = new Restaurant();
        restaurant.setName("도미노피자");
        restaurant.setInfo("올 여름, 감칠맛 가득 꽃게가 피자로! 꽃게 온 더 피자 출시! 지금 바로 전화주세요~");
        restaurant.setImage(R.drawable.ic_domino);
        restaurant.setChecked(false);

        insert(restaurant);

        restaurant = new Restaurant();
        restaurant.setName("호미불닭발");
        restaurant.setInfo("경기도 부천시에 위치한 호미 불닭발! 멈출수 없는 매콤함을 느끼러 오세요!");
        restaurant.setImage(R.drawable.ic_homi);
        restaurant.setChecked(false);

        insert(restaurant);

        restaurant = new Restaurant();
        restaurant.setName("호치킨");
        restaurant.setInfo("바삭바삭한 크리스피 치킨부터 기름기 쏙 뺀 로스트 치킨까지! 각종 치킨들과 맛있는 치킨무가 있으니 달려~!");
        restaurant.setImage(R.drawable.ic_ho);
        restaurant.setChecked(false);

        insert(restaurant);

        restaurant = new Restaurant();
        restaurant.setName("조선포차");
        restaurant.setInfo("다양한 안주와 다양한 술! 게다가 가격까지 저렴하다!! 오늘 밤은 쏘맥으로 달려부러~");
        restaurant.setImage(R.drawable.ic_chosun);
        restaurant.setChecked(false);

        insert(restaurant);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("Test", "Open DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            Log.d("Test", "Upgrade DB");

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void insert(Restaurant restaurant) {
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ");
        sb.append(TABLE_NAME);
        sb.append("(name, thumb, info, checked, nearby, popular, recent) ");
        sb.append("VALUES(?, ?, ?, ?, ?, ?, ?)");

        Random random = new Random();
        int chk = restaurant.isChecked() ? 1 : 0;
        int nearby = random.nextInt(1000);
        int popular = random.nextInt(1000);
        int recent = random.nextInt(1000);

        db.execSQL(
                sb.toString(),
                new Object[]{restaurant.getName(),
                        restaurant.getImage(),
                        restaurant.getInfo(),
                        chk,
                        nearby,
                        popular,
                        recent
                });

        Log.d("Test", "Insert DB");
    }

    public ArrayList<Restaurant> selectAllBySortCondition(int condition) {

        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM ");
        sb.append(TABLE_NAME);

        switch (condition) {
            case ORDER_BY_NEARBY:
                sb.append(" ORDER BY nearby ASC");
                break;
            case ORDER_BY_POPULAR:
                sb.append(" ORDER BY popular DESC");
                break;
            case ORDER_BY_RECENT:
                sb.append(" ORDER BY recent DESC");
                break;
        }

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<Restaurant> restaurantList = new ArrayList<>();
        Restaurant restaurant;

        while (cursor.moveToNext()) {
            restaurant = new Restaurant();
            restaurant.setName(cursor.getString(1));
            restaurant.setImage(cursor.getInt(2));
            restaurant.setInfo(cursor.getString(3));
            restaurant.setChecked(cursor.getInt(4) == 1);

            restaurantList.add(restaurant);
        }
        cursor.close();

        return restaurantList;
    }

    public int selectIdByName(String name) {
        StringBuffer sb = new StringBuffer();

        sb.append(" SELECT _id FROM ");
        sb.append(TABLE_NAME);
        sb.append(" WHERE name=?");

        Cursor cursor = db.rawQuery(sb.toString(), new String[]{name});
        cursor.moveToNext();

        int id = cursor.getInt(0);

        cursor.close();

        return id;
    }

    public void updateCheckValueById(int id, boolean checked) {
        int chk = checked ? 1 : 0;
        StringBuffer sb = new StringBuffer();

        sb.append("UPDATE ");
        sb.append(TABLE_NAME);
        sb.append(" SET checked = ? ");
        sb.append("WHERE _id = ?");

        db.execSQL(
                sb.toString(),
                new Object[]{chk,
                        id
                });

        Log.d("Test", "Update DB chk: " + String.valueOf(chk));

    }
}

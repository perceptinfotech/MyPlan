package percept.myplan.db;

import android.content.Context;

public class DBHelper {

    private DBAdapter dbAdapter;

    public DBHelper(Context context) {
        dbAdapter = new DBAdapter(context);
    }


}

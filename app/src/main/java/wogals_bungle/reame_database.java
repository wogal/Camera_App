package wogals_bungle;

import io.realm.Realm;

/**
 * Created by wogal on 1/21/2017.
 */

public class reame_database   {
    private  String _name = "";
    private  String _passwd = "";

   // private Realm ff;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_passwd() {
        return _passwd;
    }

    public void set_passwd(String _passwd) {
        this._passwd = _passwd;
    }
}

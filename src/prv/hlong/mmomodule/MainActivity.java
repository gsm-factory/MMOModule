package prv.hlong.mmomodule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		HashMap hashmap = new HashMap();
		hashmap.put("fist_time", "true");
        hashmap.put("android_id", "47643b34");
        hashmap.put("googlead_id", "96bd03b6-defc-4203-83d3-dc1c730801f7");
        hashmap.put("imei", "364574346345347");
        hashmap.put("serial", "46gdrh463");
        hashmap.put("bluetooth_mac", "00:0a:95:9d:68:16");
        hashmap.put("mac_address", "00:0a:95:9d:68:17");
        hashmap.put("wifi_name", "Wifi Name");
        hashmap.put("sim_number", "+962439943");
        hashmap.put("sim_serial", "9628693949683");
        hashmap.put("imsi", "9628693949683");
        hashmap.put("baseband", "9696969696");
        writePref(this, hashmap);
        Toast.makeText(this, "onStart Func! Created Pref config", 0).show();
	}
	public  static void writePref(Context cont, HashMap perfHashMap) {
		Iterator<?> itr = null;
		SharedPreferences sharepref = cont.getSharedPreferences("xpref_config", 1);
		Editor edit = sharepref.edit();
		Entry mEntry = null;
		for (itr = perfHashMap.entrySet().iterator(); itr.hasNext(); edit.putString((String)mEntry.getKey(), (String)mEntry.getValue())) {
			mEntry =(Entry)itr.next();
		}
		edit.commit();
		
	}
}

package prv.hlong.mmomodule;

import java.lang.reflect.Method;
import android.provider.Settings;
import android.app.Activity;
import android.content.ContentResolver;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import de.robv.android.xposed.*;
//import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
//import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedBridge;

public class FakeDeviceInfo implements IXposedHookLoadPackage {
	private XSharedPreferences xPref;

	// private LoadPackageParam lpparam;
	@Override
	public void handleLoadPackage(LoadPackageParam loadPP) throws Throwable {
		// TODO Auto-generated method stub
		xPref = new XSharedPreferences(FakeDeviceInfo.class.getPackage()
				.getName(), "xpref_config");
		// lpparam = loadPP;

		try {
			XposedHelpers.findAndHookMethod((Class<?>) Activity.class,
					"onResume", new XC_MethodHook() {
						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							xPref.reload();
						}
					});
		} catch (NoSuchMethodError e) {
			XposedBridge.log("couldn't hook method " + "onResume");
		}

		// Bypass and change android_id
		try {
			XposedHelpers.findAndHookMethod("android.provider.Settings.Secure",
					loadPP.classLoader, "getString", ContentResolver.class,
					String.class, new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							// super.afterHookedMethod(param);
							if (param.args[1]
									.equals(Settings.Secure.ANDROID_ID)) {
								param.setResult(xPref.getString("android_id",
										""));
							}
							/*
							 * if(param.args[1].equals("mock_location")) {
							 * param.setResult(0); }
							 */
						}
					});
		} catch (Exception e) {
			XposedBridge.log("Couldn't change android_id");
		}
		// //////

		// Change IMEI
		try {
			// XC_MethodReplacement.returnConstant(xPref.getString("imei", ""))
			XposedHelpers.findAndHookMethod(
					"android.telephony.TelephonyManager", loadPP.classLoader,
					"getDeviceId", new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(xPref.getString("imei", ""));
						}

					});

			XposedHelpers.findAndHookMethod(
					"com.android.internal.telephony.PhoneSubInfo",
					loadPP.classLoader, "getDeviceId", new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(xPref.getString("imei", ""));
						}

					});
			if (android.os.Build.VERSION.SDK_INT < 22) {
				XposedHelpers.findAndHookMethod(
						"com.android.internal.telephony.gsm.GSMPhone",
						loadPP.classLoader, "getDeviceId", new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								param.setResult(xPref.getString("imei", ""));
							}

						});
				XposedHelpers.findAndHookMethod(
						"com.android.internal.telephony.PhoneProxy",
						loadPP.classLoader, "getDeviceId", new XC_MethodHook() {

							@Override
							protected void afterHookedMethod(
									MethodHookParam param) throws Throwable {
								// TODO Auto-generated method stub
								super.afterHookedMethod(param);
								param.setResult(xPref.getString("imei", ""));
							}

						});
			}
		} catch (Exception e) {
			XposedBridge.log("Couldn't change IMEI");
		}
		// ////

		// Change Phone Number, Sim Serial, IMSI
		try {
			XposedHelpers.findAndHookMethod(
					"android.telephony.TelephonyManager", loadPP.classLoader,
					"getLine1Number", new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(xPref.getString("sim_number", ""));
						}
					});
			XposedHelpers.findAndHookMethod(
					"android.telephony.TelephonyManager", loadPP.classLoader,
					"getSimSerialNumber", new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(xPref.getString("sim_serial", ""));
						}

					});
			XposedHelpers.findAndHookMethod(
					"android.telephony.TelephonyManager", loadPP.classLoader,
					"getSubscriberId", new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(xPref.getString("imsi", ""));
						}

					});
		} catch (Exception e) {
			// TODO: handle exception
		}

		// ////

		// Change Wifi, Bluetooh
		try {
			XposedHelpers
					.findAndHookMethod("android.net.wifi.WifiInfo",
							loadPP.classLoader, "getMacAddress",
							XC_MethodReplacement.returnConstant(xPref
									.getString("mac_address", "")));
			XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
					loadPP.classLoader, "getSSID", XC_MethodReplacement
							.returnConstant(xPref.getString("wifi_name", "")));
			/*
			XposedHelpers.findAndHookMethod(
					"android.bluetooth.BluetoothAdapter", loadPP.classLoader,
					"getAddress", XC_MethodReplacement.returnConstant(xPref
							.getString("bluetooth_mac", "")));
			XposedHelpers.findAndHookMethod(
					"android.bluetooth.BluetoothDevice", loadPP.classLoader,
					"getAddress", XC_MethodReplacement.returnConstant(xPref
							.getString("bluetooth_mac", "")));
			*/

		} catch (Exception e) {
			XposedBridge.log("Couldn't change Wifi");
		}
		try {
			XposedHelpers.findAndHookMethod("android.bluetooth.BluetoothAdapter", loadPP.classLoader, "getAddress", new XC_MethodHook() {

				@Override
				protected void afterHookedMethod(MethodHookParam param)
						throws Throwable {
					// TODO Auto-generated method stub
					super.afterHookedMethod(param);
					param.setResult(xPref.getString("bluetooth_mac", ""));
				}
			});
			XposedHelpers.findAndHookMethod("android.bluetooth.BluetoothDevice", loadPP.classLoader, "getAddress", new XC_MethodHook() {

				@Override
				protected void afterHookedMethod(MethodHookParam param)
						throws Throwable {
					// TODO Auto-generated method stub
					super.afterHookedMethod(param);
					param.setResult(xPref.getString("bluetooth_mac", ""));
				}
			});
		} catch (Exception e) {
			XposedBridge.log("Couldn't change Bluetooh");
		}
		// /////

		// Change Google Ad ID
		try {
			XposedHelpers.findAndHookMethod("android.os.Binder",
					loadPP.classLoader, "execTransact", Integer.TYPE,
					Integer.TYPE, Integer.TYPE, Integer.TYPE,
					new XC_MethodHook() {

						@Override
						protected void beforeHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.beforeHookedMethod(param);
							if (((IBinder) param.thisObject)
									.getInterfaceDescriptor()
									.equals("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService")
									&& ((Integer) param.args[0]).intValue() == 1) {
								Parcel reply = null;
								try {
									Method methodObtain = Parcel.class
											.getDeclaredMethod(
													"obtain",
													Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? int.class
															: long.class);
									methodObtain.setAccessible(true);
									reply = (Parcel) methodObtain.invoke(null,
											param.args[2]);
								} catch (NoSuchMethodException ex) {

								}
								if (reply == null) {

								} else {
									reply.setDataPosition(0);
									reply.writeNoException();
									reply.writeString(xPref.getString(
											"googlead_id", ""));
								}

								param.setResult(true);
							}
						}
					});
		} catch (Exception e) {
			XposedBridge.log("Couldn't change Google Ad ID");
		}
		// /////

		// Change Baseband Version
		try {
			if (Build.VERSION.SDK_INT <= 14) {
				Class<?> classBuild = XposedHelpers.findClass(
						"android.os.Build", loadPP.classLoader);
				XposedHelpers.setStaticObjectField(classBuild, "RADIO",
						xPref.getString("baseband", ""));
			}
			XposedHelpers.findAndHookMethod("android.os.Build",
					loadPP.classLoader, "getRadioVersion", new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);
							param.setResult(xPref.getString("baseband", ""));
						}

					});

		} catch (Exception e) {
			// TODO: handle exception
		}
		// /////

		// Change Serial Number
		try {
			Class<?> classBuild = XposedHelpers.findClass("android.os.Build",
					loadPP.classLoader);
			XposedHelpers.setStaticObjectField(classBuild, "SERIAL",
					xPref.getString("serial", ""));
			Class<?> classSysProp = Class
					.forName("android.os.SystemProperties");
			XposedHelpers.findAndHookMethod(classSysProp, "get", String.class,
					new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);

							String serialno = (String) param.args[0];
							if (serialno.equals("ro.serialno")
									|| serialno.equals("ro.boot.serialno")
									|| serialno.equals("ril.serialnumber")
									|| serialno.equals("sys.serialnumber")) {
								param.setResult(xPref.getString("serial", ""));
							}
						}

					});
			XposedHelpers.findAndHookMethod(classSysProp, "get", String.class,
					String.class, new XC_MethodHook() {

						@Override
						protected void afterHookedMethod(MethodHookParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHookedMethod(param);

							String serialno = (String) param.args[0];
							if (serialno.equals("ro.serialno")
									|| serialno.equals("ro.boot.serialno")
									|| serialno.equals("ril.serialnumber")
									|| serialno.equals("sys.serialnumber")) {
								param.setResult(xPref.getString("serial", ""));
							}
						}

					});
			return;

		} catch (IllegalArgumentException ill) {
			ill.printStackTrace();
			return;
		} catch (ClassNotFoundException classNF) {
			classNF.printStackTrace();
		}
		//////
	}
}

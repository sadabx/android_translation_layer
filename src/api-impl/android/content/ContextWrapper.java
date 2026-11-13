package android.content;

import android.atl.ATLLoadedApp;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.view.Display;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ContextWrapper extends Context {
	protected Context baseContext;

	public ContextWrapper(Context baseContext) {
		this.baseContext = baseContext;
	}

	public Context getBaseContext() {
		return baseContext;
	}

	protected void attachBaseContext(Context baseContext) {
		if (this.baseContext != null) {
			throw new IllegalStateException("Base context already set");
		}
		this.baseContext = baseContext;
	}

	public void atl_attach_base_context(Context baseContext) {
		Objects.requireNonNull(baseContext, "baseContext must not be null");
		this.attachBaseContext(baseContext);
	}

	@Override
	public Resources.Theme getTheme() {
		return this.baseContext.getTheme();
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		if (this.baseContext != null) {
			return this.baseContext.getApplicationInfo();
		}
		ATLLoadedApp app = ATLLoadedApp.getPrimaryApplication();
		if (app != null && app.pkg != null) {
			return app.pkg.applicationInfo;
		}
		return null;
	}

	@Override
	public Object getSystemService(String name) {
		return this.baseContext.getSystemService(name);
	}

	@Override
	public Object getSystemService(Class<?> serviceClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		return this.baseContext.getSystemService(serviceClass);
	}

	@Override
	public Resources getResources() {
		if (this.baseContext != null) {
			return this.baseContext.getResources();
		}
		ATLLoadedApp app = ATLLoadedApp.getPrimaryApplication();
		if (app != null) {
			return app.default_resources;
		}
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		if (this.baseContext != null) {
			return this.baseContext.getClassLoader();
		}
		ATLLoadedApp app = ATLLoadedApp.getPrimaryApplication();
		if (app != null) {
			return app.class_loader;
		}
		return ClassLoader.getSystemClassLoader();
	}

	@Override
	public ComponentName startService(Intent intent) {
		return this.baseContext.startService(intent);
	}

	@Override
	public boolean bindService(Intent intent, ServiceConnection serviceConnection, int flags) {
		return this.baseContext.bindService(intent, serviceConnection, flags);
	}

	@Override
	public void startActivity(Intent intent) {
		this.baseContext.startActivity(intent);
	}

	@Override
	public void setTheme(int resId) {
		this.baseContext.setTheme(resId);
	}

	@Override
	public boolean isRestricted() {
		return this.baseContext.isRestricted();
	}

	@Override
	public boolean stopService(Intent intent) {
		return this.baseContext.stopService(intent);
	}

	@Override
	public Context createPackageContext(String packageName, int flags) {
		return this.baseContext.createPackageContext(packageName, flags);
	}

	@Override
	public Context createConfigurationContext(Configuration configuration) {
		return this.baseContext.createConfigurationContext(configuration);
	}

	@Override
	public Context createDisplayContext(Display display) {
		return this.baseContext.createDisplayContext(display);
	}

	@Override
	public boolean isDeviceProtectedStorage() {
		return this.baseContext.isDeviceProtectedStorage();
	}

	@Override
	public Context createDeviceProtectedStorageContext() {
		return this.baseContext.createDeviceProtectedStorageContext();
	}

	@Override
	public int getThemeResId() {
		return this.baseContext.getThemeResId();
	}

	@Override
	public ATLLoadedApp get_atl_loaded_app() {
		if (this.baseContext != null) {
			return this.baseContext.get_atl_loaded_app();
		}
		return ATLLoadedApp.getPrimaryApplication();
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		if (this.baseContext != null) {
			return this.baseContext.registerReceiver(receiver, filter);
		}
		return super.registerReceiver(receiver, filter);
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, int flags) {
		if (this.baseContext != null) {
			return this.baseContext.registerReceiver(receiver, filter, flags);
		}
		return super.registerReceiver(receiver, filter, flags);
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
		if (this.baseContext != null) {
			return this.baseContext.registerReceiver(receiver, filter, broadcastPermission, scheduler);
		}
		return super.registerReceiver(receiver, filter, broadcastPermission, scheduler);
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler, int flags) {
		if (this.baseContext != null) {
			return this.baseContext.registerReceiver(receiver, filter, broadcastPermission, scheduler, flags);
		}
		return super.registerReceiver(receiver, filter, broadcastPermission, scheduler, flags);
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		if (this.baseContext != null) {
			this.baseContext.unregisterReceiver(receiver);
		} else {
			super.unregisterReceiver(receiver);
		}
	}
}

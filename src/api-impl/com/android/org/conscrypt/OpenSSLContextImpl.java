package com.android.org.conscrypt;

import java.security.KeyManagementException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class OpenSSLContextImpl extends SSLContextSpi {
	private SSLContext delegateContext;
	private X509TrustManager trustManager;
	private boolean initialized = false;

	public OpenSSLContextImpl() {
		try {
			for (Provider p : Security.getProviders()) {
				if (!p.getName().equals("Conscrypt") && !p.getName().equals("AtlConscrypt")) {
					try {
						delegateContext = SSLContext.getInstance("TLS", p);
						break;
					} catch (Exception ignored) {}
				}
			}
			if (delegateContext == null) {
				delegateContext = SSLContext.getInstance("TLS");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private synchronized void ensureInitialized() {
		if (!initialized) {
			try {
				delegateContext.init(null, null, null);
				initialized = true;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected synchronized void engineInit(KeyManager[] km, TrustManager[] tm, SecureRandom sr) throws KeyManagementException {
		if (tm != null) {
			for (TrustManager t : tm) {
				if (t instanceof X509TrustManager) {
					this.trustManager = (X509TrustManager) t;
					break;
				}
			}
		}
		delegateContext.init(km, tm, sr);
		initialized = true;
	}

	@Override
	protected SSLSocketFactory engineGetSocketFactory() {
		ensureInitialized();
		return new OpenSSLSocketFactoryImpl(delegateContext.getSocketFactory(), trustManager);
	}

	@Override
	protected SSLServerSocketFactory engineGetServerSocketFactory() {
		ensureInitialized();
		return delegateContext.getServerSocketFactory();
	}

	@Override
	protected SSLEngine engineCreateSSLEngine() {
		ensureInitialized();
		return delegateContext.createSSLEngine();
	}

	@Override
	protected SSLEngine engineCreateSSLEngine(String host, int port) {
		ensureInitialized();
		return delegateContext.createSSLEngine(host, port);
	}

	@Override
	protected SSLSessionContext engineGetServerSessionContext() {
		ensureInitialized();
		return delegateContext.getServerSessionContext();
	}

	@Override
	protected SSLSessionContext engineGetClientSessionContext() {
		ensureInitialized();
		return delegateContext.getClientSessionContext();
	}
}

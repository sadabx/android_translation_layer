package org.apache.harmony.xnet.provider.jsse;

import javax.net.ssl.X509TrustManager;

public class SSLParametersImpl {
	public X509TrustManager x509TrustManager;

	public X509TrustManager getX509TrustManager() {
		return x509TrustManager;
	}

	public void setX509TrustManager(X509TrustManager tm) {
		this.x509TrustManager = tm;
	}
}

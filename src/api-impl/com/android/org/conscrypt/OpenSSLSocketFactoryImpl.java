package com.android.org.conscrypt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class OpenSSLSocketFactoryImpl extends SSLSocketFactory {
	public SSLParametersImpl sslParameters = new SSLParametersImpl();
	private final SSLSocketFactory delegate;

	public OpenSSLSocketFactoryImpl() {
		this(null, null);
	}

	public OpenSSLSocketFactoryImpl(SSLSocketFactory delegate, X509TrustManager tm) {
		this.delegate = delegate;
		this.sslParameters.x509TrustManager = tm;
	}

	private SSLSocketFactory getDelegate() {
		if (delegate != null) {
			return delegate;
		}
		return (SSLSocketFactory) SSLSocketFactory.getDefault();
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return getDelegate().getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return getDelegate().getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		return getDelegate().createSocket(s, host, port, autoClose);
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return getDelegate().createSocket(host, port);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
		return getDelegate().createSocket(host, port, localHost, localPort);
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return getDelegate().createSocket(host, port);
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
		return getDelegate().createSocket(address, port, localAddress, localPort);
	}
}

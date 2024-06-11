package com.arthur.plugin.ssl.support;

import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

/**
 * 吊销证书检查，判断证书是否过期
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-27
 * @see SslHttpClientSslConfigurer#setTrustManager(SslContextBuilder, X509Certificate...)
 * @see SslHttpClientSslConfigurer#setTrustManager(SslContextBuilder, TrustManagerFactory)
 * @since 1.0
 */
public class CrlTrustManager implements X509TrustManager {

	private final X509CRL crl;
	private X509Certificate[] certs;
	private final X509TrustManager delegate;

	public CrlTrustManager(X509CRL crl, X509TrustManager delegate) {
		this.crl = crl;
		this.delegate = delegate;
	}

	public CrlTrustManager(X509CRL crl, X509Certificate[] certs, X509TrustManager delegate) {
		this(crl, delegate);
		this.certs = certs;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		delegate.checkClientTrusted(chain, authType);
		for (X509Certificate cert : chain) {
			if (crl.isRevoked(cert)) {
				throw new CertificateException("Certificate revoked: " + cert.getSubjectX500Principal());
			}
		}
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		delegate.checkClientTrusted(chain, authType);
		for (X509Certificate cert : chain) {
			if (crl.isRevoked(cert)) {
				throw new CertificateException("Certificate revoked: " + cert.getSubjectX500Principal());
			}
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		if (certs != null) {
			return certs;
		}
		return delegate.getAcceptedIssuers();
	}

}

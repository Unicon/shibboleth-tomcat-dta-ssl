package edu.internet2.middleware.security.tomcat7;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.ServerSocketFactory;
import org.apache.tomcat.util.net.jsse.JSSEImplementation;
import org.apache.tomcat.util.net.jsse.JSSESocketFactory;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * A Tomcat JSSE implementation that delegates client certificate verification and validation to the application.
 * <p>
 * The underlying {@link javax.net.ssl.X509TrustManager} accepts any client/server certificate. Therefore, no
 * <code>truststoreFile</code> parameter is needed.
 */
public class DelegateToApplicationJSSEImplementation extends JSSEImplementation {
    private static Log log = LogFactory.getLog(DelegateToApplicationJSSEImplementation.class);

    public DelegateToApplicationJSSEImplementation() {
        super();
    }

    @Override
    public String getImplementationName() {
        return super.getImplementationName() + "-DelegateToApplication";
    }

    @Override
    public ServerSocketFactory getServerSocketFactory(AbstractEndpoint endpoint) {
        return new NoTrustSocketFactory(endpoint);
    }

    public class NoTrustSocketFactory extends JSSESocketFactory {
        private TrustManager[] trustManagers;

        public NoTrustSocketFactory(AbstractEndpoint endpoint) {
            super(endpoint);

            X509TrustManager noTrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            trustManagers = new TrustManager[] {noTrustManager};
        }

        @Override
        public TrustManager[] getTrustManagers() throws Exception {
            return trustManagers;
        }
    }
}

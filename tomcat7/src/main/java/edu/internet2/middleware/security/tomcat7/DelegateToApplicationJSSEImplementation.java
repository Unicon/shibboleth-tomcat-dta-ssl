/*
 * Licensed to the University Corporation for Advanced Internet Development,
 * Inc. (UCAID) under one or more contributor license agreements.  See the
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

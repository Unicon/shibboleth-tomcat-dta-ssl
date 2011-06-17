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

package edu.internet2.middleware.security.tomcat6;

import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.ServerSocketFactory;
import org.apache.tomcat.util.net.jsse.JSSEImplementation;
import org.apache.tomcat.util.net.jsse.JSSESocketFactory;

/**
 * A Tomcat JSSE implementation that delegates client certificate verification and validation to the application.
 * <p>
 * The underlying {@link X509TrustManager} accepts any client/server certificate. Therefore, no
 * <code>truststoreFile</code> parameter is needed.
 */
public class DelegateToApplicationJSSEImplementation extends JSSEImplementation {

    /** Class logger. */
    private static Log log = LogFactory.getLog(DelegateToApplicationJSSEImplementation.class);

    /** The 'trust any cert' socket factory */
    private NoTrustSocketFactory socketFactory = null;

    /**
     * Constructor.
     * 
     * @throws ClassNotFoundException if the socket factory is not found in classpath.
     */
    public DelegateToApplicationJSSEImplementation() throws ClassNotFoundException {
        super();
        socketFactory = new NoTrustSocketFactory();
        log.debug("Loaded DelegateToApplicationJSSEImplementation");
    }

    /** {@inheritDoc} */
    public String getImplementationName() {
        return super.getImplementationName() + "-DelegateToApplication";
    }

    /** {@inheritDoc} */
    public ServerSocketFactory getServerSocketFactory() {
        return socketFactory;
    }

    /** {@link JSSESocketFactory} that accepts any certificate presented. */
    public class NoTrustSocketFactory extends JSSESocketFactory {

        /** Trust manager */
        private TrustManager[] trustManagers;

        /** Constructor. */
        public NoTrustSocketFactory() {
            super();

            X509TrustManager noTrustManager = new X509TrustManager() {

                /** {@inheritDoc} */
                public void checkClientTrusted(X509Certificate[] certs, String auth) {
                    log.debug("NoTrustSocketFactory#checkClientTrusted invoked");
                }

                /** {@inheritDoc} */
                public void checkServerTrusted(X509Certificate[] certs, String auth) {
                    log.debug("NoTrustSocketFactory#checkServerTrusted invoked");
                }

                /** {@inheritDoc} */
                public X509Certificate[] getAcceptedIssuers() {
                    log.debug("NoTrustSocketFactory#getAcceptedIssuers invoked");
                    return (new X509Certificate[] {});
                }
            };

            trustManagers = new TrustManager[] { noTrustManager };
        }

        /** {@inheritDoc} */
        protected TrustManager[] getTrustManagers(String arg0, String arg1, String arg2) throws Exception {
            return trustManagers;
        }
    }
}
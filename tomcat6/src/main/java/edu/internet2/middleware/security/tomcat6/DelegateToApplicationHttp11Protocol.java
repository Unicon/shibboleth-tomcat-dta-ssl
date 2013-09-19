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

import java.util.Map.Entry;

import org.apache.coyote.http11.Http11Protocol;

/**
 * An extension to the standard Tomcat {@link Http11Protocol} that:
 * <ul>
 *  <li>forces the SSLEnabled property to 'true'</li>
 *  <li>forces clientAuth property to 'true'</li>
 *  <li>forces socketFactory property to null</li>
 *  <li>forces SSLImplementation property to 'edu.internet2.middleware.security.tomcat6.DelegateToApplicationJSSEImplementation</li>
 *  <li>disables the ability to override any of these settings</li>
 * </ul>
 * 
 * This protocol, as its name implies, delegates evaluation of a client certificate, used for authentication, to the 
 * application receiving the request.  Usage of this protocol with an application that does not perform trust validation
 * will result in an untrustworthy connection.
 */
public class DelegateToApplicationHttp11Protocol extends Http11Protocol {

    /** {@inheritDoc} */
    public void init() throws Exception {
        sslImplementation = new DelegateToApplicationJSSEImplementation();
        
        socketFactory = sslImplementation.getServerSocketFactory();
        if (attributes != null) {
            for (Entry<String, Object> attribute : attributes.entrySet()) {
                socketFactory.setAttribute(attribute.getKey(), attribute.getValue());
            }
        }

        try {
            endpoint.setName(getName());
            endpoint.setServerSocketFactory(socketFactory);
            endpoint.setHandler(cHandler);
            endpoint.init();
        } catch (Exception ex) {
            log.error(sm.getString("http11protocol.endpoint.initerror"), ex);
            throw ex;
        }
        if (log.isInfoEnabled())
            log.info(sm.getString("http11protocol.init", getName()));
    }
    
    /** {@inheritDoc} */
    public boolean getSecure() {
        return true;
    }
    
    /** 
     * {@inheritDoc} 
     * 
     * This method is not supported by this protocol
     */
    public void setSecure(boolean b) {
        throw new UnsupportedOperationException("Setting of 'secure' property is not supportted by this protocol");
    }
    
    /** {@inheritDoc} */
    public String getClientauth() {
        return Boolean.TRUE.toString();
    }
    
    /** 
     * {@inheritDoc} 
     * 
     * This method is not supported by this protocol
     */
    public void setClientauth(String k) {
        throw new UnsupportedOperationException("Setting of 'clientAuth' property is not supportted by this protocol");
    }
    
    /** {@inheritDoc} */
    public boolean isSSLEnabled() {
        return true;
    }
    
    /** 
     * {@inheritDoc} 
     * 
     * This method is not supported by this protocol
     */
    public void setSSLEnabled(boolean SSLEnabled) {
        throw new UnsupportedOperationException("Setting of 'SSLEnabled' property is not supportted by this protocol");
    }
    
    /** {@inheritDoc} */
    public String getSSLImplementation() {
        return DelegateToApplicationJSSEImplementation.class.getName();
    }
    
    /** 
     * {@inheritDoc} 
     * 
     * This method is not supported by this protocol
     */
    public void setSSLImplementation(String valueS) {
        throw new UnsupportedOperationException("Setting of 'SSLImplementation' property is not supportted by this protocol");
    }
    
    /** {@inheritDoc} */
    public String getSocketFactory() {
        return null;
    }
    
    /** 
     * {@inheritDoc} 
     * 
     * This method is not supported by this protocol
     */
    public void setSocketFactory(String valueS) {
        throw new UnsupportedOperationException("Setting of 'socketFactory' property is not supportted by this protocol");
    }
}
# Shibboleth JSSEImplementation for Supporting SOAP Endpoints in Tomcat 6

See [Preparing Apache Tomcat for the Shibboleth Identity Provider](https://wiki.shibboleth.net/confluence/display/SHIB2/IdPApacheTomcatPrepare)
for more information.

## Introduction

Shibboleth IdPs and SP may communicate directly, as opposed to sending messages via the user's browser, during certain
operations (Attribute Query, Artifact Resolution, and Logout). In order to support these request the IdP needs an
additional port (called a Connector within the Tomcat configuration), distinct from the one used by the user (because
they have different, mutually exclusive, security requirements).

This is a simple JSSE implementation that requires client cert authentication but delegates all of the trust
verification of the certificate to the application that received the request.

Use of this plugin with an application that does not validate the trustworthiness of a provided client certificate will
lead to insecure code.

## Usage

1. Download or compile the `tomcat6-{version}.jar` file. Find the files at [github](https://github.com/Unicon/shibboleth-tomcat-dta-ssl/releases).
2. Place the file in the `{TOMCAT_HOME}/lib` directory.
3. Configure the connector for SOAP connections to use the custom implementation. You should add the following to
`{TOMCAT_HOME}/conf/server.xml`. **DO NOT USE FOR YOUR BROWSER CLIENT CONNECTOR!**

        <Connector port="8443"
            protocol="org.apache.coyote.http11.Http11Protocol"
            SSLImplementation="edu.internet2.middleware.security.tomcat6.DelegateToApplicationJSSEImplementation"
            scheme="https"
            SSLEnabled="true"
            clientAuth="want"
            keystoreFile="{IDP_HOME}/credentials/idp.jks"
            keystorePass="{PASSWORD}" />

4. Replace `{IDP_HOME}` with the IdP home directory entered during installation
5. Replace `{PASSWORD}` with the password for the IdP key entered during installation.
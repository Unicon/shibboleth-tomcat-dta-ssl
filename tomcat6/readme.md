This is a simple JSSE implementation that requires client cert authentication but delegates
all of the trust verification of the certificate to the application that received the request.

This package also contains an extension to the Http11Protocol class that forces the use of the
aforementioned JSSE implementation.  Use of this prevents the need for, and possible typos in,
the various SSL related properties that must be set on the standard Http11Protocol in order to
enable the JSSE implementation.

PLEASE READ THE ABOVE AGAIN.  Use of this plugin with an application that does not
validate the trustworthiness of a provided client certificate will lead to insecure

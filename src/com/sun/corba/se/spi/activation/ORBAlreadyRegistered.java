package com.sun.corba.se.spi.activation;


/**
 * com/sun/corba/se/spi/activation/ORBAlreadyRegistered.java . Generated by the IDL-to-Java compiler
 * (portable), version "3.2" from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u60/4407/corba/src/share/classes/com/sun/corba/se/spi/activation/activation.idl
 * Tuesday, August 4, 2015 11:07:52 AM PDT
 */

public final class ORBAlreadyRegistered extends org.omg.CORBA.UserException {

  public String orbId = null;

  public ORBAlreadyRegistered() {
    super(ORBAlreadyRegisteredHelper.id());
  } // ctor

  public ORBAlreadyRegistered(String _orbId) {
    super(ORBAlreadyRegisteredHelper.id());
    orbId = _orbId;
  } // ctor


  public ORBAlreadyRegistered(String $reason, String _orbId) {
    super(ORBAlreadyRegisteredHelper.id() + "  " + $reason);
    orbId = _orbId;
  } // ctor

} // class ORBAlreadyRegistered

/*
 * Copyright (c) 1996, 1998, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.security.interfaces;

/**
 * The interface to a DSA public or private key. DSA (Digital Signature
 * Algorithm) is defined in NIST's FIPS-186.
 *
 * @author Benjamin Renaud
 * @author Josh Bloch
 * @see DSAParams
 * @see java.security.Key
 * @see java.security.Signature
 */
public interface DSAKey {

  /**
   * Returns the DSA-specific key parameters. These parameters are
   * never secret.
   *
   * @return the DSA-specific key parameters.
   * @see DSAParams
   */
  public DSAParams getParams();
}

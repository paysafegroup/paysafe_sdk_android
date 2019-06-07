/*
 * Copyright (c) 2016 Paysafe
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.paysafe.common;

import com.paysafe.common.impl.BaseDomainObject;

/** The Class PaysafeException
 * @author asawari.vaidya 
 * @since 26-06-2015.
 */
public class PaysafeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/** The raw response. */
	private final BaseDomainObject rawResponse;
	
    /** The code. */
    private final String code;

    /**
    * Constructor 1
    *
    * @param message Exception Message.
    * */
    public PaysafeException(final String message) {
        this(message, null);
    } // end of Constructor

    /**
    * Constructor 2
    *
    * @param message Exception Message.
    * @param cause Exception Cause.
    * */
    public PaysafeException(final String message, final Throwable cause) {
        super(message, cause);
        rawResponse = null;
        code = null;
    } // end of Constructor

    /**
     * Constructor 3
     *
     * @param obj Base Domain Object.
     * @param cause Exception cause.
     * */
    public PaysafeException(final BaseDomainObject obj, final Throwable cause) {
        super(null == obj || null == obj.getError()
                        ? "An unknown error occurred"
                        : obj.getError().getMessage(),
                cause);
        rawResponse = obj;
        code = null == obj || null == obj.getError()
                ? null
                : obj.getError().getCode();
    } // end of Constructor

    /**
     * Get Raw Response.
     * @return Raw Response.
     */
    public final BaseDomainObject getRawResponse() {
        return rawResponse;
    } // end of getRawResponse()

    /**
     * Get Code.
     * @return Code.
     */
    public final String getCode() {
        return code;
    } // end of getCode()

} // end of class PaysafeException

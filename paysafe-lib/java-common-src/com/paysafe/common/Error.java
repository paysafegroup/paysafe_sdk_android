/*
 * Copyright (c) 2015 Optimal Payments
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

import java.util.ArrayList;

import com.paysafe.common.impl.DomainObject;

/**
 * The Class Error.
 * 
 * @author asawari.vaidya
 * @since 26-06-2015.
 * 
 */
public class Error implements DomainObject {

	/** The code */
	private String code;

	/** The message */
	private String message;

	/** The details */
	private ArrayList<String> details;
	
	/** The fieldErrors */
	private ArrayList<FieldError> fieldErrors;

	/** The links */
	private ArrayList<Link> links;

	/**
	 * Get Error Code.
	 * 
	 * @return Error Code.
	 * */
	public final String getCode() {
		return code;
	}

	/**
	 * Set Error Code.
	 * 
	 * @param code
	 *            Error Code.
	 * */
	public final void setCode(final String code) {
		this.code = code;
	}

	/**
	 * Get Error Message.
	 * 
	 * @return Error Message.
	 * */
	public final String getMessage() {
		return message;
	}

	/**
	 * Set Error Message.
	 * 
	 * @param message
	 *            Error Message
	 * */
	public final void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * Get Error Details.
	 * 
	 * @return Array List of String Objects.
	 * */
	public final ArrayList<String> getDetails() {
		return details;
	}

	/**
	 * Set Error Details.
	 * 
	 * @param details
	 *            Error Details.
	 * */
	public final void setDetails(final ArrayList<String> details) {
		this.details = details;
	}

	/**
	 * Get Field Errors.
	 * 
	 * @return Array List of FieldError Objects.
	 * */
	public final ArrayList<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	/**
	 * Set Field Errors.
	 * 
	 * @param fieldErrors
	 *            Field Errors.
	 * */
	public final void setFieldErrors(final ArrayList<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	/**
	 * Get Error Links.
	 * 
	 * @return Array List of Link Objects.
	 * */
	public final ArrayList<Link> getLinks() {
		return links;
	}

	/**
	 * Set Error Links.
	 * 
	 * @param links
	 *            Error Links.
	 * */
	public final void setLinks(final ArrayList<Link> links) {
		this.links = links;
	}
} // end of class Error

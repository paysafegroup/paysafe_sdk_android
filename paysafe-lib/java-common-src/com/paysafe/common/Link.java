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


import com.google.gson.annotations.Expose;
import com.paysafe.common.impl.DomainObject;


/**
 * The Class Link.
 * @author asawari.vaidya 
 * @since 26-06-2015.
 */
public class Link implements DomainObject {
	 @Expose
     private String rel;
	 @Expose
     private String href;

    /**
    * Get Rel.
    *
    * @return Rel.
    * */
    public final String getRel() {
        return rel;
    }

    /**
    * Set Rel.
    *
    * @param rel Rel.
    * */
    public final void setRel(final String rel) {
        this.rel = rel;
    }

    /**
    * Get Href.
    *
    * @return Href.
    * */
    public final String getHref() {
        return href;
    }

    /**
    * Set Href.
    *
    * @param href Href.
    * */
    public final void setHref(final String href) {
        this.href = href;
    }

} // end of class Link

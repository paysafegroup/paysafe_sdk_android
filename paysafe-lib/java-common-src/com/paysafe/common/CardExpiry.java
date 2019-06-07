package com.paysafe.common;

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
import com.google.gson.annotations.Expose;
import com.paysafe.common.impl.DomainObject;
import com.paysafe.common.impl.GenericBuilder;
import com.paysafe.common.impl.NestedBuilder;
/**
 * The Class CardExpiry.
 * @author asawari.vaidya
 * @since  26-06-2015
 */
public class CardExpiry implements DomainObject {
	
	/** The month. */
    @Expose
    private Integer month;
    
    /** The year. */
    @Expose
    private Integer year;

    /**
    * Get Card Expiry Month.
    *
    * @return Card Expiry Month.
    * */
    public final Integer getMonth() {
        return month;
    }

    /**
    * Set Card Expiry Month.
    *
    * @param month Card Expiry Month.
    * */
    public final void setMonth(final Integer month) {
        this.month = month;
    }

    /**
    * Get Card Expiry Year.
    *
    * @return Card Expiry Year.
    * */
    public final Integer getYear() {
        return year;
    }

    /**
    * Set Card Expiry Year.
    *
    * @param year Card Expiry Year.
    * */
    public final void setYear(final Integer year) {
        this.year = year;
    }

    /**
     * The sub-builder class for CardExpiry.
     *
     * @param <BLDRT> the parent builder
     */
    public static class CardExpiryBuilder<BLDRT extends GenericBuilder> extends
            NestedBuilder<CardExpiry, BLDRT> {

        private final CardExpiry cardExpiry = new CardExpiry();

        /**
         * Constructor.
         *
         * @param parent Parent object.
         */
        public CardExpiryBuilder(final BLDRT parent) {
            super(parent);
        }

        /**
         * Build this CardExpiry object.
         *
         * @return CardExpiry object.
         */
        @Override
        public final CardExpiry build() {
            return cardExpiry;
        }

        /**
         * Set the month property for Card Expiry.
         *
         * @param month Card Expiry Month.
         * @return CardExpiryBuilder object.
         */
        public final CardExpiryBuilder<BLDRT> month(final Integer month) {
            cardExpiry.setMonth(month);
            return this;
        }

        /**
         * Set the year property for Card Expiry.
         *
         * @param year Card Expiry Year.
         * @return CardExpiryBuilder object.
         */
        public final CardExpiryBuilder<BLDRT> year(final Integer year) {
            cardExpiry.setYear(year);
            return this;
        }
    }
} // end of class CardExpiry

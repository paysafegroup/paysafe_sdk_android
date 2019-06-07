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
package com.paysafe.customervault;



import com.google.gson.annotations.Expose;
import com.paysafe.common.impl.DomainObject;
import com.paysafe.common.impl.GenericBuilder;
import com.paysafe.common.impl.NestedBuilder;

/**
* The Class BillingAddress.
* 
* @author asawari.vaidya
* @since 26-06-2015..
*/
public class BillingAddress implements DomainObject {
	
	/** The nick name. */

	@Expose
	private String nickName;

	/** The street */
	@Expose
	private String street;

	/** The street2 */
	@Expose
	private String street2;

	/** The city */
	@Expose
	private String city;

	/** The state */
	@Expose
	private String state;

	/** The country */
	@Expose
	private String country;

	/** The zip */
	@Expose
	private String zip;

	/** The phone */
	private String phone;

    /**
    * Get Nick Name.
    *
    * @return Nick Name.
    * */
    public final String getNickName() {
        return nickName;
    }

    /**
    * Set Nick Name.
    *
    * @param nickName Nick Name
    * */
    public final void setNickName(final String nickName) {
        this.nickName = nickName;
    }

    /**
    * Get Street.
    *
    * @return Street.
    * */
    public final String getStreet() {
        return street;
    }

    /**
    * Set Street.
    *
    * @param street Street
    * */
    public final void setStreet(final String street) {
        this.street = street;
    }

    /**
    * Get Street2.
    *
    * @return Street.
    * */
    public final String getStreet2() {
        return street2;
    }

    /**
    * Set Street2.
    *
    * @param street2 Street 2
    * */
    public final void setStreet2(final String street2) {
        this.street2 = street2;
    }

    /**
    * Get City.
    *
    * @return City.
    * */
    public final String getCity() {
        return city;
    }

    /**
    * Set City.
    *
    * @param city City
    * */
    public final void setCity(final String city) {
        this.city = city;
    }

    /**
    * Get State.
    *
    * @return State.
    * */
    public final String getState() {
        return state;
    }

    /**
    * Set State.
    *
    * @param state State
    * */
    public final void setState(final String state) {
        this.state = state;
    }

    /**
    * Get Country.
    *
    * @return Country.
    * */
    public final String getCountry() {
        return country;
    }

    /**
    * Set Country.
    *
    * @param country Country
    * */
    public final void setCountry(final String country) {
        this.country = country;
    }

    /**
    * Get Zip.
    *
    * @return Zip Code.
    * */
    public final String getZip() {
        return zip;
    }

    /**
    * Set Zip.
    *
    * @param zip Zip Code
    * */
    public final void setZip(final String zip) {
        this.zip = zip;
    }

    /**
    * Get Phone.
    *
    * @return Phone.
    * */
    public final String getPhone() {
        return phone;
    }

    /**
    * Set Phone
    *
    * @param phone Phone
    * */
    public final void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * The Builder class for BillingAddress
     * */
    public static class BillingAddressBuilder<BLDRT extends GenericBuilder> extends
            NestedBuilder<BillingAddress, BLDRT> {

        private final BillingAddress billingAddress = new BillingAddress();

        /**
         * Constructor.
         *
         * @param parent Parent object.
         */
        public BillingAddressBuilder(BLDRT parent) {
            super(parent);
        }

        @Override
        public BillingAddress build() {
            return billingAddress;
        }

        /**
         * Set the Nickname property.
         *
         * @param nickName Nick Name.
         * */
        public final BillingAddressBuilder<BLDRT> nickName(final String nickName) {
            billingAddress.setNickName(nickName);
            return this;
        }

        /**
         * Set the Street property.
         *
         * @param street Street.
         * */
        public final BillingAddressBuilder<BLDRT> street(final String street) {
            billingAddress.setStreet(street);
            return this;
        }

        /**
         * Set the Street2 property.
         *
         * @param street2 Street 2.
         * */
        public final BillingAddressBuilder<BLDRT> street2(final String street2) {
            billingAddress.setStreet2(street2);
            return this;
        }

        /**
         * Set the City property.
         *
         * @param city City.
         * */
        public final BillingAddressBuilder<BLDRT> city(final String city) {
            billingAddress.setCity(city);
            return this;
        }

        /**
         * Set the State property.
         *
         * @param state State.
         * */
        public final BillingAddressBuilder<BLDRT> state(final String state) {
            billingAddress.setState(state);
            return this;
        }

        /**
         * Set the Country property.
         *
         * @param country Country.
         * */
        public final BillingAddressBuilder<BLDRT> country(final String country) {
            billingAddress.setCountry(country);
            return this;
        }

        /**
         * Set the Zip property.
         *
         * @param zip Zip Code.
         * */
        public final BillingAddressBuilder<BLDRT> zip(final String zip) {
            billingAddress.setZip(zip);
            return this;
        }

        /**
         * Set the Phone property.
         *
         * @param phone Phone.
         * */
        public final BillingAddressBuilder<BLDRT> phone(final String phone) {
            billingAddress.setPhone(phone);
            return this;
        }

    } // end of class BillingAddressBuilder

} // end of class BillingDetails

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
import com.paysafe.common.Error;
import com.paysafe.common.Id;
import com.paysafe.common.impl.BaseBuilder;
import com.paysafe.common.impl.BaseDomainObject;

/**
* The Class SingleUseToken.
* 
* @author asawari.vaidya
* @since 26-06-2015.
*/
public class SingleUseToken implements BaseDomainObject {
	
	/** The id */
	private Id<SingleUseToken> id;

	/** The card */
	@Expose
	private Card card;

    /** The Google Pay Token */
    @Expose
    private GooglePayPaymentToken googlePayPaymentToken;


	/** The profileId */
	private String profileId;

	/** The paymentToken */
	private String paymentToken;

	/** The timeToLiveSeconds */
	private Integer timeToLiveSeconds;

	/** The error */
	private Error error;

	/** The mConnectivityError */
	private String mConnectivityError;

    /**
    * Get ID
    *
    * @return Single Use Token Id.
    * */
    public final Id<SingleUseToken> getId() {
        return id;
    }

    /**
    * Set ID
    *
    * @param id Sets the Single Use Token id.
    * */
    public final void setId(final Id<SingleUseToken> id) {
        this.id = id;
    }

    /**
    * Get Card
    *
    * @return Object of Card.
    * */
    public final Card getCard() {
        return card;
    }

    /**
    * Set Card
    *
    * @param card Card Object
    * */
    public final void setCard(final Card card) {
        this.card = card;
    }

    /**
     * Get Google Pay Token
     *
     * @return Object of GooglePayPaymentToken
     */
    public final GooglePayPaymentToken getGooglePayPaymentToken() {
        return googlePayPaymentToken;
    }

    /**
     * Set Google Pay Token
     *
     * @param googlePayPaymentToken GooglePayPaymentToken Object
     */
    public final void setGooglePayPaymentToken(final GooglePayPaymentToken googlePayPaymentToken) {
        this.googlePayPaymentToken = googlePayPaymentToken;
    }

    /**
    * Get Profile Id
    *
    * @return Profile Id.
    * */
    public final String getProfileId() {
        return profileId;
    }

    /**
    * Set Profile Id
    *
    * @param profileId Profile Id
    * */
    public final void setProfileId(final String profileId) {
        this.profileId = profileId;
    }

    /**
    * Get Payment Token
    *
    * @return Payment Token
    * */
    public final String getPaymentToken() {
        return paymentToken;
    }

    /**
    * Set Payment Token
    *
    * @param paymentToken Payment Token
    * */
    public final void setPaymentToken(final String paymentToken) {
        this.paymentToken = paymentToken;
    }

    /**
    * Get Time to Live Seconds
    *
    * @return Time to live seconds.
    * */
    public final Integer getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    /**
    * Set Time to Live Seconds
    *
    * @param timeToLiveSeconds Time to live seconds
    * */
    public final void setTimeToLiveSeconds(final Integer timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    /**
    * Get Error
    *
    * @return Object of Error.
    * */
    public Error getError() {
        return error;
    }

    /**
    * Set Error
    *
    * @param error Error object
    * */
    public final void setError(final Error error) {
        this.error = error;
    }

	 public String getConnectivityError() {
		return mConnectivityError;
	}

	public void setConnectivityError(String mConnectivityError) {
		this.mConnectivityError = mConnectivityError;
	}

    /**
     * Get a SingleUseTokenBuilder
     *
     * @return SingleUseTokenBuilder object.
     * */
    public static SingleUseTokenBuilder builder() {
        return new SingleUseToken().new SingleUseTokenBuilder();
    } // end of builder()


    /**
     * The Builder class for SingleUseToken
     * */
    public class SingleUseTokenBuilder implements BaseBuilder<SingleUseToken> {

        private final SingleUseToken singleUseToken = new SingleUseToken();
        private Card.CardBuilder<SingleUseTokenBuilder> cardBuilder;
        private GooglePayPaymentToken.GooglePayPaymentTokenBuilder<SingleUseTokenBuilder>
                googlePayPaymentTokenBuilder;

        @Override
        public final SingleUseToken build() {
            if(null != cardBuilder) {
                singleUseToken.setCard(cardBuilder.build());
            }
            if(null != googlePayPaymentTokenBuilder) {
                singleUseToken.setGooglePayPaymentToken(googlePayPaymentTokenBuilder.build());
            }
            return singleUseToken;
        } // end of build()

        /**
         * Set the Id property
         *
         * @param id Single Use Token Id
         * @return SingleUseTokenBuilder object.
         * */
        public final SingleUseTokenBuilder id(final Id<SingleUseToken> id) {
            singleUseToken.setId(id);
            return this;
        }

        /**
         * Set the ProfileId property
         *
         * @param profileId Profile Id
         * @return SingleUseTokenBuilder object.
         * */
        public final SingleUseTokenBuilder profileId(final String profileId) {
            singleUseToken.setProfileId(profileId);
            return this;
        }

        /**
         * Set the property Card
         *
         * @return Card.CardBuilder<SingleUseTokenBuilder> object.
         * */
        public final Card.CardBuilder<SingleUseTokenBuilder> card() {
            if(null == cardBuilder) {
                cardBuilder = new Card.CardBuilder<>(this);
            }
            return cardBuilder;
        }

        /**
         * Set the property GooglePayPaymentToken
         *
         * @return GooglePayPaymentToken.GooglePayPaymentTokenBuilder<SingleUseTokenBuilder>
         *     object.
         * */
        public final GooglePayPaymentToken.GooglePayPaymentTokenBuilder<SingleUseTokenBuilder>
        googlePayPaymentToken() {
            if(null == googlePayPaymentTokenBuilder) {
                googlePayPaymentTokenBuilder = new
                        GooglePayPaymentToken.GooglePayPaymentTokenBuilder<>
                        (this);
            }
            return googlePayPaymentTokenBuilder;
        }

        /**
         * Set the paymentToken property
         *
         * @param paymentToken Payment Token
         * @return SingleUseTokenBuilder object.
         * */
        public final SingleUseTokenBuilder payment(final String paymentToken) {
            singleUseToken.setPaymentToken(paymentToken);
            return this;
        }

        /**
         * Set the timeToLiveSeconds property
         *
         * @param timeToLiveSeconds Time to live seconds
         * @return SingleUseTokenBuilder object.
         * */
        public final SingleUseTokenBuilder timeToLiveSeconds(
                final Integer timeToLiveSeconds) {
            singleUseToken.setTimeToLiveSeconds(timeToLiveSeconds);
            return this;
        }

    } // end of class SingleUseTokenBuilder

} // end of class SingleUseToken

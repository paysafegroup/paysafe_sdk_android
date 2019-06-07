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
import com.paysafe.common.CardExpiry;
import com.paysafe.common.Error;
import com.paysafe.common.Id;
import com.paysafe.common.impl.BaseDomainObject;
import com.paysafe.common.impl.GenericBuilder;
import com.paysafe.common.impl.NestedBuilder;
/**
* The Class Card.
* 
* @author asawari.vaidya
* @since 26-06-2015.
*/
public class Card implements BaseDomainObject {

	/** The id */
	private Id<Card> id;

	/** The nickName */
	@Expose
	private String nickName;

	/** The status */
	private String status;

	/** The merchantRefNum */
	@Expose
	private String merchantRefNum;

	/** The holderName */
	@Expose
	private String holderName;

	/** The cardNum */
	@Expose
	private String cardNum;

	/** The cardBin */
	private String cardBin;

	/** The cvv */
    	@Expose
	private String cvv;

	/** The track1 */
	private String track1;

	/** The track2 */
	private String track2;

	/** The lastDigits */
	private String lastDigits;

	/** The cardExpiry */
	@Expose
	private CardExpiry cardExpiry;

	/** The cardType */
	private String cardType;

	/** The billingAddress */
	@Expose
	private BillingAddress billingAddress;

	/** The billingAddressId */
	@Expose
	private String billingAddressId;

	/** The defaultCardIndicator */
	private Boolean defaultCardIndicator;

	/** The paymentToken */
	@Expose
	private String paymentToken;

	/** The singleUseToken */
	@Expose
	private String singleUseToken;

    /**
    * Get Id
    *
    * @return Card Id.
    * */
    public final Id<Card> getId() {
        return id;
    }

    /**
    * Set Id
    *
    * @param id Card Id.
    * */
    public final void setId(final Id<Card> id) {
        this.id = id;
    }

    /**
    * Get Nick Name
    *
    * @return Nick Name.
    * */
    public final String getNickName() {
        return nickName;
    }

    /**
    * Set Nick Name
    *
    * @param nickName Nick Name
    * */
    public final void setNickName(final String nickName) {
        this.nickName = nickName;
    }

    /**
    * Get Status
    *
    * @return Status.
    * */
    public final String getStatus() {
        return status;
    }

    /**
    * Set Status
    *
    * @param status Status
    * */
    public final void setStatus(final String status) {
        this.status = status;
    }

    /**
    * Get Merchant Reference Number
    *
    * @return Merchant Reference Number.
    * */
    public final String getMerchantRefNum() {
        return merchantRefNum;
    }

    /**
    * Set Merchant Reference Number
    *
    * @param merchantRefNum Merchant Reference Number
    * */
    public final void setMerchantRefNum(final String merchantRefNum) {
        this.merchantRefNum = merchantRefNum;
    }

    /**
    * Get Holder Name
    *
    * @return Card Holder Name.
    * */
    public final String getHolderName() {
        return holderName;
    }

    /**
    * Set Holder Name
    *
    * @param holderName Card Holder Name
    * */
    public final void setHolderName(final String holderName) {
        this.holderName = holderName;
    }

    /**
    * Get Card Number
    *
    * @return Card Number.
    * */
    public final String getCardNum() {
        return cardNum;
    }

    /**
    * Set Card Number
    *
    * @param cardNum Card Number
    * */
    public final void setCardNum(final String cardNum) {
        this.cardNum = cardNum;
    }

    /**
    * Get Card Bin
    *
    * @return Card Bin.
    * */
    public final String getCardBin() {
        return cardBin;
    }

    /**
    *
    * Set Card Bin
    *
    * @param cardBin Card Bin
    * */
    public final void setCardBin(final String cardBin) {
        this.cardBin = cardBin;
    }

    /**
    * Get Cvv
    *
    * @return Cvv.
    * */
    public final String getCvv() {
        return cvv;
    }

    /**
    * Set Cvv
    *
    * @param cvv Card CVV
    * */
    public final void setCvv(final String cvv) {
        this.cvv = cvv;
    }

    /**
    * Get Track  1
    *
    * @return Track 1.
    * */
    public final String getTrack1() {
        return track1;
    }

    /**
    * Set Track 1
    *
    * @param track1 Track 1
    * */
    public final void setTrack1(final String track1) {
        this.track1 = track1;
    }

    /**
    * Get Track 2
    *
    * @return Track 2.
    * */
    public final String getTrack2() {
        return track2;
    }

    /**
    * Set Track 2
    *
    * @param track2 Track 2
    * */
    public final void setTrack2(final String track2) {
        this.track2 = track2;
    }

    /**
    * Get Last Digits
    *
    * @return Last Digits of Card Number.
    * */
    public final String getLastDigits() {
        return lastDigits;
    }

    /**
    * Set Last Digits
    *
    * @param lastDigits Last 4 digits of Card Number
    * */
    public final void setLastDigits(final String lastDigits) {
        this.lastDigits = lastDigits;
    }

    /**
    * Get Card Expiry
    *
    * @return Object of CardExpiry.
    * */
    public final CardExpiry getCardExpiry() {
        return cardExpiry;
    }

    /**
    * Set CardExpiry
    *
    * @param cardExpiry Card Expiry object
    * */
    public final void setCardExpiry(final CardExpiry cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    /**
    * Get Card Type
    *
    * @return Card Type.
    * */
    public final String getCardType() {
        return cardType;
    }

    /**
    * Set Card Type
    *
    * @param cardType Card Type
    * */
    public final void setCardType(final String cardType) {
        this.cardType = cardType;
    }

    /**
    * Get Billing Address
    *
    * @return Object of BillingAddress.
    * */
    public final BillingAddress getBillingAddress() {
        return billingAddress;
    }

    /**
    * Set Billing Address
    *
    * @param billingAddress Billing Address object
    * */
    public final void setBillingAddress(final BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
    * Get Billing Address Id
    *
    * @return Billing Address Id.
    * */
    public final String getBillingAddressId() {
        return billingAddressId;
    }

    /**
    * Set Billing Address Id
    *
    * @param billingAddressId Billing Address Id
    * */
    public final void setBillingAddressId(final String billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    /**
    * Get Default Card Indicator
    *
    * @return Default Card Indicator.
    * */
    public final Boolean getDefaultCardIndicator() {
        return defaultCardIndicator;
    }

    /**
    * Set Default Card Indicator
    *
    * @param defaultCardIndicator Default Card Indicator
    * */
    public final void setDefaultCardIndicator(final Boolean defaultCardIndicator) {
        this.defaultCardIndicator = defaultCardIndicator;
    }

    /**
    * Get Payment Token
    *
    * @return Payment Token.
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
    * Get Single Use Token
    *
    * @return Single Use Token.
    * */
    public final String getSingleUseToken() {
        return singleUseToken;
    }

    /**
    * Set Single Use Token
    *
    * @param singleUseToken Single Use Token
    * */
    public final void setSingleUseToken(final String singleUseToken) {
        this.singleUseToken = singleUseToken;
    }

    @Override
    public Error getError() {
        return null;
    }

    /**
     * The builder class for Card.
     * */
    public static class CardBuilder<BLDRT extends GenericBuilder> extends NestedBuilder<Card, BLDRT> {

        private final Card card = new Card();
        private CardExpiry.CardExpiryBuilder<CardBuilder<BLDRT>> cardExpiryBuilder;
        private BillingAddress.BillingAddressBuilder<CardBuilder<BLDRT>> billingAddressBuilder;

        /**
         * Constructor
         * @param parent Parent object.
         */
        public CardBuilder(final BLDRT parent) {
            super(parent);
        }

        /**
         * Build this card object.
         *
         * @return Card object.
         */
        @Override
        public final Card build() {
            if (null != cardExpiryBuilder) {
                card.setCardExpiry(cardExpiryBuilder.build());
            }
            if (null != billingAddressBuilder) {
                card.setBillingAddress(billingAddressBuilder.build());
            }
            return card;
        } // end of build

        /**
         * Set the id property.
         *
         * @param id Card Id.
         * @return CardBuilder object.
         */
        public final CardBuilder<BLDRT> id(final Id<Card> id) {
            card.setId(id);
            return this;
        }

        /**
         * Set the nickName property.
         *
         * @param nickName Nick Name.
         * @return CardBuilder object.
         */
        public final CardBuilder<BLDRT> nickName(final String nickName) {
            card.setNickName(nickName);
            return this;
        }

        /**
         * Set the billingAddress property.
         *
         * @return BillingAddressBuilder object.
         * */
        public final BillingAddress.BillingAddressBuilder<CardBuilder<BLDRT>> billingAddress() {
            if(null == billingAddressBuilder) {
                billingAddressBuilder = new
                        BillingAddress.BillingAddressBuilder<>(this);
            }
            return billingAddressBuilder;
        }

        /**
         * Set the merchantRefNum property.
         *
         * @param merchantRefNum Merchant Reference Number.
         * @return CardBuilder object.
         */
        public final CardBuilder<BLDRT> merchantRefNum(final String merchantRefNum) {
            card.setMerchantRefNum(merchantRefNum);
            return this;
        }

        /**
         * Set the holderName property.
         *
         * @param holderName Card Holder Name.
         * @return CardBuilder object.
         */
        public final CardBuilder<BLDRT> holderName(final String holderName) {
            card.setHolderName(holderName);
            return this;
        }

        /**
         * Set the cardNum property.
         *
         * @param cardNum Card Number.
         * @return CardBuilder object.
         */
        public final CardBuilder<BLDRT> cardNum(final String cardNum) {
            card.setCardNum(cardNum);
            return this;
        }


        /**
         * Set the cvv property.
         ** @return CardBuilder object.
         */
        public final CardBuilder<BLDRT> cvv(final String cvv) {
            card.setCvv(cvv);
            return this;
        }

        /**
         * Build a cardExpiry within this card.
         *
         * @return Card Expiry Builder object.
         */
        public final CardExpiry.CardExpiryBuilder<CardBuilder<BLDRT>> cardExpiry() {
            if (null == cardExpiryBuilder) {
                cardExpiryBuilder = new CardExpiry.CardExpiryBuilder<>(this);
            }
            return cardExpiryBuilder;
        }

        /**
         * Set the billingAddressId property.
         *
         * @param billingAddressId Billing Address Id.
         * @return CardBuilder object.
         */
        public final CardBuilder<BLDRT> billingAddressId(final String billingAddressId) {
            card.setBillingAddressId(billingAddressId);
            return this;
        }
    }
} // end of class Card

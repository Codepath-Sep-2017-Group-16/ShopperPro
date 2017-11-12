package com.codepath.socialshopper.socialshopper.Activities;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.codepath.socialshopper.socialshopper.R;
import com.codepath.socialshopper.socialshopper.Utils.DatabaseUtils;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.InstrumentInfo;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.stripe.android.model.StripePaymentSource;
import com.stripe.wrap.pay.AndroidPayConfiguration;
import com.stripe.wrap.pay.activity.StripeAndroidPayActivity;

public class PaymentActivity extends StripeAndroidPayActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

    }

    @Override
    protected void onAndroidPayAvailable() {

    }

    @Override
    protected void onAndroidPayNotAvailable() {

    }

    @Override
    protected void addBuyButtonWalletFragment(@NonNull SupportWalletFragment walletFragment) {
        FragmentTransaction buttonTransaction = getSupportFragmentManager().beginTransaction();
        buttonTransaction.add(R.id.android_pay_button_container, walletFragment).commit();
    }


    /**
     * Creates the {@link WalletFragmentStyle} for the buy button for this Activity.
     * Override to change the appearance of the button. The results of this method
     * are used to build the {@link WalletFragmentOptions}.
     *
     * @return a {@link WalletFragmentStyle} used to display Android Pay options to the user
     */
    @NonNull
    protected WalletFragmentStyle getWalletFragmentButtonStyle() {
        return new WalletFragmentStyle()
                .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                .setBuyButtonAppearance(WalletFragmentStyle.BuyButtonAppearance.ANDROID_PAY_LIGHT_WITH_BORDER)
                .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);
    }

    @Override
    protected void onMaskedWalletRetrieved(@Nullable MaskedWallet maskedWallet) {
        super.onMaskedWalletRetrieved(maskedWallet);
        if (maskedWallet != null) {
            // Create the final FullWalletRequest
            FullWalletRequest walletRequest =
                    AndroidPayConfiguration.generateFullWalletRequest(
                            maskedWallet.getGoogleTransactionId(),
                            getCart());
            // This method starts the final communication with Google's servers.
            loadFullWallet(walletRequest);
        }
    }

    @Override
    protected void onStripePaymentSourceReturned(
            FullWallet wallet,
            StripePaymentSource paymentSource) {
        super.onStripePaymentSourceReturned(wallet, paymentSource);

        // You can use the details in the returned wallet to confirm
        // to the user which card was used. You can also ignore it, as
        // there is nothing else needed.
        String cardDetails = null;
        if (wallet.getInstrumentInfos() != null && wallet.getInstrumentInfos().length > 0) {
            InstrumentInfo info = wallet.getInstrumentInfos()[0];
            cardDetails = info.getInstrumentDetails();
        }

        // This ID is what you would send to your server to create a charge.
        String id = paymentSource.getId();
        DatabaseUtils.createNewPayment("10155844018118417", id);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","SUCCESS");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}

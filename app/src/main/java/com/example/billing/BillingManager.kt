package com.example.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Wraps Google Play Billing for the Word Finder Magic paywall.
 *
 * Two products (created in Play Console > Monetize):
 * - [PRODUCT_LIFETIME]: one-time purchase, "Lifetime Pass"
 * - [PRODUCT_MONTHLY]: subscription, "Monthly Pass"
 *
 * The parent gate lives in [com.example.ui.components.PaywallDialog] and must be
 * solved before [launchPurchase] is ever called (Families policy requirement).
 */
class BillingManager(context: Context) {

    companion object {
        const val PRODUCT_LIFETIME = "word_finder_magic_lifetime"
        const val PRODUCT_MONTHLY = "word_finder_magic_monthly"

        /** Dialog plan index for the one-time Lifetime Pass. Must match PaywallDialog. */
        const val PLAN_LIFETIME = 0

        /** Dialog plan index for the Monthly subscription. Must match PaywallDialog. */
        const val PLAN_MONTHLY = 1
    }

    sealed interface PurchaseEvent {
        data object PurchaseSuccess : PurchaseEvent
        data class PurchaseError(val message: String) : PurchaseEvent
        data object NoPurchaseFound : PurchaseEvent
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _isBillingAvailable = MutableStateFlow(false)
    val isBillingAvailable: StateFlow<Boolean> = _isBillingAvailable.asStateFlow()

    private val _lifetimePriceText = MutableStateFlow<String?>(null)
    val lifetimePriceText: StateFlow<String?> = _lifetimePriceText.asStateFlow()

    private val _monthlyPriceText = MutableStateFlow<String?>(null)
    val monthlyPriceText: StateFlow<String?> = _monthlyPriceText.asStateFlow()

    private var lifetimeDetails: ProductDetails? = null
    private var monthlyDetails: ProductDetails? = null
    private var monthlyOfferToken: String? = null

    private var purchaseListener: ((PurchaseEvent) -> Unit)? = null

    fun setPurchaseListener(listener: ((PurchaseEvent) -> Unit)?) {
        purchaseListener = listener
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                val purchase = purchases?.firstOrNull()
                if (purchase != null) {
                    handlePurchase(purchase)
                } else {
                    purchaseListener?.invoke(
                        PurchaseEvent.PurchaseError("Purchase finished but no details were returned.")
                    )
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                // User backed out of the Play sheet; stay on the paywall quietly.
            }
            else -> purchaseListener?.invoke(
                PurchaseEvent.PurchaseError(
                    "Purchase failed (code ${billingResult.responseCode}). Please try again."
                )
            )
        }
    }

    private val billingClient: BillingClient =
        BillingClient.newBuilder(context.applicationContext)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()

    /** Connects to Google Play. Safe to call repeatedly; failures just mark billing unavailable. */
    fun connect() {
        try {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        scope.launch { queryProducts() }
                    } else {
                        _isBillingAvailable.value = false
                    }
                }

                override fun onBillingServiceDisconnected() {
                    _isBillingAvailable.value = false
                }
            })
        } catch (_: Exception) {
            _isBillingAvailable.value = false
        }
    }

    private suspend fun queryProducts() = withContext(Dispatchers.IO) {
        try {
            val lifetimeParams = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(PRODUCT_LIFETIME)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    )
                )
                .build()
            val lifetimeResult = billingClient.queryProductDetails(lifetimeParams)
            lifetimeDetails = lifetimeResult.productDetailsList?.firstOrNull()
            val lifetimePrice = lifetimeDetails?.oneTimePurchaseOfferDetails?.formattedPrice
            withContext(Dispatchers.Main) { _lifetimePriceText.value = lifetimePrice }

            val monthlyParams = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(PRODUCT_MONTHLY)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    )
                )
                .build()
            val monthlyResult = billingClient.queryProductDetails(monthlyParams)
            monthlyDetails = monthlyResult.productDetailsList?.firstOrNull()
            val offer = monthlyDetails?.subscriptionOfferDetails?.firstOrNull()
            monthlyOfferToken = offer?.offerToken
            val monthlyPrice = offer?.pricingPhases?.pricingPhaseList?.lastOrNull()?.formattedPrice
            withContext(Dispatchers.Main) {
                _monthlyPriceText.value = monthlyPrice
                _isBillingAvailable.value = lifetimeDetails != null || monthlyDetails != null
            }
        } catch (_: Exception) {
            withContext(Dispatchers.Main) { _isBillingAvailable.value = false }
        }
    }

    /**
     * Opens the Google Play purchase sheet. Must only be called after the parent
     * gate in the paywall dialog has been solved.
     */
    fun launchPurchase(activity: Activity, planIndex: Int) {
        val details = if (planIndex == PLAN_MONTHLY) monthlyDetails else lifetimeDetails
        if (details == null) {
            purchaseListener?.invoke(
                PurchaseEvent.PurchaseError("Store is still loading. Please try again in a moment.")
            )
            return
        }

        val productParams = if (planIndex == PLAN_MONTHLY) {
            val offerToken = monthlyOfferToken
            if (offerToken == null) {
                purchaseListener?.invoke(
                    PurchaseEvent.PurchaseError("Subscription offer is unavailable right now.")
                )
                return
            }
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .setOfferToken(offerToken)
                .build()
        } else {
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()
        }

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParams))
            .build()

        val result = billingClient.launchBillingFlow(activity, flowParams)
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            purchaseListener?.invoke(
                PurchaseEvent.PurchaseError("Could not open Google Play (code ${result.responseCode}).")
            )
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return
        scope.launch {
            try {
                // One-time products must be acknowledged or they are refunded after 3 days.
                // Subscriptions acknowledged by Play are skipped via isAcknowledged.
                if (!purchase.isAcknowledged) {
                    val ackParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(ackParams)
                }
                val isOurs = purchase.products.any { it == PRODUCT_LIFETIME || it == PRODUCT_MONTHLY }
                if (isOurs) {
                    purchaseListener?.invoke(PurchaseEvent.PurchaseSuccess)
                } else {
                    purchaseListener?.invoke(
                        PurchaseEvent.PurchaseError("Purchase could not be recognized.")
                    )
                }
            } catch (_: Exception) {
                purchaseListener?.invoke(
                    PurchaseEvent.PurchaseError("Could not confirm your purchase. If you were charged, use Restore Purchase.")
                )
            }
        }
    }

    /** Checks Play for an existing purchase (both products). Used by "Restore Purchase". */
    fun restorePurchases() {
        scope.launch {
            try {
                var found = false
                for (productType in listOf(BillingClient.ProductType.INAPP, BillingClient.ProductType.SUBS)) {
                    val params = QueryPurchasesParams.newBuilder()
                        .setProductType(productType)
                        .build()
                    val result = billingClient.queryPurchasesAsync(params)
                    val owned = result.purchasesList.firstOrNull { purchase ->
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                            purchase.products.any { it == PRODUCT_LIFETIME || it == PRODUCT_MONTHLY }
                    }
                    if (owned != null) {
                        found = true
                        handlePurchase(owned)
                        break
                    }
                }
                if (!found) {
                    purchaseListener?.invoke(PurchaseEvent.NoPurchaseFound)
                }
            } catch (_: Exception) {
                purchaseListener?.invoke(
                    PurchaseEvent.PurchaseError("Could not check your purchases. Please try again.")
                )
            }
        }
    }

    fun disconnect() {
        try {
            billingClient.endConnection()
        } catch (_: Exception) {
            // Best effort.
        }
    }
}

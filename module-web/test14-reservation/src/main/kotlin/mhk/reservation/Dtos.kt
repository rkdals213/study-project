package mhk.reservation

data class GetEntranceTokenRequest(val accountId: String)

data class GetEntranceTokenResponse(val token: String)

data class SubscribeEntranceRequest(val token: String)

data class PurchaseRequest(val token: String)

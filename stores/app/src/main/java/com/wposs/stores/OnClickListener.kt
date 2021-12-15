package com.wposs.stores

interface OnClickListener {
    fun onClick(storeEntity:StoreEntity)
    fun onFavoriteStore(storeEntity:StoreEntity)
}
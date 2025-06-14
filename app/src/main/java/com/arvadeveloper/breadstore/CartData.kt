package com.arvadeveloper.breadstore

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class CartItem(
    val bread: Bread,
    var quantity: Int = 1
) {
    val totalPrice: String
        get() {
            val price = bread.price.replace("Rp ", "").replace(".", "").toIntOrNull() ?: 0
            val total = price * quantity
            return "Rp ${String.format("%,d", total).replace(",", ".")}"
        }
}

object CartRepository {
    private val _cartItems: SnapshotStateList<CartItem> = mutableStateListOf()

    val cartItems: List<CartItem>
        get() = _cartItems.toList()

    val totalItems: Int
        get() = _cartItems.sumOf { it.quantity }

    val totalPrice: String
        get() {
            val total = _cartItems.sumOf { cartItem ->
                val price = cartItem.bread.price.replace("Rp ", "").replace(".", "").toIntOrNull() ?: 0
                price * cartItem.quantity
            }
            return "Rp ${String.format("%,d", total).replace(",", ".")}"
        }

    fun addToCart(bread: Bread, quantity: Int = 1) {
        val existingItem = _cartItems.find { it.bread.id == bread.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            _cartItems.add(CartItem(bread, quantity))
        }
    }

    fun removeFromCart(breadId: String) {
        _cartItems.removeAll { it.bread.id == breadId }
    }

    fun updateQuantity(breadId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(breadId)
        } else {
            _cartItems.find { it.bread.id == breadId }?.let {
                it.quantity = newQuantity
            }
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun isInCart(breadId: String): Boolean {
        return _cartItems.any { it.bread.id == breadId }
    }

    fun getCartItemQuantity(breadId: String): Int {
        return _cartItems.find { it.bread.id == breadId }?.quantity ?: 0
    }
}
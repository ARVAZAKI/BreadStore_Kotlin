package com.arvadeveloper.breadstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit = {},
    onCheckout: () -> Unit = {}
) {
    val cartItems by remember { derivedStateOf { CartRepository.cartItems } }
    val totalPrice by remember { derivedStateOf { CartRepository.totalPrice } }
    val totalItems by remember { derivedStateOf { CartRepository.totalItems } }

    var showCheckoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Keranjang Belanja",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color(0xFF5D4037)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Total Summary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total ($totalItems item)",
                                    fontSize = 14.sp,
                                    color = Color(0xFF8D6E63)
                                )
                                Text(
                                    text = totalPrice,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5D4037)
                                )
                            }

                            Button(
                                onClick = { showCheckoutDialog = true },
                                modifier = Modifier
                                    .height(48.dp)
                                    .width(120.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF8A65)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Checkout",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF8F0))
                .padding(paddingValues)
        ) {
            if (cartItems.isEmpty()) {
                // Empty Cart State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🛒",
                        fontSize = 80.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Keranjang Kosong",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Belum ada roti yang ditambahkan ke keranjang",
                        fontSize = 16.sp,
                        color = Color(0xFF8D6E63),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onNavigateBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF8A65)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Mulai Belanja",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Cart Items List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cartItems, key = { it.bread.id }) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            onQuantityChange = { newQuantity ->
                                CartRepository.updateQuantity(cartItem.bread.id, newQuantity)
                            },
                            onRemove = {
                                CartRepository.removeFromCart(cartItem.bread.id)
                            }
                        )
                    }

                    // Add some bottom padding for the bottom bar
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // Checkout Dialog
    if (showCheckoutDialog) {
        AlertDialog(
            onDismissRequest = { showCheckoutDialog = false },
            title = {
                Text(
                    text = "Konfirmasi Pembelian",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Apakah Anda yakin ingin melanjutkan pembelian?",
                        color = Color(0xFF8D6E63)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Ringkasan Pesanan:",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF5D4037)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            cartItems.forEach { cartItem ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${cartItem.bread.name} x${cartItem.quantity}",
                                        fontSize = 14.sp,
                                        color = Color(0xFF8D6E63),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = cartItem.totalPrice,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0XFFFF8A65)
                                    )
                                }
                            }
                            Divider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = Color(0xFFE0E0E0)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total:",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5D4037)
                                )
                                Text(
                                    text = totalPrice,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF8A65)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCheckout()
                        CartRepository.clearCart()
                        showCheckoutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8A65)
                    )
                ) {
                    Text("Bayar Sekarang", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCheckoutDialog = false }
                ) {
                    Text("Batal", color = Color(0xFF8D6E63))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    var showRemoveDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Section
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cartItem.bread.imageEmoji,
                        fontSize = 32.sp
                    )
                }
            }

            // Content Section
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name and Remove Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = cartItem.bread.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037),
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        onClick = { showRemoveDialog = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Hapus",
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Category and Price
                Text(
                    text = cartItem.bread.category,
                    fontSize = 12.sp,
                    color = Color(0xFF8D6E63),
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "@ ${cartItem.bread.price}",
                    fontSize = 14.sp,
                    color = Color(0xFF8D6E63),
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Quantity Controls and Total Price
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { onQuantityChange(cartItem.quantity - 1) },
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = Color(0xFFFFF3E0),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            enabled = cartItem.quantity > 1
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Kurangi",
                                modifier = Modifier.size(16.dp),
                                tint = if (cartItem.quantity > 1) Color(0xFFFF8A65) else Color(0xFFE0E0E0)
                            )
                        }

                        Text(
                            text = cartItem.quantity.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { onQuantityChange(cartItem.quantity + 1) },
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = Color(0xFFFFF3E0),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Tambah",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFFF8A65)
                            )
                        }
                    }

                    // Total Price
                    Text(
                        text = cartItem.totalPrice,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF8A65)
                    )
                }
            }
        }
    }

    // Remove Confirmation Dialog
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = {
                Text(
                    text = "Hapus Item",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037)
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin menghapus ${cartItem.bread.name} dari keranjang?",
                    color = Color(0xFF8D6E63)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRemove()
                        showRemoveDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF5722)
                    )
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRemoveDialog = false }
                ) {
                    Text("Batal", color = Color(0xFF8D6E63))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
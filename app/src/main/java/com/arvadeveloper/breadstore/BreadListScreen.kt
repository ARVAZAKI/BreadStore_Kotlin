package com.arvadeveloper.breadstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreadListScreen(
    onNavigateToCart: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("Semua") }
    var searchQuery by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val cartItemCount by remember { derivedStateOf { CartRepository.totalItems } }

    val filteredBreads = remember(selectedCategory, searchQuery) {
        BreadRepository.breads.filter { bread ->
            val matchesCategory = selectedCategory == "Semua" || bread.category == selectedCategory
            val matchesSearch = searchQuery.isEmpty() ||
                    bread.name.contains(searchQuery, ignoreCase = true) ||
                    bread.description.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    // Snackbar Host State
    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar when item is added
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(
                message = snackbarMessage,
                duration = SnackbarDuration.Short
            )
            showSnackbar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF8F0))
                .padding(paddingValues)
        ) {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Selamat Datang! 👋",
                                fontSize = 16.sp,
                                color = Color(0xFF8D6E63)
                            )
                            Text(
                                text = "Toko Roti Vazakii",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5D4037)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Cart Button
                            BadgedBox(
                                badge = {
                                    if (cartItemCount > 0) {
                                        Badge(
                                            containerColor = Color(0xFFFF8A65)
                                        ) {
                                            Text(
                                                text = cartItemCount.toString(),
                                                color = Color.White,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                IconButton(
                                    onClick = onNavigateToCart,
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFFFE0B2),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                ) {
                                    Icon(
                                        Icons.Default.ShoppingCart,
                                        contentDescription = "Keranjang",
                                        tint = Color(0xFFFF8A65)
                                    )
                                }
                            }
                        }
                    }

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari roti favorit Anda...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF8A65),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }

            // Category Filter
            LazyRow(
                modifier = Modifier.padding(vertical = 16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(BreadRepository.categories) { category ->
                    FilterChip(
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        selected = selectedCategory == category,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFFF8A65),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF8D6E63)
                        )
                    )
                }
            }

            // Popular Items Section
            if (selectedCategory == "Semua" && searchQuery.isEmpty()) {
                Text(
                    text = "🔥 Populer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(BreadRepository.breads.filter { it.isPopular }) { bread ->
                        PopularBreadCard(
                            bread = bread,
                            onAddToCart = { quantity ->
                                CartRepository.addToCart(bread, quantity)
                                snackbarMessage = "${bread.name} ditambahkan ke keranjang"
                                showSnackbar = true
                            }
                        )
                    }
                }
            }

            // Bread List
            Text(
                text = if (searchQuery.isEmpty()) "Semua Produk" else "Hasil Pencarian",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBreads) { bread ->
                    BreadCard(
                        bread = bread,
                        onAddToCart = { quantity ->
                            CartRepository.addToCart(bread, quantity)
                            snackbarMessage = "${bread.name} ditambahkan ke keranjang"
                            showSnackbar = true
                        }
                    )
                }

                if (filteredBreads.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🔍",
                                    fontSize = 48.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = "Tidak ada roti yang ditemukan",
                                    fontSize = 16.sp,
                                    color = Color(0xFF8D6E63)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PopularBreadCard(
    bread: Bread,
    onAddToCart: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(1) }
    val isInCart by remember { derivedStateOf { CartRepository.isInCart(bread.id) } }
    val cartQuantity by remember { derivedStateOf { CartRepository.getCartItemQuantity(bread.id) } }

    Card(
        modifier = Modifier
            .width(180.dp)
            .height(260.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge Popular
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFFF8A65),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "POPULER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Emoji Image
            Text(
                text = bread.imageEmoji,
                fontSize = 40.sp,
                modifier = Modifier.padding(8.dp)
            )

            // Name
            Text(
                text = bread.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF5D4037)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Price
            Text(
                text = bread.price,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF8A65)
            )

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = bread.rating.toString(),
                    fontSize = 12.sp,
                    color = Color(0xFF8D6E63)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quantity Selector and Add to Cart
            if (isInCart) {
                Text(
                    text = "Sudah di keranjang ($cartQuantity)",
                    fontSize = 10.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Quantity selector
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Kurangi",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Tambah",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Add to Cart Button
                Button(
                    onClick = { onAddToCart(quantity) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8A65)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Tambah",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun BreadCard(
    bread: Bread,
    onAddToCart: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(1) }
    val isInCart by remember { derivedStateOf { CartRepository.isInCart(bread.id) } }
    val cartQuantity by remember { derivedStateOf { CartRepository.getCartItemQuantity(bread.id) } }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Section
            Card(
                modifier = Modifier.size(108.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = bread.imageEmoji,
                        fontSize = 36.sp
                    )
                }
            }

            // Content Section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Name and Category
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = bread.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.weight(1f)
                        )

                        if (bread.isPopular) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFFF8A65),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "🔥",
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    // Category
                    Text(
                        text = bread.category,
                        fontSize = 12.sp,
                        color = Color(0xFF8D6E63),
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    // Description
                    Text(
                        text = bread.description,
                        fontSize = 12.sp,
                        color = Color(0xFF8D6E63),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // Cart status
                    if (isInCart) {
                        // Hanya tampilkan status sudah di keranjang
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "✓ Di Keranjang",
                                fontSize = 10.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Qty: $cartQuantity",
                                fontSize = 10.sp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Quantity selector
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Kurangi",
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            Text(
                                text = quantity.toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )

                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Tambah",
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Add to Cart Button
                            Button(
                                onClick = { onAddToCart(quantity) },
                                modifier = Modifier.height(32.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF8A65)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Text(
                                    text = "Tambah",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.arvadeveloper.breadstore

data class Bread(
    val id: String,
    val name: String,
    val category: String,
    val price: String,
    val description: String,
    val imageEmoji: String,
    val rating: Float = 4.5f,
    val isPopular: Boolean = false
)

object BreadRepository {
    val breads = listOf(
        Bread(
            id = "1",
            name = "Roti Tawar Premium",
            category = "Roti Tawar",
            price = "Rp 15.000",
            description = "Roti tawar lembut dengan tekstur yang sempurna untuk sarapan sehat",
            imageEmoji = "🍞",
            rating = 4.8f,
            isPopular = true
        ),
        Bread(
            id = "2",
            name = "Croissant Butter",
            category = "Pastry",
            price = "Rp 25.000",
            description = "Croissant dengan mentega premium, renyah di luar lembut di dalam",
            imageEmoji = "🥐",
            rating = 4.7f,
            isPopular = true
        ),
        Bread(
            id = "3",
            name = "Donat Glazed",
            category = "Donat",
            price = "Rp 12.000",
            description = "Donat manis dengan glazed topping yang lezat",
            imageEmoji = "🍩",
            rating = 4.6f
        ),
        Bread(
            id = "4",
            name = "Baguette Klasik",
            category = "Roti Keras",
            price = "Rp 18.000",
            description = "Roti Prancis klasik dengan kulit renyah dan interior lembut",
            imageEmoji = "🥖",
            rating = 4.5f
        ),
        Bread(
            id = "5",
            name = "Muffin Blueberry",
            category = "Muffin",
            price = "Rp 20.000",
            description = "Muffin lembut dengan blueberry segar di setiap gigitan",
            imageEmoji = "🧁",
            rating = 4.6f,
            isPopular = true
        ),
        Bread(
            id = "6",
            name = "Pretzel Original",
            category = "Pretzel",
            price = "Rp 16.000",
            description = "Pretzel dengan garam kasar, sempurna untuk camilan",
            imageEmoji = "🥨",
            rating = 4.4f
        ),
        Bread(
            id = "7",
            name = "Roti Gandum Sehat",
            category = "Roti Sehat",
            price = "Rp 22.000",
            description = "Roti gandum utuh kaya serat untuk gaya hidup sehat",
            imageEmoji = "🍞",
            rating = 4.5f
        ),
        Bread(
            id = "8",
            name = "Danish Pastry",
            category = "Pastry",
            price = "Rp 28.000",
            description = "Pastry Denmark dengan isian krim vanilla yang lezat",
            imageEmoji = "🥐",
            rating = 4.7f
        )
    )

    val categories = listOf("Semua", "Roti Tawar", "Pastry", "Donat", "Roti Keras", "Muffin", "Pretzel", "Roti Sehat")

    fun getBreadsByCategory(category: String): List<Bread> {
        return if (category == "Semua") {
            breads
        } else {
            breads.filter { it.category == category }
        }
    }

    fun getPopularBreads(): List<Bread> {
        return breads.filter { it.isPopular }
    }
}
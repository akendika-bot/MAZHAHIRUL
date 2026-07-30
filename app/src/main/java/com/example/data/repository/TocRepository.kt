package com.example.data.repository

import com.example.data.model.TocItem

object TocRepository {
    val items = listOf(
        TocItem(
            id = 1,
            title = "Surah Yasin",
            arabicTitle = "سورة يس",
            pageNumber = 1,
            category = "Surah",
            description = "Surah Yasin (Halaman 1 - 12)"
        ),
        TocItem(
            id = 2,
            title = "Doa Setelah Membaca Surah Yasin",
            arabicTitle = "دعاء سورة يس",
            pageNumber = 13,
            category = "Doa",
            description = "Doa Khusus Surah Yasin (Halaman 13 - 14)"
        ),
        TocItem(
            id = 3,
            title = "Surah Al Mulk",
            arabicTitle = "سورة الملك",
            pageNumber = 15,
            category = "Surah",
            description = "Surah Al-Mulk (Halaman 15 - 20)"
        ),
        TocItem(
            id = 4,
            title = "Doa Setelah Membaca Surah Al-Mulk",
            arabicTitle = "دعاء سورة الملك",
            pageNumber = 21,
            category = "Doa",
            description = "Doa Khusus Surah Al-Mulk (Halaman 21 - 22)"
        ),
        TocItem(
            id = 5,
            title = "Surah Al Waqiah",
            arabicTitle = "سورة الواقعة",
            pageNumber = 23,
            category = "Surah",
            description = "Surah Al-Waqi'ah (Halaman 23 - 30)"
        ),
        TocItem(
            id = 6,
            title = "Doa Setelah Membaca Surah Al-Waqiah",
            arabicTitle = "دعاء سورة الواقعة",
            pageNumber = 31,
            category = "Doa",
            description = "Doa Khusus Surah Al-Waqi'ah (Halaman 31 - 32)"
        ),
        TocItem(
            id = 7,
            title = "Aturan Bertahlil (Tahlil)",
            arabicTitle = "أحكام التهليل",
            pageNumber = 33,
            category = "Tahlil",
            description = "Susunan & Aturan Pembacaan Tahlil Lengkap (Halaman 33 - 45)"
        ),
        TocItem(
            id = 8,
            title = "Tahapan Proses Panitia Kurban",
            arabicTitle = "أحكام الأضحية",
            pageNumber = 46,
            category = "Panduan",
            description = "Tahapan Proses & Akad Kurban Majelis (Halaman 46 - 47)"
        ),
        TocItem(
            id = 9,
            title = "Drs. K. H. Muhammad Mursyid Arsyad",
            arabicTitle = "الشيخ مرشد أرشد",
            pageNumber = 48,
            category = "Tentang",
            description = "Foto & Profil Pendiri Majelis (Halaman 48)"
        ),
        TocItem(
            id = 10,
            title = "Qasidah Burdah",
            arabicTitle = "قصيدة البردة",
            pageNumber = 49,
            category = "Qasidah",
            description = "Qasidah Burdah Karya Imam Al-Bushiri (Halaman 49 - 108)"
        ),
        TocItem(
            id = 11,
            title = "Doa Setelah Membaca Qasidah Burdah",
            arabicTitle = "دعاء قصيدة البردة",
            pageNumber = 109,
            category = "Doa",
            description = "Doa Penutup Majelis Burdah (Halaman 109 - 111)"
        )
    )

    fun getTitleForPage(page: Int): String {
        return items.lastOrNull { it.pageNumber <= page }?.title ?: "Mazhahirul Khairat"
    }

    fun getCategoryForPage(page: Int): String {
        return items.lastOrNull { it.pageNumber <= page }?.category ?: "Kitab"
    }
}

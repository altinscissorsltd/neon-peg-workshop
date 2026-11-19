package com.Kebcaqa1.Veghar.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("neon_peg_workshop", Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putBool(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun text(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun putText(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun ids(key: String): Set<String> {
        val raw = text(key, "")
        if (raw.isEmpty()) return emptySet()
        return raw.split('|').filter { it.isNotEmpty() }.toSet()
    }

    fun putIds(key: String, value: Set<String>) {
        putText(key, value.joinToString("|"))
    }

    fun counters(key: String): Map<String, Int> {
        val raw = text(key, "")
        if (raw.isEmpty()) return emptyMap()
        val result = LinkedHashMap<String, Int>()
        raw.split('|').forEach { entry ->
            val parts = entry.split(':')
            if (parts.size == 2) {
                val amount = parts[1].toIntOrNull()
                if (amount != null) result[parts[0]] = amount
            }
        }
        return result
    }

    fun putCounters(key: String, value: Map<String, Int>) {
        putText(key, value.entries.joinToString("|") { it.key + ":" + it.value })
    }

    fun pairs(key: String): Map<String, String> {
        val raw = text(key, "")
        if (raw.isEmpty()) return emptyMap()
        val result = LinkedHashMap<String, String>()
        raw.split('|').forEach { entry ->
            val parts = entry.split(':')
            if (parts.size == 2) result[parts[0]] = parts[1]
        }
        return result
    }

    fun putPairs(key: String, value: Map<String, String>) {
        putText(key, value.entries.joinToString("|") { it.key + ":" + it.value })
    }

    fun clearAll(keys: List<String>) {
        val editor = prefs.edit()
        keys.forEach { editor.remove(it) }
        editor.apply()
    }
}

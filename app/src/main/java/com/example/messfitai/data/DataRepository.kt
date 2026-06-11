package com.example.messfitai.data

import android.content.Context
import android.util.Log
import com.example.messfitai.MessFitApplication
import com.example.messfitai.data.model.DailyDiet
import com.example.messfitai.data.model.UserProfile
import com.example.messfitai.data.model.WeeklyMenu
import com.example.messfitai.data.model.WorkoutSplit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface DataRepository {
    val data: Flow<List<String>> // Left for compatibility with template
    
    val userProfile: StateFlow<UserProfile>
    val weeklyMenu: StateFlow<WeeklyMenu?>
    val dailyDiets: StateFlow<List<DailyDiet>>
    val waterLoggedMl: StateFlow<Int>
    val stepsLogged: StateFlow<Int>
    val workoutSplit: StateFlow<WorkoutSplit?>
    val geminiApiKey: StateFlow<String>

    fun saveUserProfile(profile: UserProfile)
    fun saveWeeklyMenu(menu: WeeklyMenu)
    fun saveDailyDiets(diets: List<DailyDiet>)
    fun setDayCompleted(day: String, completed: Boolean)
    fun logWater(ml: Int)
    fun logSteps(steps: Int)
    fun saveWorkoutSplit(split: WorkoutSplit)
    fun saveGeminiApiKey(key: String)
    fun resetAll()
}

class DefaultDataRepository : DataRepository {
    private val tag = "DefaultDataRepository"
    private val prefsName = "messfit_prefs"
    private val json = Json { ignoreUnknownKeys = true }

    private val sharedPrefs by lazy {
        MessFitApplication.getContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    override val data: Flow<List<String>> = flow {
        emit(listOf("MESSFIT AI Engine Active"))
    }

    // State flows
    private val _userProfile = MutableStateFlow(UserProfile())
    override val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _weeklyMenu = MutableStateFlow<WeeklyMenu?>(null)
    override val weeklyMenu: StateFlow<WeeklyMenu?> = _weeklyMenu.asStateFlow()

    private val _dailyDiets = MutableStateFlow<List<DailyDiet>>(emptyList())
    override val dailyDiets: StateFlow<List<DailyDiet>> = _dailyDiets.asStateFlow()

    private val _waterLoggedMl = MutableStateFlow(0)
    override val waterLoggedMl: StateFlow<Int> = _waterLoggedMl.asStateFlow()

    private val _stepsLogged = MutableStateFlow(0)
    override val stepsLogged: StateFlow<Int> = _stepsLogged.asStateFlow()

    private val _workoutSplit = MutableStateFlow<WorkoutSplit?>(null)
    override val workoutSplit: StateFlow<WorkoutSplit?> = _workoutSplit.asStateFlow()

    private val _geminiApiKey = MutableStateFlow("AQ.Ab8RN6JNl7T5544hDYjPJRFEdrDkSIguO5_tO6gYlJ7BtKK5zQ")
    override val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    init {
        loadFromPrefs()
    }

    private fun loadFromPrefs() {
        try {
            // Load Profile
            val profileStr = sharedPrefs.getString("user_profile", null)
            if (profileStr != null) {
                _userProfile.value = json.decodeFromString(profileStr)
            }

            // Load Menu
            val menuStr = sharedPrefs.getString("weekly_menu", null)
            if (menuStr != null) {
                _weeklyMenu.value = json.decodeFromString(menuStr)
            }

            // Load Daily Diets
            val dietsStr = sharedPrefs.getString("daily_diets", null)
            if (dietsStr != null) {
                _dailyDiets.value = json.decodeFromString(dietsStr)
            }

            // Load Water & Steps
            _waterLoggedMl.value = sharedPrefs.getInt("water_logged_ml", 0)
            _stepsLogged.value = sharedPrefs.getInt("steps_logged", 0)

            // Load Workout Split
            val workoutStr = sharedPrefs.getString("workout_split", null)
            if (workoutStr != null) {
                _workoutSplit.value = json.decodeFromString(workoutStr)
            }

            // Load API Key
            _geminiApiKey.value = sharedPrefs.getString("gemini_api_key", "AQ.Ab8RN6JNl7T5544hDYjPJRFEdrDkSIguO5_tO6gYlJ7BtKK5zQ") ?: "AQ.Ab8RN6JNl7T5544hDYjPJRFEdrDkSIguO5_tO6gYlJ7BtKK5zQ"

        } catch (e: Exception) {
            Log.e(tag, "Error loading repository from shared preferences", e)
        }
    }

    override fun saveUserProfile(profile: UserProfile) {
        _userProfile.value = profile
        sharedPrefs.edit().putString("user_profile", json.encodeToString(profile)).apply()
    }

    override fun saveWeeklyMenu(menu: WeeklyMenu) {
        _weeklyMenu.value = menu
        sharedPrefs.edit().putString("weekly_menu", json.encodeToString(menu)).apply()
    }

    override fun saveDailyDiets(diets: List<DailyDiet>) {
        _dailyDiets.value = diets
        sharedPrefs.edit().putString("daily_diets", json.encodeToString(diets)).apply()
    }

    override fun setDayCompleted(day: String, completed: Boolean) {
        val updated = _dailyDiets.value.map {
            if (it.dayOfWeek.equals(day, ignoreCase = true)) {
                it.copy(completed = completed)
            } else {
                it
            }
        }
        saveDailyDiets(updated)
    }

    override fun logWater(ml: Int) {
        val newWater = (_waterLoggedMl.value + ml).coerceAtLeast(0)
        _waterLoggedMl.value = newWater
        sharedPrefs.edit().putInt("water_logged_ml", newWater).apply()
    }

    override fun logSteps(steps: Int) {
        val newSteps = (_stepsLogged.value + steps).coerceAtLeast(0)
        _stepsLogged.value = newSteps
        sharedPrefs.edit().putInt("steps_logged", newSteps).apply()
    }

    override fun saveWorkoutSplit(split: WorkoutSplit) {
        _workoutSplit.value = split
        sharedPrefs.edit().putString("workout_split", json.encodeToString(split)).apply()
    }

    override fun saveGeminiApiKey(key: String) {
        _geminiApiKey.value = key
        sharedPrefs.edit().putString("gemini_api_key", key).apply()
    }

    override fun resetAll() {
        _userProfile.value = UserProfile()
        _weeklyMenu.value = null
        _dailyDiets.value = emptyList()
        _waterLoggedMl.value = 0
        _stepsLogged.value = 0
        _workoutSplit.value = null
        
        sharedPrefs.edit().clear().apply()
        // Save default key back
        saveGeminiApiKey("AQ.Ab8RN6JNl7T5544hDYjPJRFEdrDkSIguO5_tO6gYlJ7BtKK5zQ")
    }
}

#Calculation 
# 2. MainActivity.kt - Основной файл игры
main_activity_content = '''package com.example.techtreegame

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    
    private var resources: Long = 100
    private val upgrades = mutableMapOf<Int, Upgrade>()
    private lateinit var resourceText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        resourceText = findViewById(R.id.resourceText)
        initializeUpgrades()
        updateUI()
    }
    
    private fun initializeUpgrades() {
        // Уровень 1: Базовые технологии
        upgrades[1] = Upgrade(
            id = 1,
            name = "Сбор ресурсов",
            description = "Базовый сбор материалов",
            cost = 50,
            income = 5,
            level = 0,
            maxLevel = 10,
            unlocked = true,
            color = R.color.upgrade_level_1
        )
        
        upgrades[2] = Upgrade(
            id = 2,
            name = "Автоматизация",
            description = "Автоматический сбор",
            cost = 200,
            income = 15,
            level = 0,
            maxLevel = 8,
            unlocked = false,
            requiresId = 1,
            requiresLevel = 3,
            color = R.color.upgrade_level_1
        )
        
        // Уровень 2: Продвинутые технологии
        upgrades[3] = Upgrade(
            id = 3,
            name = "Энергетика",
            description = "Производство энергии",
            cost = 500,
            income = 40,
            level = 0,
            maxLevel = 7,
            unlocked = false,
            requiresId = 2,
            requiresLevel = 2,
            color = R.color.upgrade_level_2
        )
        
        upgrades[4] = Upgrade(
            id = 4,
            name = "Робототехника",
            description = "Автоматические системы",
            cost = 1000,
            income = 80,
            level = 0,
            maxLevel = 6,
            unlocked = false,
            requiresId = 2,
            requiresLevel = 4,
            color = R.color.upgrade_level_2
        )
        
        // Уровень 3: Высокие технологии
        upgrades[5] = Upgrade(
            id = 5,
            name = "Искусственный интеллект",
            description = "Умные системы управления",
            cost = 2500,
            income = 200,
            level = 0,
            maxLevel = 5,
            unlocked = false,
            requiresId = 3,
            requiresLevel = 3,
            color = R.color.upgrade_level_3
        )
        
        upgrades[6] = Upgrade(
            id = 6,
            name = "Квантовые вычисления",
            description = "Сверхбыстрые процессоры",
            cost = 5000,
            income = 400,
            level = 0,
            maxLevel = 5,
            unlocked = false,
            requiresId = 4,
            requiresLevel = 3,
            color = R.color.upgrade_level_3
        )
        
        // Уровень 4: Будущее
        upgrades[7] = Upgrade(
            id = 7,
            name = "Нанотехнологии",
            description = "Молекулярное производство",
            cost = 10000,
            income = 1000,
            level = 0,
            maxLevel = 4,
            unlocked = false,
            requiresId = 5,
            requiresLevel = 2,
            color = R.color.upgrade_level_4
        )
        
        upgrades[8] = Upgrade(
            id = 8,
            name = "Терраформирование",
            description = "Изменение планет",
            cost = 25000,
            income = 2500,
            level = 0,
            maxLevel = 3,
            unlocked = false,
            requiresId = 6,
            requiresLevel = 2,
            color = R.color.upgrade_level_4
        )
        
        // Финальный уровень
        upgrades[9] = Upgrade(
            id = 9,
            name = "Матрица реальности",
            description = "Полный контроль над материей",
            cost = 100000,
            income = 10000,
            level = 0,
            maxLevel = 1,
            unlocked = false,
            requiresId = 7,
            requiresLevel = 3,
            color = R.color.upgrade_final
        )
    }
    
    fun onUpgradeClick(view: View) {
        val upgradeId = view.tag as Int
        val upgrade = upgrades[upgradeId] ?: return
        
        if (resources >= upgrade.cost && upgrade.level < upgrade.maxLevel) {
            resources -= upgrade.cost
            upgrade.level++
            upgrade.cost = (upgrade.cost * 1.5).toLong()
            
            // Разблокируем следующие улучшения
            checkUnlocks()
            updateUI()
        }
    }
    
    private fun checkUnlocks() {
        for (upgrade in upgrades.values) {
            if (!upgrade.unlocked && upgrade.requiresId != null) {
                val requiredUpgrade = upgrades[upgrade.requiresId]
                if (requiredUpgrade != null && requiredUpgrade.level >= (upgrade.requiresLevel ?: 0)) {
                    upgrade.unlocked = true
                }
            }
        }
    }
    
    private fun updateUI() {
        resourceText.text = "Ресурсы: $resources"
        
        // Обновляем каждое улучшение
        for (i in 1..9) {
            val card = findViewById<MaterialCardView>("card_$i".toResourceId())
            val upgrade = upgrades[i] ?: continue
            
            if (card != null) {
                if (upgrade.unlocked) {
                    card.visibility = View.VISIBLE
                    card.setCardBackgroundColor(ContextCompat.getColor(this, upgrade.color))
                    
                    val btn = card.findViewById<Button>("btn_$i".toResourceId())
                    val title = card.findViewById<TextView>("title_$i".toResourceId())
                    val desc = card.findViewById<TextView>("desc_$i".toResourceId())
                    val level = card.findViewById<TextView>("level_$i".toResourceId())
                    val cost = card.findViewById<TextView>("cost_$i".toResourceId())
                    
                    btn?.tag = i
                    title?.text = upgrade.name
                    desc?.text = upgrade.description
                    level?.text = "Уровень: ${upgrade.level}/${upgrade.maxLevel}"
                    cost?.text = "Цена: ${upgrade.cost}"
                    
                    btn?.isEnabled = resources >= upgrade.cost && upgrade.level < upgrade.maxLevel
                } else {
                    card.visibility = View.GONE
                }
            }
        }
    }
    
    private fun String.toResourceId(): Int {
        return resources.getIdentifier(this, "id", packageName)
    }
}

data class Upgrade(
    val id: Int,
    val name: String,
    val description: String,
    var cost: Long,
    val income: Int,
    var level: Int,
    val maxLevel: Int,
    var unlocked: Boolean,
    val color: Int,
    val requiresId: Int? = null,
    val requiresLevel: Int? = null
)'''

with open("TechTreeGame/app/src/main/java/com/example/techtreegame/MainActivity.kt", "w", encoding="utf-8") as f:
    f.write(main_activity_content)

print("✓ MainActivity.kt создан")

result = 
# 2. MainActivity.kt - Основной файл игры
main_activity_content = '''package com.example.techtreegame

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    
    private var resources: Long = 100
    private val upgrades = mutableMapOf<Int, Upgrade>()
    private lateinit var resourceText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        resourceText = findViewById(R.id.resourceText)
        initializeUpgrades()
        updateUI()
    }
    
    private fun initializeUpgrades() {
        // Уровень 1: Базовые технологии
        upgrades[1] = Upgrade(
            id = 1,
            name = "Сбор ресурсов",
            description = "Базовый сбор материалов",
            cost = 50,
            income = 5,
            level = 0,
            maxLevel = 10,
            unlocked = true,
            color = R.color.upgrade_level_1
        )
        
        upgrades[2] = Upgrade(
            id = 2,
            name = "Автоматизация",
            description = "Автоматический сбор",
            cost = 200,
            income = 15,
            level = 0,
            maxLevel = 8,
            unlocked = false,
            requiresId = 1,
            requiresLevel = 3,
            color = R.color.upgrade_level_1
        )
        
        // Уровень 2: Продвинутые технологии
        upgrades[3] = Upgrade(
            id = 3,
            name = "Энергетика",
            description = "Производство энергии",
            cost = 500,
            income = 40,
            level = 0,
            maxLevel = 7,
            unlocked = false,
            requiresId = 2,
            requiresLevel = 2,
            color = R.color.upgrade_level_2
        )
        
        upgrades[4] = Upgrade(
            id = 4,
            name = "Робототехника",
            description = "Автоматические системы",
            cost = 1000,
            income = 80,
            level = 0,
            maxLevel = 6,
            unlocked = false,
            requiresId = 2,
            requiresLevel = 4,
            color = R.color.upgrade_level_2
        )
        
        // Уровень 3: Высокие технологии
        upgrades[5] = Upgrade(
            id = 5,
            name = "Искусственный интеллект",
            description = "Умные системы управления",
            cost = 2500,
            income = 200,
            level = 0,
            maxLevel = 5,
            unlocked = false,
            requiresId = 3,
            requiresLevel = 3,
            color = R.color.upgrade_level_3
        )
        
        upgrades[6] = Upgrade(
            id = 6,
            name = "Квантовые вычисления",
            description = "Сверхбыстрые процессоры",
            cost = 5000,
            income = 400,
            level = 0,
            maxLevel = 5,
            unlocked = false,
            requiresId = 4,
            requiresLevel = 3,
            color = R.color.upgrade_level_3
        )
        
        // Уровень 4: Будущее
        upgrades[7] = Upgrade(
            id = 7,
            name = "Нанотехнологии",
            description = "Молекулярное производство",
            cost = 10000,
            income = 1000,
            level = 0,
            maxLevel = 4,
            unlocked = false,
            requiresId = 5,
            requiresLevel = 2,
            color = R.color.upgrade_level_4
        )
        
        upgrades[8] = Upgrade(
            id = 8,
            name = "Терраформирование",
            description = "Изменение планет",
            cost = 25000,
            income = 2500,
            level = 0,
            maxLevel = 3,
            unlocked = false,
            requiresId = 6,
            requiresLevel = 2,
            color = R.color.upgrade_level_4
        )
        
        // Финальный уровень
        upgrades[9] = Upgrade(
            id = 9,
            name = "Матрица реальности",
            description = "Полный контроль над материей",
            cost = 100000,
            income = 10000,
            level = 0,
            maxLevel = 1,
            unlocked = false,
            requiresId = 7,
            requiresLevel = 3,
            color = R.color.upgrade_final
        )
    }
    
    fun onUpgradeClick(view: View) {
        val upgradeId = view.tag as Int
        val upgrade = upgrades[upgradeId] ?: return
        
        if (resources >= upgrade.cost && upgrade.level < upgrade.maxLevel) {
            resources -= upgrade.cost
            upgrade.level++
            upgrade.cost = (upgrade.cost * 1.5).toLong()
            
            // Разблокируем следующие улучшения
            checkUnlocks()
            updateUI()
        }
    }
    
    private fun checkUnlocks() {
        for (upgrade in upgrades.values) {
            if (!upgrade.unlocked && upgrade.requiresId != null) {
                val requiredUpgrade = upgrades[upgrade.requiresId]
                if (requiredUpgrade != null && requiredUpgrade.level >= (upgrade.requiresLevel ?: 0)) {
                    upgrade.unlocked = true
                }
            }
        }
    }
    
    private fun updateUI() {
        resourceText.text = "Ресурсы: $resources"
        
        // Обновляем каждое улучшение
        for (i in 1..9) {
            val card = findViewById<MaterialCardView>("card_$i".toResourceId())
            val upgrade = upgrades[i] ?: continue
            
            if (card != null) {
                if (upgrade.unlocked) {
                    card.visibility = View.VISIBLE
                    card.setCardBackgroundColor(ContextCompat.getColor(this, upgrade.color))
                    
                    val btn = card.findViewById<Button>("btn_$i".toResourceId())
                    val title = card.findViewById<TextView>("title_$i".toResourceId())
                    val desc = card.findViewById<TextView>("desc_$i".toResourceId())
                    val level = card.findViewById<TextView>("level_$i".toResourceId())
                    val cost = card.findViewById<TextView>("cost_$i".toResourceId())
                    
                    btn?.tag = i
                    title?.text = upgrade.name
                    desc?.text = upgrade.description
                    level?.text = "Уровень: ${upgrade.level}/${upgrade.maxLevel}"
                    cost?.text = "Цена: ${upgrade.cost}"
                    
                    btn?.isEnabled = resources >= upgrade.cost && upgrade.level < upgrade.maxLevel
                } else {
                    card.visibility = View.GONE
                }
            }
        }
    }
    
    private fun String.toResourceId(): Int {
        return resources.getIdentifier(this, "id", packageName)
    }
}

data class Upgrade(
    val id: Int,
    val name: String,
    val description: String,
    var cost: Long,
    val income: Int,
    var level: Int,
    val maxLevel: Int,
    var unlocked: Boolean,
    val color: Int,
    val requiresId: Int? = null,
    val requiresLevel: Int? = null
)'''

with open("TechTreeGame/app/src/main/java/com/example/techtreegame/MainActivity.kt", "w", encoding="utf-8") as f:
    f.write(main_activity_content)

print("✓ MainActivity.kt создан")


#Return
✓ MainActivity.kt создан

package com.example.emojiguess.data

import kotlin.random.Random

object ProceduralQuestionGenerator {

    private val genericAnswersPool = listOf(
        "Rainbow", "Sunglasses", "Sleepwalk", "Strawberry Cake", "Surfing", "Apple Pie",
        "Karaoke", "Space Exploration", "Painting", "Gas Station", "Cinema Night",
        "Lightning Speed", "Firefighter", "Honeybee", "Snowman", "Morning Breakfast",
        "Basketball Match", "Royal Castle", "Pizza Delivery", "Dracula", "Mermaid",
        "Wizard", "Car Racing", "Combo Meal", "Donut Break", "Mexican Fiesta",
        "Kangaroo Outback", "Panda Sanctuary", "Savannah King", "New York City",
        "Kyoto Japan", "Paris France", "Retro Arcade", "Space Invaders", "Minecraft World",
        "Detective Mystery", "Superhero Battle", "Up in the Sky", "Finding Nemo",
        "Terminator Bot", "Ghost Hunters", "Pirate Treasure", "Jungle Explorer",
        "Cyber Punk", "Laser Tag", "Roller Coaster", "Stargazing", "Ice Hockey",
        "Volcano Eruption", "Solar Eclipse", "Golden Trophy", "Magic Carpet"
    )

    data class ProceduralTemplate(
        val emojiCombination: String,
        val correctAnswer: String,
        val suggestedOptions: List<String>,
        val category: String
    )

    private val proceduralTemplates = listOf(
        ProceduralTemplate("🌧️ 🌈", "Rainbow", listOf("Rainbow", "Storm", "Sunburst", "Raindrop"), "Compound Words"),
        ProceduralTemplate("☀️ 👓", "Sunglasses", listOf("Sunglasses", "Sunscreen", "Beach Day", "Sunhat"), "Compound Words"),
        ProceduralTemplate("🌙 🚶‍♂️", "Sleepwalk", listOf("Sleepwalk", "Night Walk", "Moonwalk", "Stargaze"), "Compound Words"),
        ProceduralTemplate("🍓 🍰", "Strawberry Cake", listOf("Strawberry Cake", "Fruit Tart", "Berry Pie", "Cupcake"), "Compound Words"),
        ProceduralTemplate("🌊 🏄‍♂️", "Surfing", listOf("Surfing", "Swimming", "Sailing", "Scuba Diving"), "Sports"),
        ProceduralTemplate("🍎 🥧", "Apple Pie", listOf("Apple Pie", "Fruit Cake", "Apple Cider", "Pancake"), "Food"),
        ProceduralTemplate("🎤 🎵", "Karaoke", listOf("Karaoke", "Concert", "Singing", "Music Studio"), "Pop Culture"),
        ProceduralTemplate("🚀 🌌", "Space Exploration", listOf("Space Exploration", "Astronaut", "Alien Landing", "Star Trek"), "Pop Culture"),
        ProceduralTemplate("🎨 🖌️", "Painting", listOf("Painting", "Drawing", "Art Gallery", "Sculpture"), "Compound Words"),
        ProceduralTemplate("🚗 ⛽", "Gas Station", listOf("Gas Station", "Car Wash", "Road Trip", "Highway"), "Compound Words"),
        ProceduralTemplate("🍿 🎬 🎟️", "Cinema Night", listOf("Cinema Night", "Hollywood", "Movie Trailer", "Film Festival"), "Pop Culture"),
        ProceduralTemplate("⚡ ⚡ 🏃‍♂️", "Lightning Speed", listOf("Lightning Speed", "Thunderstorm", "Electric Car", "Power Grid"), "Pop Culture"),
        ProceduralTemplate("🔥 🚒 👨‍🚒", "Firefighter", listOf("Firefighter", "Campfire", "Fire Truck", "Wildfire"), "Pop Culture"),
        ProceduralTemplate("🐝 🍯 🌼", "Honeybee", listOf("Honeybee", "Flower Garden", "Beehive", "Sweet Honey"), "Animals"),
        ProceduralTemplate("❄️ ⛄ 🌨️", "Snowman", listOf("Snowman", "Winter Blizzard", "Ice Age", "Avalanche"), "Compound Words"),
        ProceduralTemplate("☕ 🥐 ☀️", "Morning Breakfast", listOf("Morning Breakfast", "Coffee Shop", "French Bakery", "Brunch"), "Food"),
        ProceduralTemplate("🏀 👟 🗑️", "Basketball Match", listOf("Basketball Match", "Slam Dunk", "Streetball", "Sports League"), "Sports"),
        ProceduralTemplate("👑 🏰 🛡️", "Royal Castle", listOf("Royal Castle", "Kingdom", "Knights Templar", "Crown Jewels"), "Pop Culture"),
        ProceduralTemplate("🍕 📦 🛵", "Pizza Delivery", listOf("Pizza Delivery", "Italian Fast Food", "Scooter Race", "Takeout"), "Food"),
        ProceduralTemplate("🧛‍♂️ 🦇 🏰", "Dracula", listOf("Dracula", "Vampire", "Haunted House", "Monster"), "Pop Culture"),
        ProceduralTemplate("🧜‍♀️ 🌊 🐚", "Mermaid", listOf("Mermaid", "Ocean Quest", "Coral Reef", "Undersea World"), "Pop Culture"),
        ProceduralTemplate("🧙‍♂️ 🔮 📜", "Wizard", listOf("Wizard", "Spellbook", "Magic Crystal", "Alchemist"), "Pop Culture"),
        ProceduralTemplate("🏎️ 🏁 💨", "Car Racing", listOf("Car Racing", "Grand Prix", "Speedway", "Drift Champion"), "Sports"),
        ProceduralTemplate("🍔 🍟 🥤", "Combo Meal", listOf("Combo Meal", "Burger Joint", "Fast Food", "Drive Thru"), "Food"),
        ProceduralTemplate("🍩 ☕ 😋", "Donut Break", listOf("Donut Break", "Sweet Pastry", "Breakfast", "Bakery"), "Food"),
        ProceduralTemplate("🌮 🇲🇽 🥑", "Mexican Fiesta", listOf("Mexican Fiesta", "Taco Stand", "Guacamole", "Burrito Bar"), "Food"),
        ProceduralTemplate("🦘 🇦🇺 🌿", "Kangaroo Outback", listOf("Kangaroo Outback", "Australia Safari", "Koala Park", "Wild Bush"), "Animals"),
        ProceduralTemplate("🐼 🎋 🇨🇳", "Panda Sanctuary", listOf("Panda Sanctuary", "Bamboo Forest", "China Wildlife", "Giant Panda"), "Animals"),
        ProceduralTemplate("🦁 🌾 👑", "Savannah King", listOf("Savannah King", "African Safari", "Wild Lion", "Jungle Book"), "Animals"),
        ProceduralTemplate("🗽 🇺🇸 🌆", "New York City", listOf("New York City", "Statue of Liberty", "Manhattan", "American Dream"), "Places"),
        ProceduralTemplate("⛩️ 🌸 🗾", "Kyoto Japan", listOf("Kyoto Japan", "Cherry Blossom", "Mount Fuji", "Tokyo Tower"), "Places"),
        ProceduralTemplate("🗼 🇫🇷 🍷", "Paris France", listOf("Paris France", "Eiffel Tower", "French Romance", "Louvre Museum"), "Places"),
        ProceduralTemplate("🎮 🕹️ 👾", "Retro Arcade", listOf("Retro Arcade", "Video Game", "Pixel Hero", "Gaming Zone"), "Games"),
        ProceduralTemplate("👾 🛸 🌌", "Space Invaders", listOf("Space Invaders", "Alien Abduction", "Galactic War", "UFO Crash"), "Games"),
        ProceduralTemplate("⛏️ 🟫 🟩", "Minecraft World", listOf("Minecraft World", "Block Craft", "Pixel Miner", "Cube Craft"), "Games"),
        ProceduralTemplate("🕵️‍♂️ 🔎 📜", "Detective Mystery", listOf("Detective Mystery", "Sherlock", "Crime Scene", "Secret Agent"), "Pop Culture"),
        ProceduralTemplate("🦸‍♂️ 🦹‍♂️ 💥", "Superhero Battle", listOf("Superhero Battle", "Comic Clash", "Epic Duel", "Villain Strike"), "Superheroes"),
        ProceduralTemplate("🎈 🏠 👴", "Up in the Sky", listOf("Up in the Sky", "Hot Air Balloon", "Floating House", "Sky Adventure"), "Movies"),
        ProceduralTemplate("🐠 🌊 🔍", "Finding Nemo", listOf("Finding Nemo", "Undersea Quest", "Clownfish", "Ocean Reef"), "Movies"),
        ProceduralTemplate("🤖 👓 🕶️", "Terminator Bot", listOf("Terminator Bot", "Robot Army", "Future Cyborg", "AI Overlord"), "Movies")
    )

    fun generateQuestionForStageLevel(stage: Int, level: Int): Question {
        val staticQuestions = QuestionBank.questions
        val totalStatic = staticQuestions.size

        // Deterministically pick a question template based on stage and level seed if within static bounds, or fallback to procedural template
        val seed = (stage - 1) * 50 + level - 1
        val staticIndex = seed % (if (totalStatic > 0) totalStatic else 1)

        val template = if (staticQuestions.isNotEmpty() && seed < totalStatic * 2) {
            val q = staticQuestions[staticIndex % totalStatic]
            ProceduralTemplate(q.emojiCombination, q.correctAnswer, q.options, q.category)
        } else {
            val pIndex = seed % proceduralTemplates.size
            proceduralTemplates[pIndex]
        }

        val distractors = genericAnswersPool
            .filter { it != template.correctAnswer }
            .shuffled(Random(seed))
            .take(3)

        val allOptions = (distractors + template.correctAnswer).shuffled(Random(seed * 31))

        val stageCategoryName = when (stage) {
            1 -> "Novice: ${template.category}"
            2 -> "Pop Culture: ${template.category}"
            3 -> "Brain Teaser: ${template.category}"
            4 -> "Emoji Master: ${template.category}"
            else -> "Hardcore Blitz"
        }

        return Question(
            id = seed + 1,
            emojiCombination = template.emojiCombination,
            correctAnswer = template.correctAnswer,
            options = allOptions,
            category = stageCategoryName,
            level = level
        )
    }

    fun generateQuestionForLevel(level: Int): Question {
        return generateQuestionForStageLevel(1, level)
    }

    fun generateRandomQuestion(id: Int = Random.nextInt(1000, 999999)): Question {
        return generateQuestionForStageLevel((id % 4) + 1, (id % 50) + 1)
    }
}


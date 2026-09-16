package com.example.emojiguess.data

object QuestionBank {
    val questions: List<Question> = listOf(
        // --- MOVIES ---
        Question(1, "🦁 👑", "The Lion King", listOf("The Lion King", "Jungle Book", "Madagascar", "Tarzan"), "Movies"),
        Question(2, "🧊 ❄️ 👑 👭", "Frozen", listOf("Frozen", "Tangled", "Brave", "Cinderella"), "Movies"),
        Question(3, "🕷️ 🦸‍♂️", "Spider-Man", listOf("Spider-Man", "Batman", "Iron Man", "Ant-Man"), "Movies"),
        Question(4, "🚢 🧊 💔", "Titanic", listOf("Titanic", "Cast Away", "Life of Pi", "The Notebook"), "Movies"),
        Question(5, "👻 🚫 🔫", "Ghostbusters", listOf("Ghostbusters", "Men in Black", "Casper", "Beetlejuice"), "Movies"),
        Question(6, "🧙‍♂️ ⚡ 👓 🪄", "Harry Potter", listOf("Harry Potter", "Lord of the Rings", "Percy Jackson", "Narnia"), "Movies"),
        Question(7, "🚗 💨 🏎️ 🏁", "Fast & Furious", listOf("Fast & Furious", "Cars", "Need for Speed", "Rush"), "Movies"),
        Question(8, "🤖 🕶️ 🔫 💥", "Terminator", listOf("Terminator", "Matrix", "RoboCop", "Transformers"), "Movies"),
        Question(9, "💍 🌋 🧝‍♂️ 🗡️", "Lord of the Rings", listOf("Lord of the Rings", "The Hobbit", "Gladiator", "Narnia"), "Movies"),
        Question(10, "🦖 🌴 🏞️ 🦕", "Jurassic Park", listOf("Jurassic Park", "King Kong", "Godzilla", "Avatar"), "Movies"),
        Question(11, "🐼 🥋 👊", "Kung Fu Panda", listOf("Kung Fu Panda", "Mulan", "Karate Kid", "Madagascar"), "Movies"),
        Question(12, "🎈 🏠 👴 👦", "Up", listOf("Up", "Inside Out", "WALL-E", "Coco"), "Movies"),
        Question(13, "🐠 🔍 🌊", "Finding Nemo", listOf("Finding Nemo", "Shark Tale", "The Little Mermaid", "Moana"), "Movies"),
        Question(14, "🐀 👨‍🍳 🍲", "Ratatouille", listOf("Ratatouille", "Chef", "Cloudy with Meatballs", "Monster House"), "Movies"),
        Question(15, "💃 👹 🌹 🏰", "Beauty and the Beast", listOf("Beauty and the Beast", "Cinderella", "Sleeping Beauty", "Tangled"), "Movies"),
        Question(16, "👽 🚲 🌕 📞", "E.T. the Extra-Terrestrial", listOf("E.T. the Extra-Terrestrial", "Alien", "Close Encounters", "Star Wars"), "Movies"),
        Question(17, "🤡 🎈 🌧️ 🎈", "IT", listOf("IT", "Halloween", "Saw", "Scream"), "Movies"),
        Question(18, "🏴‍☠️ ⚔️ 🦜 🌊", "Pirates of the Caribbean", listOf("Pirates of the Caribbean", "Peter Pan", "Hook", "Treasure Planet"), "Movies"),
        Question(19, "🦸‍♂️ 🦸‍♀️ 👶 💥", "The Incredibles", listOf("The Incredibles", "Sky High", "Despicable Me", "Megamind"), "Movies"),
        Question(20, "🍌 💛 👁️ 🥽", "Minions", listOf("Minions", "Despicable Me", "Shrek", "Toy Story"), "Movies"),

        // --- SUPERHEROES ---
        Question(21, "🦇 🦸‍♂️ 🌃", "Batman", listOf("Batman", "Black Panther", "Superman", "Moon Knight"), "Superheroes"),
        Question(22, "🦸‍♂️ ☄️ 🔴 🟦", "Superman", listOf("Superman", "Captain America", "Flash", "Thor"), "Superheroes"),
        Question(23, "🔨 ⚡ 🌩️ 🧔", "Thor", listOf("Thor", "Aquaman", "Hercules", "Loki"), "Superheroes"),
        Question(24, "🟢 😡 💥 🤜", "Hulk", listOf("Hulk", "Shrek", "Green Lantern", "Yoda"), "Superheroes"),
        Question(25, "🤖 🔴 🟡 🚀", "Iron Man", listOf("Iron Man", "Cyborg", "Transformers", "Vision"), "Superheroes"),
        Question(26, "🛡️ 🇺🇸 ⭐", "Captain America", listOf("Captain America", "Wonder Woman", "Falcon", "Winter Soldier"), "Superheroes"),
        Question(27, "⚡ 🏃‍♂️ 🔴 💨", "The Flash", listOf("The Flash", "Quicksilver", "Sonic", "Dash"), "Superheroes"),
        Question(28, "👑 🐆 🌍 🖤", "Black Panther", listOf("Black Panther", "Catwoman", "Wolverine", "Venom"), "Superheroes"),
        Question(29, "🏹 🎯 👁️", "Hawkeye", listOf("Hawkeye", "Green Arrow", "Robin Hood", "Legolas"), "Superheroes"),
        Question(30, "👸 ⚔️ 🛡️ 👑", "Wonder Woman", listOf("Wonder Woman", "Supergirl", "Batgirl", "Captain Marvel"), "Superheroes"),
        Question(31, "🔱 🌊 🐠 🐬", "Aquaman", listOf("Aquaman", "Namor", "Poseidon", "Percy Jackson"), "Superheroes"),
        Question(32, "🐜 🔍 🦸‍♂️", "Ant-Man", listOf("Ant-Man", "Wasp", "Atom", "Spider-Man"), "Superheroes"),
        Question(33, "⚔️ 🔴 🖤 💀 🌮", "Deadpool", listOf("Deadpool", "Harley Quinn", "Punisher", "Taskmaster"), "Superheroes"),

        // --- CARTOONS ---
        Question(34, "🧽 🍍 🌊 👖", "SpongeBob SquarePants", listOf("SpongeBob SquarePants", "Patrick Star", "Squidward", "Aquaman"), "Cartoons"),
        Question(35, "🐭 🏰 🔴 🖤", "Mickey Mouse", listOf("Mickey Mouse", "Bugs Bunny", "Tom and Jerry", "Donald Duck"), "Cartoons"),
        Question(36, "🐱 🐭 🧀 🔨", "Tom and Jerry", listOf("Tom and Jerry", "Garfield", "Looney Tunes", "Oggy"), "Cartoons"),
        Question(37, "👦 🐶 🌭 🚐", "Scooby-Doo", listOf("Scooby-Doo", "Paw Patrol", "Garfield", "Courage the Cowardly Dog"), "Cartoons"),
        Question(38, "👨 💛 🍩 🍺", "Homer Simpson", listOf("Homer Simpson", "Family Guy", "Futurama", "Rick and Morty"), "Cartoons"),
        Question(39, "🧪 👴 👦 🌀", "Rick and Morty", listOf("Rick and Morty", "Dexter's Lab", "Jimmy Neutron", "Gravity Falls"), "Cartoons"),
        Question(40, "👧 👧 👧 🧪 🦸‍♀️", "Powerpuff Girls", listOf("Powerpuff Girls", "Totally Spies", "Winx Club", "My Little Pony"), "Cartoons"),
        Question(41, "👹 🟢 🧅 🐴", "Shrek", listOf("Shrek", "Monsters Inc", "Trolls", "Ice Age"), "Cartoons"),

        // --- FOOD ---
        Question(42, "🍕 🇮🇹 🧀 🍕", "Pizza", listOf("Pizza", "Burger", "Pasta", "Taco"), "Food"),
        Question(43, "🍔 🍟 🥤", "Fast Food", listOf("Fast Food", "Barbecue", "Breakfast", "Buffet"), "Food"),
        Question(44, "🍣 🍱 🐟 🍚", "Sushi", listOf("Sushi", "Ramen", "Dim Sum", "Fried Rice"), "Food"),
        Question(45, "🌮 🇲🇽 🌯 🥑", "Taco", listOf("Taco", "Burrito", "Nachos", "Quesadilla"), "Food"),
        Question(46, "☕ 🥐 🥖 🇫🇷", "French Breakfast", listOf("French Breakfast", "Italian Coffee", "English Tea", "Pancakes"), "Food"),
        Question(47, "🍦 🍧 🍨 🍓", "Ice Cream", listOf("Ice Cream", "Frozen Yogurt", "Cake", "Smoothie"), "Food"),
        Question(48, "🍩 ☕ 🍫 😋", "Donut", listOf("Donut", "Bagel", "Cookie", "Muffin"), "Food"),
        Question(49, "🍿 🎬 🥤", "Movie Popcorn", listOf("Movie Popcorn", "Candy Bar", "Hot Dog", "Pretzel"), "Food"),

        // --- ANIMALS ---
        Question(50, "👑 🦁 🌾  Africa", "Lion", listOf("Lion", "Tiger", "Leopard", "Cheetah"), "Animals"),
        Question(51, "🐼 🎋 🇨🇳", "Giant Panda", listOf("Giant Panda", "Koala", "Polar Bear", "Grizzly Bear"), "Animals"),
        Question(52, "🦘 🇦🇺 👶 🎒", "Kangaroo", listOf("Kangaroo", "Koala", "Emu", "Wombat"), "Animals"),
        Question(53, "🐧 🧊 ❄️ 🏊‍♂️", "Penguin", listOf("Penguin", "Puffin", "Seal", "Walrus"), "Animals"),
        Question(54, "🦉 🌙 🌲 👁️", "Owl", listOf("Owl", "Eagle", "Falcon", "Bat"), "Animals"),
        Question(55, "🐬 🌊 🎪 🧠", "Dolphin", listOf("Dolphin", "Whale", "Shark", "Seal"), "Animals"),
        Question(56, "🦒 🦒 🌿 🦒", "Giraffe", listOf("Giraffe", "Zebra", "Elephant", "Camel"), "Animals"),
        Question(57, "🐝 🍯 🌺 💛", "Honey Bee", listOf("Honey Bee", "Butterfly", "Wasp", "Ladybug"), "Animals"),

        // --- SPORTS ---
        Question(58, "⚽ 🥅 🏟️ 🏆", "Football (Soccer)", listOf("Football (Soccer)", "Basketball", "Tennis", "Baseball"), "Sports"),
        Question(59, "🏀 🗑️ 👟 ⛹️‍♂️", "Basketball", listOf("Basketball", "Volleyball", "Handball", "Baseball"), "Sports"),
        Question(60, "🎾 🎾 🏸 🏆", "Tennis", listOf("Tennis", "Badminton", "Squash", "Table Tennis"), "Sports"),
        Question(61, "⚾ 🧢 🏏 🏟️", "Baseball", listOf("Baseball", "Cricket", "Golf", "Softball"), "Sports"),
        Question(62, "🥊 🥊 🤼‍♂️ 🏆", "Boxing", listOf("Boxing", "Wrestling", "Karate", "Judo"), "Sports"),
        Question(63, "⛳ 🏌️‍♂️ ⛳ 🟢", "Golf", listOf("Golf", "Billiards", "Bowling", "Curling"), "Sports"),
        Question(64, "🏎️ 🏁 🏆 💨", "Formula 1", listOf("Formula 1", "NASCAR", "Rally", "Motocross"), "Sports"),
        Question(65, "🏊‍♂️ 🌊 ⏱️ 🥇", "Swimming", listOf("Swimming", "Surfing", "Diving", "Rowing"), "Sports"),

        // --- COUNTRIES ---
        Question(66, "🗽 🇺🇸 🦅 🍔", "United States", listOf("United States", "Canada", "United Kingdom", "Australia"), "Countries"),
        Question(67, "🗼 🥐 🥖 🍷", "France", listOf("France", "Italy", "Spain", "Germany"), "Countries"),
        Question(68, "⛩️ 🍣 🗾 🌸", "Japan", listOf("Japan", "South Korea", "China", "Thailand"), "Countries"),
        Question(69, "🍝 🍕 🏛️ 🎨", "Italy", listOf("Italy", "Greece", "France", "Spain"), "Countries"),
        Question(70, "🦘 🌏 🪃 🐨", "Australia", listOf("Australia", "New Zealand", "South Africa", "Brazil"), "Countries"),
        Question(71, "🐫 🏜️ 🛕 🌴", "Egypt", listOf("Egypt", "Saudi Arabia", "Morocco", "Dubai"), "Countries"),
        Question(72, "💃 🐂 🥘 ☀️", "Spain", listOf("Spain", "Portugal", "Mexico", "Argentina"), "Countries"),
        Question(73, "🍁 🏒 ❄️ 🐻", "Canada", listOf("Canada", "Russia", "Norway", "Finland"), "Countries"),
        Question(74, "🕌 🌶️ 🥻 🏏", "India", listOf("India", "Pakistan", "Bangladesh", "Sri Lanka"), "Countries"),
        Question(75, "⚽ 💃 🌴 🔰", "Brazil", listOf("Brazil", "Argentina", "Colombia", "Peru"), "Countries"),

        // --- PLACES ---
        Question(76, "🗼 🇫🇷 🌃", "Eiffel Tower", listOf("Eiffel Tower", "Louvre", "Big Ben", "Leaning Tower of Pisa"), "Places"),
        Question(77, "🗽 🇺🇸 🌆", "Statue of Liberty", listOf("Statue of Liberty", "Empire State Building", "Golden Gate Bridge", "Mount Rushmore"), "Places"),
        Question(78, "⛩️ 🗾 🏔️ 🌸", "Mount Fuji", listOf("Mount Fuji", "Great Wall of China", "Taj Mahal", "Kyoto"), "Places"),
        Question(79, "🛕 🇮🇳 🤍 🕌", "Taj Mahal", listOf("Taj Mahal", "Red Fort", "Lotus Temple", "Angkor Wat"), "Places"),
        Question(80, "🏜️ 🛕 🐪 🇪🇬", "Pyramids of Giza", listOf("Pyramids of Giza", "Petra", "Colosseum", "Machu Picchu"), "Places"),
        Question(81, "🏛️ 🇮🇹 ⚔️ 🛡️", "Colosseum", listOf("Colosseum", "Parthenon", "Vatican", "Pompeii"), "Places"),
        Question(82, "🌉 🔴 🇺🇸 🌉", "Golden Gate Bridge", listOf("Golden Gate Bridge", "Brooklyn Bridge", "London Bridge", "Sydney Harbour Bridge"), "Places"),
        Question(83, "🏰 🇬🇧 👑 🕵️‍♂️", "Big Ben", listOf("Big Ben", "London Eye", "Tower of London", "Buckingham Palace"), "Places"),

        // --- BRANDS ---
        Question(84, "🍎 📱 💻 ⌚", "Apple", listOf("Apple", "Microsoft", "Samsung", "Google"), "Brands"),
        Question(85, "👟 📈 🏃‍♂️ 💨", "Nike", listOf("Nike", "Adidas", "Puma", "Reebok"), "Brands"),
        Question(86, "🥤 🔴 ⚪ 🧊", "Coca-Cola", listOf("Coca-Cola", "Pepsi", "Red Bull", "Sprite"), "Brands"),
        Question(87, "☕ 🧜‍♀️ 🟢 ☕", "Starbucks", listOf("Starbucks", "Dunkin'", "Nespresso", "Costa"), "Brands"),
        Question(88, "🚗 ⚡ 🔋 🚀", "Tesla", listOf("Tesla", "BMW", "Audi", "Porsche"), "Brands"),
        Question(89, "🍟 🍔 🔴 🟡", "McDonald's", listOf("McDonald's", "Burger King", "KFC", "Subway"), "Brands"),
        Question(90, "🔍 🌐 📧 📱", "Google", listOf("Google", "Yahoo", "Bing", "Meta"), "Brands"),
        Question(91, "🎮 🔴 🍄 👾", "Nintendo", listOf("Nintendo", "PlayStation", "Xbox", "Sega"), "Brands"),

        // --- GAMES ---
        Question(92, "⛏️ 🧱 🧟‍♂️ 🟩", "Minecraft", listOf("Minecraft", "Roblox", "Terraria", "Fortnite"), "Games"),
        Question(93, "🟨 👻 🍒 👾", "Pac-Man", listOf("Pac-Man", "Space Invaders", "Donkey Kong", "Tetris"), "Games"),
        Question(94, "🍄 👨‍🔧 🐢 🏰", "Super Mario", listOf("Super Mario", "Sonic the Hedgehog", "Zelda", "Kirby"), "Games"),
        Question(95, "🪂 🚌 🔫 🕺", "Fortnite", listOf("Fortnite", "PUBG", "Apex Legends", "Call of Duty"), "Games"),
        Question(96, "🗡️ 🧝‍♂️ 🛡️ 🌿", "Zelda", listOf("Zelda", "Final Fantasy", "Skyrim", "Witcher"), "Games"),
        Question(97, "🚗 💣 🏙️ 🔫", "Grand Theft Auto", listOf("Grand Theft Auto", "Cyberpunk", "Mafia", "Payday"), "Games"),

        // --- GENERAL KNOWLEDGE & FAMOUS CHARACTERS ---
        Question(98, "🕵️‍♂️ 🔎 🎻 🇬🇧", "Sherlock Holmes", listOf("Sherlock Holmes", "James Bond", "Hercule Poirot", "Doctor Who"), "Famous Characters"),
        Question(99, "👨‍🎨 🎨 🖼️ 🇮🇹", "Leonardo da Vinci", listOf("Leonardo da Vinci", "Picasso", "Van Gogh", "Michelangelo"), "Famous Characters"),
        Question(100, "👨‍🔬 🍎 💡 ⚡", "Albert Einstein", listOf("Albert Einstein", "Isaac Newton", "Nikola Tesla", "Thomas Edison"), "General Knowledge"),
        Question(101, "🚀 👨‍🚀 🌕 🇺🇸", "Moon Landing", listOf("Moon Landing", "Mars Rover", "Space Station", "Satellite"), "General Knowledge"),
        Question(102, "🎭 📜 🇬🇧 ✍️", "William Shakespeare", listOf("William Shakespeare", "Charles Dickens", "Oscar Wilde", "Edgar Allan Poe"), "Famous Characters")
    )
}

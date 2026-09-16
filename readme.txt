Build a complete, polished, production-ready native Android mobile game called **Emoji Guess**.

I want you to create the ENTIRE Android Studio project, including every required file, Kotlin source file, Gradle file, AndroidManifest, resources, themes, icons/placeholders, game logic, persistence, animations, and AdMob integration.

Do NOT give me partial code or examples. Create the actual project files in the workspace.

## 1. TECHNOLOGY

Use:

* Kotlin
* Android Studio
* Jetpack Compose
* Material 3
* Gradle Kotlin DSL
* AndroidX
* DataStore Preferences
* Google Mobile Ads SDK
* Minimum SDK: 23
* Target SDK: current Play Store supported target API
* Java/Kotlin JVM target 17

Package/application ID:

`com.example.emojiguess`

App name:

`Emoji Guess`

The project must compile successfully.

After creating the files, run a Gradle build and fix ALL compilation errors before finishing.

Do not leave TODOs, broken imports, unresolved references, or pseudo-code.

---

# 2. PROJECT STRUCTURE

Create this structure:

EmojiGuess/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/example/emojiguess/
│           │   ├── MainActivity.kt
│           │   ├── data/
│           │   │   ├── Question.kt
│           │   │   └── GamePreferences.kt
│           │   ├── ads/
│           │   │   └── AdManager.kt
│           │   ├── game/
│           │   │   └── GameViewModel.kt
│           │   └── ui/
│           │       ├── EmojiGuessApp.kt
│           │       ├── HomeScreen.kt
│           │       ├── GameScreen.kt
│           │       ├── ResultScreen.kt
│           │       ├── Components.kt
│           │       └── Theme.kt
│           └── res/
│               └── values/
│                   ├── strings.xml
│                   └── themes.xml

If additional files are necessary, create them.

---

# 3. GAME CONCEPT

The game is a simple emoji guessing game.

Example:

🦁 👑

Question:

"What does this mean?"

Answers:

The Lion King
Jungle Book
Madagascar
Tarzan

The player selects one answer.

Correct:

+100 points

Wrong:

Lose one heart.

The game should feel fast, addictive, colorful and polished.

---

# 4. HOME SCREEN

Create a professional mobile-game home screen.

Design:

* Dark background
* Purple/cyan gradient
* Large Emoji Guess logo
* Animated emoji
* Play button
* Best score
* Coins
* Settings button
* Small "Daily Challenge" button

Example visual hierarchy:

EMOJI
GUESS

🤔

"Can you guess them all?"

[ ▶ PLAY ]

🏆 Best Score: 1250

🪙 500 Coins

[ 📅 DAILY CHALLENGE ]

Make the screen responsive for different Android phone sizes.

Use Compose animations.

The logo should have a modern gaming appearance.

---

# 5. GAME SCREEN

Create a polished gameplay interface.

Top:

* Back/home button
* Score
* Hearts
* Coin count

Below:

* Progress bar
* Question number
* Streak

Main card:

* Large emoji combination
* Category
* "What does this mean?"

Below:

4 large answer buttons.

Example:

┌───────────────────────┐
│ 🦁 👑                │
│                       │
│ What does this mean? │
└───────────────────────┘

[ The Lion King ]

[ Jungle Book ]

[ Madagascar ]

[ Tarzan ]

Answer buttons must animate.

Correct answer:

* Green
* ✓ icon
* Small scale animation
* Optional confetti
* Haptic feedback
* Sound effect

Wrong answer:

* Red
* ✕ icon
* Shake animation
* Haptic feedback

Then automatically move to the next question.

---

# 6. LIVES

Start each game with:

❤️ ❤️ ❤️

A wrong answer removes one heart.

When hearts reach zero:

Show Game Over.

Do NOT immediately terminate the app.

---

# 7. SCORE

Correct answer:

100 points.

Streak bonus:

streak × 25

For example:

First correct:

100

Second:

125

Third:

150

Fourth:

175

etc.

Display the streak prominently:

🔥 5 STREAK

---

# 8. COINS

Add an in-game coin system.

Starting coins:

500

Correct answer:

+10 coins

Three-answer streak:

+25 bonus coins

Coins can be used for hints.

Persist coins using DataStore.

Never reset coins when the app is closed.

---

# 9. HINT SYSTEM

Add a Hint button.

Cost:

50 coins.

When the player uses a hint:

* Remove one incorrect answer OR
* Highlight the correct answer subtly.

The hint must only work if the player has at least 50 coins.

If insufficient coins:

Show a small Snackbar:

"Not enough coins!"

Do not allow negative coins.

---

# 10. QUESTIONS

Create at least 100 high-quality questions.

Categories:

* Movies
* Superheroes
* Cartoons
* Food
* Animals
* Sports
* Countries
* Places
* Brands
* Games
* General knowledge
* Famous characters

Every question must contain:

* emoji
* correct answer
* exactly 4 options
* category

The correct answer must always exist in the four options.

Shuffle the question order for every new game.

Shuffle the answer options while keeping the correct answer valid.

Do not duplicate the same question during a single game.

---

# 11. GAME FLOW

Implement:

Home
↓
Play
↓
Question 1
↓
Question 2
↓
Question 3
↓
...
↓
Game Over
↓
Result screen

At game over show:

GAME OVER

Score

Best Score

Coins earned

Questions answered

Correct answers

Accuracy %

Highest streak

Buttons:

[ 🔄 PLAY AGAIN ]

[ 🏠 HOME ]

[ 🎁 WATCH AD & CONTINUE ]

---

# 12. REWARDED AD

Integrate Google Mobile Ads.

Use Google's official TEST rewarded ad unit during development.

Test rewarded ad ID:

`ca-app-pub-3940256099942544/5224354917`

When the player taps:

WATCH AD & CONTINUE

show a rewarded ad.

After the reward is successfully earned:

* restore 1 heart
* return to the game
* continue from the current question

If the ad is unavailable:

Gracefully handle the failure.

Do not crash.

Preload rewarded ads.

After an ad is shown, preload another one.

Create a dedicated:

`AdManager.kt`

to keep advertising code separate from game logic.

Add clear comments showing exactly where production AdMob IDs need to be inserted.

---

# 13. INTERSTITIAL ADS

Add an interstitial ad manager using Google's official TEST interstitial ID:

`ca-app-pub-3940256099942544/1033173712`

Do not show ads after every question.

Show an interstitial only at reasonable breaks, for example after a completed game.

Limit frequency so gameplay is not annoying.

Use test IDs during development.

Make it easy to replace the IDs with production IDs later.

---

# 14. BANNER ADS

Add a small adaptive banner on appropriate non-gameplay screens.

Use Google's test banner ID:

`ca-app-pub-3940256099942544/9214589741`

Do NOT cover gameplay buttons.

Do NOT place ads where accidental clicks are likely.

The game must remain usable when the ad is unavailable.

---

# 15. DATASTORE

Use Android DataStore Preferences.

Persist:

* Best score
* Coins
* Games played
* Total correct answers
* Total questions
* Highest streak
* Sound enabled
* Vibration enabled

When the app restarts, restore the saved data.

Do not use SharedPreferences.

---

# 16. SETTINGS SCREEN

Create a simple settings dialog/screen.

Options:

🔊 Sound
📳 Vibration
ℹ️ About
🔒 Privacy Policy

Use switches for sound and vibration.

Persist the switches.

---

# 17. DAILY CHALLENGE

Create a Daily Challenge button.

For the first version, implement a deterministic daily question selection based on the current date.

The same day should give the same challenge.

Show:

DAILY CHALLENGE

🔥 Today's Challenge

One special question.

Allow the user to complete it once per day.

Persist the completion date.

Give a coin reward when completed.

Do not require a server.

---

# 18. ANIMATIONS

Use Jetpack Compose animations.

Include:

* Fade-in
* Slide-in
* Scale animation
* Button press animation
* Correct answer animation
* Wrong answer shake
* Progress bar animation
* Streak animation
* Coin animation
* Game-over entrance animation

Keep animations smooth and lightweight.

Do not make them excessive.

---

# 19. HAPTIC FEEDBACK

Use Android haptic feedback.

Correct:

light/medium feedback.

Wrong:

different feedback.

Button press:

light feedback.

Respect the vibration setting.

If vibration is disabled, do not vibrate.

---

# 20. SOUND

Implement sound support cleanly.

If no custom audio files are available, do not create broken resource references.

Create a simple sound manager architecture that can later accept:

correct.mp3
wrong.mp3
click.mp3
game_over.mp3

The app must still compile and work if these files are not included.

Respect the Sound setting.

---

# 21. UI DESIGN

Make the app look like a modern casual mobile game.

Color style:

* Dark navy/black background
* Purple primary
* Cyan secondary
* Gold for coins
* Green correct
* Red wrong
* White text

Use gradients and rounded cards.

Corner radius:

16–28 dp.

Buttons should be large enough for touch.

Use typography hierarchy.

Avoid a plain/default Android appearance.

---

# 22. RESPONSIVE DESIGN

The game must work on:

* small phones
* normal phones
* large phones
* portrait orientation

Do not hardcode screen widths.

Use:

* Modifier.fillMaxWidth()
* adaptive padding
* BoxWithConstraints where appropriate
* vertical scrolling when necessary

Prevent text clipping.

Prevent answer buttons from going outside the screen.

---

# 23. ACCESSIBILITY

Add:

* content descriptions
* sufficient contrast
* sensible touch targets
* readable text sizes

Emoji itself should be visually prominent.

---

# 24. GAME VIEWMODEL

Use a ViewModel.

Do not put all game logic inside MainActivity.

Create:

`GameViewModel.kt`

It should manage:

* current question
* score
* lives
* coins
* streak
* answers
* game state
* game over
* hints
* daily challenge
* statistics

Expose state using Compose-friendly state.

Avoid unnecessary recompositions.

---

# 25. ARCHITECTURE

Use a clean structure:

UI
↓
ViewModel
↓
Data / Preferences

AdManager should be independent.

QuestionBank should be independent.

Do not create unnecessary complexity.

This is a small offline game.

---

# 26. MAIN ACTIVITY

`MainActivity.kt`

Responsibilities:

* initialize Mobile Ads
* set Compose content
* apply app theme
* host EmojiGuessApp

Do not put all game logic in MainActivity.

---

# 27. THEME

Create:

`Theme.kt`

Use Material 3.

Create a custom dark color scheme.

Primary:

Purple

Secondary:

Cyan

Tertiary:

Gold

Background:

Very dark navy/black

Cards:

Dark purple/navy

---

# 28. APP ICON

Create a simple vector app icon if possible.

Concept:

A large:

🤔

inside a rounded colorful game-style icon.

If an actual emoji cannot be used as a vector safely, create a clean abstract icon using Compose/vector drawable shapes.

The icon should look good at small sizes.

---

# 29. PLAY STORE PREPARATION

Configure the app for Google Play.

Application ID:

`com.example.emojiguess`

Version:

1.0.0

versionCode:

1

Use release build configuration.

The project must be capable of generating:

`app-release.aab`

Do not hardcode a signing password into the project.

Do not commit private signing keys.

Create comments/instructions for creating a release keystore.

---

# 30. ADMOB PRODUCTION SWITCH

Clearly mark all test AdMob IDs.

Use:

TEST APP ID:

`ca-app-pub-3940256099942544~3347511713`

TEST BANNER:

`ca-app-pub-3940256099942544/9214589741`

TEST INTERSTITIAL:

`ca-app-pub-3940256099942544/1033173712`

TEST REWARDED:

`ca-app-pub-3940256099942544/5224354917`

Create constants in `AdManager.kt`.

Example architecture:

object AdConfig {
const val APP_ID = "..."
const val BANNER_ID = "..."
const val INTERSTITIAL_ID = "..."
const val REWARDED_ID = "..."
}

Clearly comment:

"Replace these test IDs with your production AdMob IDs before release."

Do not use production IDs during development.

---

# 31. PRIVACY

Create a simple privacy-policy placeholder screen inside the app explaining that the application may use advertising services.

Do not claim that no data is collected if advertising SDKs are present.

Add a clearly labeled placeholder:

"Privacy Policy"

with a place where the developer can later put the real privacy-policy URL.

Do not invent a fake privacy-policy URL.

---

# 32. ERROR HANDLING

The app must gracefully handle:

* Ad unavailable
* DataStore read failure
* DataStore write failure
* Empty question list
* Activity recreation
* Invalid question
* Unexpected state

Never crash because an ad fails.

Never allow:

negative coins

negative lives

invalid question indexes

---

# 33. GAME OVER CONTINUE LOGIC

If player has:

❤️ 0

and watches a rewarded ad successfully:

restore:

❤️ 1

Continue playing.

Do not restart the whole game.

If the player closes the ad without earning the reward:

do not restore the heart.

---

# 34. STATS

Show on the result screen:

Score
Best Score
Correct
Wrong
Accuracy
Highest streak
Coins earned

Example:

SCORE
1250

🎯 Correct: 12
❌ Wrong: 3
📊 Accuracy: 80%
🔥 Best Streak: 6
🪙 Coins: +145

---

# 35. CODE QUALITY

Follow Kotlin best practices.

Use:

* data classes
* sealed classes where appropriate
* immutable UI state
* ViewModel
* coroutines
* DataStore
* Compose state

Avoid:

* global mutable state
* huge MainActivity
* duplicated code
* unnecessary dependencies
* deprecated APIs

Use meaningful names.

Add comments only where useful.

---

# 36. BUILD VALIDATION

After generating the project:

1. Sync Gradle.
2. Run compilation.
3. Fix all errors.
4. Run unit/build checks if available.
5. Generate a debug APK.
6. Generate a release AAB if signing configuration permits.
7. Verify there are no unresolved imports.
8. Verify AndroidManifest is valid.
9. Verify AdMob initialization does not crash.
10. Verify the game can run without internet except ads.

If any error occurs, fix it yourself before finishing.

---

# 37. IMPORTANT

Do not answer with only code snippets.

Actually create/update the files in the workspace.

Do not omit files.

Do not use placeholder Kotlin such as:

TODO()
throw NotImplementedException()
"implement this later"

Everything must be implemented.

If a feature cannot be implemented exactly because an external account is required, implement the application-side structure and clearly isolate the required account configuration.

---

# 38. FINAL VERIFICATION

Before saying the task is complete, verify:

* App launches.
* Home screen works.
* Play button works.
* Questions display.
* Four answers display.
* Correct answer works.
* Wrong answer works.
* Hearts work.
* Score works.
* Streak works.
* Coins work.
* Hint works.
* Game over works.
* Restart works.
* Home works.
* Best score persists.
* Coins persist.
* Settings persist.
* Rewarded ad integration works with test ID.
* Interstitial integration works with test ID.
* Banner integration works with test ID.
* No crash when ads are unavailable.
* Daily challenge works.
* Project compiles.
* Release configuration exists.

Finally, tell me:

1. Which files you created.
2. Whether Gradle build succeeded.
3. Any warnings that remain.
4. Exact command to build the APK.
5. Exact command to build the AAB.
6. Which AdMob IDs I must replace before publishing.
7. Which Play Store configuration items I still need to provide manually.

Do not modify the package name unless absolutely necessary.

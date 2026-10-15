package com.example.data

import com.example.model.GradeLevel

object CurriculumWordRepository {

    // Themes IDs
    const val THEME_ANIMALS = "animals"
    const val THEME_SPACE = "space"
    const val THEME_OCEAN = "ocean"
    const val THEME_MAGIC = "magic"
    const val THEME_FOOD = "food"
    const val THEME_SPORTS = "sports"
    const val THEME_NATURE = "nature"
    const val THEME_SIGHT_WORDS = "sight_words"

    // High quality curriculum word collections organized by Theme and Grade Level
    private val themeWordMap: Map<String, Map<GradeLevel, List<String>>> = mapOf(
        THEME_ANIMALS to mapOf(
            GradeLevel.PRE_K to listOf("CAT", "DOG", "PIG", "COW", "FOX", "BEE", "BUG", "HEN", "ANT", "BAT"),
            GradeLevel.KINDERGARTEN to listOf("FROG", "BEAR", "DUCK", "LION", "BIRD", "DEER", "FISH", "GOAT", "SEAL", "WOLF"),
            GradeLevel.GRADE_1 to listOf("HORSE", "SHEEP", "ZEBRA", "TIGER", "PUPPY", "BUNNY", "OTTER", "ROBIN", "CAMEL", "MOOSE"),
            GradeLevel.GRADE_2 to listOf("MONKEY", "PANDA", "GIRAFFE", "RABBIT", "DONKEY", "BADGER", "BEAVER", "PARROT", "TURTLE", "JAGUAR"),
            GradeLevel.GRADE_3 to listOf("DOLPHIN", "PENGUIN", "CHEETAH", "GORILLA", "LEOPARD", "HAMSTER", "OSTRICH", "KANGAROO", "RACCOON", "BUFFALO"),
            GradeLevel.GRADE_4 to listOf("CHAMELEON", "ALLIGATOR", "FLAMINGO", "HEDGEHOG", "PORCUPINE", "ARMADILLO", "CROCODILE", "PLATYPUS", "OCTOPUS"),
            GradeLevel.GRADE_5 to listOf("CHIMPANZEE", "SALAMANDER", "RHINOCEROS", "ORANGUTAN", "HIPPOPOTAMUS", "WOODPECKER", "TARANTULA", "BARRACUDA")
        ),
        THEME_SPACE to mapOf(
            GradeLevel.PRE_K to listOf("SUN", "STAR", "MOON", "SKY", "WARM", "RAY", "GLOW", "HOT", "BEAM"),
            GradeLevel.KINDERGARTEN to listOf("MARS", "SHIP", "ROCK", "EARTH", "NIGHT", "LIGHT", "DARK", "SHINE", "ORBIT"),
            GradeLevel.GRADE_1 to listOf("VENUS", "COMET", "SOLAR", "SPACE", "ALIEN", "PROBE", "WORLD", "PILOT", "RADAR"),
            GradeLevel.GRADE_2 to listOf("PLANET", "ROCKET", "CRATER", "METEOR", "SATURN", "NEBULA", "LAUNCH", "GALAXY", "OXYGEN"),
            GradeLevel.GRADE_3 to listOf("JUPITER", "URANUS", "ECLIPSE", "GRAVITY", "HORIZON", "CAPSULE", "BOOSTER", "ATMOSPHERE", "COSMIC"),
            GradeLevel.GRADE_4 to listOf("ASTRONAUT", "TELESCOPE", "ASTEROID", "SATELLITE", "NEPTUNE", "UNIVERSE", "SOLARIS", "MISSION"),
            GradeLevel.GRADE_5 to listOf("CONSTELLATION", "SUPERNOVA", "TRAJECTORY", "SPACECRAFT", "WEIGHTLESS", "INTERSTELLAR", "ASTRONOMY", "GRAVITATION")
        ),
        THEME_OCEAN to mapOf(
            GradeLevel.PRE_K to listOf("FISH", "SEA", "WAVE", "SAND", "CRAB", "FIN", "WET", "BLUE", "SWIM"),
            GradeLevel.KINDERGARTEN to listOf("WHALE", "CORAL", "SHELL", "SHARK", "SHORE", "TIDE", "SURF", "BOAT", "GULL"),
            GradeLevel.GRADE_1 to listOf("CLAM", "OTTER", "SQUID", "REEF", "DIVER", "OCEAN", "FLOAT", "SPLASH", "BEACH"),
            GradeLevel.GRADE_2 to listOf("DOLPHIN", "TURTLE", "STARFISH", "LOBSTER", "CURRENT", "SEAGULL", "ISLAND", "ANCHOR", "PEARL"),
            GradeLevel.GRADE_3 to listOf("OCTOPUS", "JELLYFISH", "SEAHORSE", "PELICAN", "TSUNAMI", "STINGRAY", "HARBOR", "LAGOON", "BARRIER"),
            GradeLevel.GRADE_4 to listOf("SUBMARINE", "SWORDFISH", "SHIPWRECK", "ANEMONE", "ECOSYSTEM", "BARNACLE", "TRENCH", "CREATURE"),
            GradeLevel.GRADE_5 to listOf("BIOLUMINESCENT", "CRUSTACEAN", "HYDROTHERMAL", "SUBMERSIBLE", "PLANKTON", "ECOSYSTEM", "BARRACUDA")
        ),
        THEME_MAGIC to mapOf(
            GradeLevel.PRE_K to listOf("CAT", "HAT", "WISH", "STAR", "MOON", "RING", "BOOK", "GOLD", "KING"),
            GradeLevel.KINDERGARTEN to listOf("FAIRY", "WAND", "CROWN", "QUEEN", "MAGIC", "SPELL", "CLOAK", "GOWN", "ROBE"),
            GradeLevel.GRADE_1 to listOf("GIANT", "TOWER", "KNIGHT", "CASTLE", "DRAGON", "PRINCE", "SWORD", "SHIELD", "CHARM"),
            GradeLevel.GRADE_2 to listOf("WIZARD", "POTION", "CRYSTAL", "SPARKLE", "UNICORN", "SHADOW", "MIRROR", "SCROLL", "HERO"),
            GradeLevel.GRADE_3 to listOf("SORCERER", "KINGDOM", "TREASURE", "MYSTERY", "FORTRESS", "TALISMAN", "WARRIOR", "ILLUSION"),
            GradeLevel.GRADE_4 to listOf("ENCHANTMENT", "APPRENTICE", "SPELLBOOK", "LABYRINTH", "LEGENDARY", "MYTHOLOGY", "GUARDIAN"),
            GradeLevel.GRADE_5 to listOf("INCANTATION", "ALCHEMIST", "SUPERPOWER", "EXTRAORDINARY", "SORCERY", "PHENOMENON", "TRANSFORMATION")
        ),
        THEME_FOOD to mapOf(
            GradeLevel.PRE_K to listOf("JAM", "EGG", "NUT", "PIE", "BUN", "HAM", "FIG", "PEA", "TEA", "MILK"),
            GradeLevel.KINDERGARTEN to listOf("APPLE", "BREAD", "CAKE", "CORN", "RICE", "SOUP", "PEAR", "PLUM", "BEAN", "MEAT"),
            GradeLevel.GRADE_1 to listOf("BERRY", "CHEESE", "GRAPE", "HONEY", "LEMON", "MELON", "PIZZA", "TOAST", "WATER", "SALAD"),
            GradeLevel.GRADE_2 to listOf("BANANA", "COOKIE", "CARROT", "MUFFIN", "ORANGE", "POTATO", "TOMATO", "WAFFLE", "BUTTER", "PEACH"),
            GradeLevel.GRADE_3 to listOf("PANCAKE", "AVOCADO", "BROCCOLI", "NOODLE", "CUPCAKE", "SANDWICH", "POPCORN", "CHERRY", "PUMPKIN"),
            GradeLevel.GRADE_4 to listOf("SPAGHETTI", "WATERMELON", "STRAWBERRY", "BLUEBERRY", "CHOCOLATE", "BREAKFAST", "NUTRITION", "PINEAPPLE"),
            GradeLevel.GRADE_5 to listOf("REFRIGERATOR", "INGREDIENTS", "DELICIOUS", "APPETIZING", "VEGETABLES", "HEALTHFUL", "CULINARY")
        ),
        THEME_SPORTS to mapOf(
            GradeLevel.PRE_K to listOf("RUN", "HOP", "JUMP", "BALL", "TAG", "WIN", "HIT", "KICK", "BAT"),
            GradeLevel.KINDERGARTEN to listOf("PLAY", "TEAM", "GOAL", "RACE", "SWIM", "PASS", "FAST", "GAME", "CATCH"),
            GradeLevel.GRADE_1 to listOf("SKATE", "TRACK", "SCORE", "COACH", "MEDAL", "CLIMB", "CHEER", "RELAY", "DODGE"),
            GradeLevel.GRADE_2 to listOf("SOCCER", "TENNIS", "HOCKEY", "HELMET", "RUNNER", "DEFENSE", "STRIKE", "TROPHY", "LEAGUE"),
            GradeLevel.GRADE_3 to listOf("BASEBALL", "CHAMPION", "ATHLETE", "DRIBBLE", "PRACTICE", "STADIUM", "FITNESS", "PADDLE"),
            GradeLevel.GRADE_4 to listOf("BASKETBALL", "GYMNASTICS", "VOLLEYBALL", "TOURNAMENT", "COMPETITION", "SNOWBOARD", "BADMINTON"),
            GradeLevel.GRADE_5 to listOf("CHAMPIONSHIP", "SPORTSMANSHIP", "INTERSCHOLASTIC", "ENDURANCE", "DECATHLON", "PROFESSIONAL")
        ),
        THEME_NATURE to mapOf(
            GradeLevel.PRE_K to listOf("TREE", "LEAF", "RAIN", "SNOW", "WIND", "MUD", "LOG", "BUD", "DIRT"),
            GradeLevel.KINDERGARTEN to listOf("PLANT", "RIVER", "GRASS", "STONE", "FLOWER", "STORM", "CLOUD", "WOODS", "HILL"),
            GradeLevel.GRADE_1 to listOf("FOREST", "STREAM", "GARDEN", "VALLEY", "DESERT", "BRANCH", "SPRING", "AUTUMN", "BREEZE"),
            GradeLevel.GRADE_2 to listOf("MEADOW", "JUNGLE", "SUNRISE", "CANYON", "WEATHER", "ROOTS", "BLOSSOM", "PUDDLE", "RAINBOW"),
            GradeLevel.GRADE_3 to listOf("MOUNTAIN", "WATERFALL", "VOLCANO", "THUNDER", "WILDLIFE", "GLACIER", "SUNLIGHT", "HABITAT"),
            GradeLevel.GRADE_4 to listOf("RAINFOREST", "HURRICANE", "LIGHTNING", "ECOSYSTEM", "LANDSCAPE", "ATMOSPHERE", "VEGETATION"),
            GradeLevel.GRADE_5 to listOf("BIODIVERSITY", "CONSERVATION", "PRECIPITATION", "DEFORESTATION", "SUSTAINABLE", "ENVIRONMENT")
        ),
        THEME_SIGHT_WORDS to mapOf(
            GradeLevel.PRE_K to listOf("SEE", "CAN", "AND", "BIG", "RED", "RUN", "ONE", "TWO", "LOOK", "HELP", "PLAY", "AWAY"),
            GradeLevel.KINDERGARTEN to listOf("LIKE", "HAVE", "COME", "SAID", "WANT", "GOOD", "MAKE", "LITTLE", "WITH", "THIS", "THEY"),
            GradeLevel.GRADE_1 to listOf("AFTER", "COULD", "EVERY", "ROUND", "THINK", "WALK", "GOING", "THANK", "KNOW", "AGAIN", "SOME"),
            GradeLevel.GRADE_2 to listOf("ALWAYS", "AROUND", "BECAUSE", "BEFORE", "FOUND", "WRITE", "SLEEP", "GREEN", "FIRST", "RIGHT"),
            GradeLevel.GRADE_3 to listOf("BETTER", "CARRY", "CLEAN", "DRINK", "EIGHT", "LAUGH", "LIGHT", "SEVEN", "START", "TOGETHER"),
            GradeLevel.GRADE_4 to listOf("ACROSS", "BEAUTIFUL", "BECOME", "CERTAIN", "DISCOVER", "EXAMPLE", "EXPLAIN", "REMEMBER", "SCIENCE"),
            GradeLevel.GRADE_5 to listOf("COMMUNITY", "CONCLUSION", "EXPERIMENT", "GOVERNMENT", "KNOWLEDGE", "ORGANISM", "POPULATION", "STRATEGY")
        )
    )

    fun getWords(themeId: String, grade: GradeLevel): List<String> {
        return themeWordMap[themeId]?.get(grade)
            ?: themeWordMap[THEME_ANIMALS]?.get(grade)
            ?: listOf("CAT", "DOG", "SUN", "FUN")
    }

    fun getAllThemes(): List<Pair<String, String>> {
        return listOf(
            THEME_ANIMALS to "🐾 Animal Safari",
            THEME_SPACE to "🚀 Cosmic Space",
            THEME_OCEAN to "🌊 Ocean Splash",
            THEME_MAGIC to "🏰 Magic & Fantasy",
            THEME_FOOD to "🍎 Tasty Treats",
            THEME_SPORTS to "⚽ Sports Stars",
            THEME_NATURE to "🌿 Nature Wonders",
            THEME_SIGHT_WORDS to "📚 Sight Words"
        )
    }
}

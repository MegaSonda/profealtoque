import com.example.ChatGPTAssitant.ChatGPTAssistant

class VirtualProfessor(private val apiKey: String) {

    private val chatAssistant = ChatGPTAssistant()

    // Palabras clave para identificar saludos y preguntas generales
    private val greetings = listOf("hola", "buenos días", "buenas tardes", "saludos")
    private val generalQuestions = listOf("cómo estás", "quién eres", "qué puedes hacer", "ayuda")

    // Palabras clave específicas para cada asignatura
    private val mathKeywords = listOf(
        "matemática", "número", "ecuación", "geometría", "álgebra", "radio", "figura", "área", "volumen",
        "ángulo", "triángulo", "fórmula", "parábola", "calcular",
        "logaritmo", "función", "derivada", "integral", "límite", "teorema",
        "ecuaciones cuadráticas", "sistemas de ecuaciones", "factorización", "exponente", "radical",
        "polinomios", "racional", "trigonometría", "sen", "coseno", "tangente", "identidades trigonométricas",
        "cosecante", "secante", "cotangente", "matrices", "determinante", "vectores", "planos", "cónicas",
        "teorema de Pitágoras", "ecuaciones lineales", "gráficas", "expresiones algebraicas"
    )
    val questionCategories = mapOf(
        "writingOrVocabulary" to listOf("sinonimo", "antonimo", "palabras relacionadas"),
        "definition" to listOf("qué es", "que es", "qué significa", "que significa"),
        "literaryAnalysis" to listOf("analisis literario", "tema principal", "recursos literarios"),
        "grammar" to listOf("gramática", "sintaxis", "reglas gramaticales")
    )
    private fun normalizeQuestion(question: String): String {
        // Convert the question to lowercase
        val lowerCaseQuestion = question.lowercase()

        // Remove any unnecessary punctuation (optional)
        val normalizedQuestion = lowerCaseQuestion.replace(Regex("[^a-zA-Z0-9áéíóúüñ ]"), "")

        return normalizedQuestion
    }

    private fun isQuestionOfType(question: String, category: String): Boolean {
        val normalizedQuestion = normalizeQuestion(question)
        val keywords = questionCategories[category] ?: return false
        return keywords.any { normalizedQuestion.contains(it, ignoreCase = true) }
    }

    val languageKeywords = listOf(
        "sustantivo", "clasificación de sustantivos", "verbo transitivo", "verbo intransitivo",
        "metáfora", "comparación", "sujeto tácito", "conectores", "texto", "adjetivo calificativo",
        "sinónimo", "antónimo", "resumen", "idea principal", "texto narrativo", "texto argumentativo",
        "oración compuesta", "prosodia", "signos de puntuación", "género literario", "pronombres",
        "figura retórica", "oración subordinada", "análisis sintáctico", "preposiciones",
        "texto descriptivo"
    )

    //historia
    // Palabras clave ampliadas para historia
    private val expandedHistoryKeywords = listOf(
        "historia", "civilización", "revolución", "época", "acontecimiento", "histórico", "guerra", "antigüedad",
        "edad media", "moderno", "conquista", "descubrimiento", "explorador", "colonización", "imperio",
        "napoleón", "cristóbal colón", "independencia", "segunda guerra mundial", "francisco pizarro",
        "imperio romano", "french revolution", "invierno ruso", "renacimiento", "ilustración", "modernismo",
        "revolución industrial", "presidente", "pandemia", "peste negra", "guerra mundial", "edad antigua",
        "dinastía", "caída del imperio romano", "revolución francesa", "guerra fría", "crisis económica",
        "invasión", "tratado de paz", "sistema feudal", "imperio otomano", "reformas religiosas", "plaga", "colonias",
        "tratado de versalles", "expansión territorial", "guerreros", "batalla de stalingrado", "renacimiento",
        "ilustración", "esclavitud", "revolución industrial", "descubrimiento de américa", "nazi", "fascismo", "comunismo"
    )
    // Función para detectar preguntas históricas generales, sin depender de figuras específicas
    // Función para detectar preguntas históricas generales
    // Función para detectar preguntas históricas generales, sin depender de figuras específicas
    private fun isGeneralHistoricalQuestion(question: String): Boolean {
        val questionLower = normalizeQuestion(question).lowercase()
        return expandedHistoryKeywords.any { questionLower.contains(it) } ||
                questionLower.contains("historia", ignoreCase = true) ||
                questionLower.contains("evento histórico", ignoreCase = true) ||
                questionLower.contains("acontecimiento histórico", ignoreCase = true)
    }
    // Función para detectar preguntas históricas sobre figuras específicas
    private fun isHistoricalFigureQuestion(question: String): Boolean {
        val questionLower = normalizeQuestion(question).lowercase()
        val historicalFigures = listOf("napoleón", "bonaparte", "cristóbal colón", "francisco pizarro", "alexander el grande", "cleopatra")
        return historicalFigures.any { questionLower.contains(it) } &&
                (questionLower.contains("quién", ignoreCase = true) ||
                        questionLower.contains("era", ignoreCase = true) ||
                        questionLower.contains("hizo", ignoreCase = true) ||
                        questionLower.contains("qué hizo", ignoreCase = true))
    }


    // Función para detectar preguntas sobre figuras históricas
    private fun isFigureHistoricalQuestion(question: String): Boolean {
        return question.lowercase().contains("quién es", ignoreCase = true) &&
                (question.lowercase().contains("napoleón", ignoreCase = true) ||
                        question.lowercase().contains("napoleon", ignoreCase = true) ||
                        question.lowercase().contains("bonaparte", ignoreCase = true))
    }


    // Función principal para detectar preguntas históricas
    private fun isHistoricalQuestion(question: String): Boolean {
        return isGeneralHistoricalQuestion(question) ||
                isHistoricalFigureQuestion(question) ||
                question.lowercase().contains("cómo", ignoreCase = true) ||
                question.lowercase().contains("por qué", ignoreCase = true)
    }
    // Identificar preguntas tipo "¿Qué es...?" o "¿Qué significa...?"
    private fun isDefinitionQuestion(question: String): Boolean {
        return question.lowercase().startsWith("qué es") || question.lowercase().startsWith("qué significa")
    }

    // Identificar peticiones de fórmulas o análisis de textos
    private fun isLiteraryAnalysisRequest(question: String): Boolean {
        return question.lowercase().contains("análisis", ignoreCase = true) ||
                question.lowercase().contains("género literario", ignoreCase = true) ||
                question.lowercase().contains("recurso literario", ignoreCase = true) ||
                question.lowercase().contains("figura literaria", ignoreCase = true)
    }

    // Identificar preguntas sobre gramática o sintaxis
    private fun isGrammarOrSyntaxRequest(question: String): Boolean {
        return question.lowercase().contains("gramática", ignoreCase = true) ||
                question.lowercase().contains("sintaxis", ignoreCase = true) ||
                question.lowercase().contains("ortografía", ignoreCase = true)
    }

    // Identificar operaciones de redacción y vocabulario
    private fun isWritingOrVocabularyRequest(question: String): Boolean {
        return question.lowercase().contains("redacción", ignoreCase = true) ||
                question.lowercase().contains("coherencia", ignoreCase = true) ||
                question.lowercase().contains("sinónimo", ignoreCase = true) ||
                question.lowercase().contains("antónimo", ignoreCase = true) ||
                question.lowercase().contains("vocabulario", ignoreCase = true)
    }

    private fun isGreetingOrGeneralQuestion(question: String): Boolean {
        return greetings.any { question.contains(it, ignoreCase = true) } ||
                generalQuestions.any { question.contains(it, ignoreCase = true) }
    }

    private fun isRelevantQuestion(question: String, keywords: List<String>): Boolean {
        return keywords.any { keyword -> question.contains(keyword, ignoreCase = true) }
    }

    fun onMathProfessorSelected(question: String, callback: (String?) -> Unit) {
        if (isGreetingOrGeneralQuestion(question)) {
            callback("¡Hola! Soy tu asistente de Matemáticas. ¿En qué puedo ayudarte?")
        } else if (isRelevantQuestion(question, mathKeywords)) {
            val prompt = "Eres un profesor de matemáticas. Responde la pregunta de manera clara y detallada: $question"
            chatAssistant.askQuestion(apiKey, prompt, callback)
        } else {
            callback("Esta pregunta no parece estar relacionada con matemáticas. Intenta hacer una pregunta sobre temas de matemáticas.")
        }
    }

    fun onLanguageProfessorSelected(question: String, callback: (String?) -> Unit) {
        val normalizedQuestion = normalizeQuestion(question)
        val category = when {
            isGreetingOrGeneralQuestion(normalizedQuestion) -> "greeting"
            isQuestionOfType(normalizedQuestion, "writingOrVocabulary") -> "writingOrVocabulary"
            isQuestionOfType(normalizedQuestion, "definition") -> "definition"
            isQuestionOfType(normalizedQuestion, "literaryAnalysis") -> "literaryAnalysis"
            isQuestionOfType(normalizedQuestion, "grammar") -> "grammar"
            else -> null
        }

        if (category != null) {
            val prompt = when (category) {
                "greeting" -> "¡Hola! Soy tu asistente de Lenguaje. ¿En qué puedo ayudarte?"
                else -> "Eres un profesor de lenguaje. Responde de manera clara y detallada: $question"
            }
            chatAssistant.askQuestion(apiKey, prompt, callback)
        } else {
            callback("Esta pregunta no parece estar relacionada con lenguaje. Intenta hacer una pregunta sobre temas de lenguaje.")
        }
    }

    fun onHistoryProfessorSelected(question: String, callback: (String?) -> Unit) {
        if (isGreetingOrGeneralQuestion(question)) {
            callback("¡Hola! Soy tu asistente de Historia. ¿En qué puedo ayudarte?")
        } else if (isHistoricalQuestion(question)) {
            val prompt = "Eres un profesor de historia. Responde de manera clara y detallada: $question"
            chatAssistant.askQuestion(apiKey, prompt, callback)
        } else {
            callback("Esta pregunta no parece estar relacionada con historia. Intenta hacer una pregunta sobre temas de historia.")
        }
    }
}

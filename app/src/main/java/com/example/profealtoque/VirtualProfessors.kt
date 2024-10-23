import android.os.Handler
import android.os.Looper
import com.example.profealtoque.ChatActivity

class VirtualProfessor(private val apiKey: String) {
    private val chatGPTAssistant = ChatGPTAssistant()
    private val handler = Handler(Looper.getMainLooper())

    // Estas funciones pueden invocar a la API en el futuro, pero ahora no se usan.
    fun onMathProfessorSelected(activity: ChatActivity) {
        // Simulación o lógica de API futura
    }

    fun onLanguageProfessorSelected(activity: ChatActivity) {
        // Simulación o lógica de API futura
    }

    fun onHistoryProfessorSelected(activity: ChatActivity) {
        // Simulación o lógica de API futura
    }
}

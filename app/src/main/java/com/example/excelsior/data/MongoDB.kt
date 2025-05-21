import com.example.excelsior.data.models.Produto
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDB {
    private var realm: Realm? = null
    private val app = App.create("61f09dd3bcdea04a53ac5161") // Substitua pelo seu App ID

    fun getInstance(): Realm {
        if (realm == null || realm!!.isClosed()) {
            val user = app.currentUser ?: throw IllegalStateException("Nenhum usuário autenticado encontrado.")
            val syncConfig = SyncConfiguration.Builder(user, "61f09dd3bcdea04a53ac5161", setOf(Produto::class)) // Pass partitionValue here
                .build()
            realm = Realm.open(syncConfig)
        }
        return realm!!
    }
}
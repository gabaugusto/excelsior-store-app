import com.example.excelsior.data.models.Produto
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object MongoDB {
    private var realm: Realm? = null

    suspend fun getInstance(): Realm {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(Produto::class)
            )
                .syncDirectory("your-realm-app-id") // Substitua pelo seu App ID
                .build()
            realm = Realm.open(config)
        }
        return realm!!
    }
}
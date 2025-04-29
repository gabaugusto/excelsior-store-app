# 📱 Como programar Excelsior Store em projeto completamente novo?

Vamos detalhar arquivo por arquivo os códigos essenciais para um projeto novo em Jetpack Compose com MongoDB Realm, desde a configuração inicial até a UI. 

### 1. build.gradle (Module: app)

```groovy
plugins {
    id("kotlin-kapt") // Para Realm (se usar anotações)
}

dependencies {

    // ... (Dependências do projeto que já existem)
 
    // Realm MongoDB
    implementation(libs.library.sync)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
```

### 2. AndroidManifest.xml

Dentro de um projeto em android, o arquivo AndroidManifest.xml é fundamental para definir as permissões e configurações do aplicativo. Para garantir que o aplicativo tenha acesso à internet, é necessário adicionar a seguinte linha dentro da tag `<manifest>`:

```xml
<manifest ...>
    <uses-permission android:name="android.permission.INTERNET" />
    
   
</manifest>
```

Outras permissões podem ser necessárias dependendo das funcionalidades do seu aplicativo, como acesso à câmera, armazenamento, etc. No entanto, para o funcionamento básico do MongoDB Realm e a sincronização de dados, a permissão de internet é a mais importante.

### 3. MainActivity.kt

Este é o ponto de entrada do aplicativo. Aqui, você pode configurar o tema e a navegação inicial. O código abaixo mostra um exemplo básico de como configurar a `MainActivity` para chamar a função MainScreen, que representa a tela principal do aplicativo.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme { // Se usou o template padrão do Android Studio
                MainScreen()
            }
        }
    }
}
```

### 4. ui/screens/MainScreen.kt

Esta é a tela principal do aplicativo. Aqui, você pode definir a UI usando Jetpack Compose. O exemplo abaixo mostra uma tela simples com um título e uma lista de produtos.

```kotlin
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val produtos by viewModel.produtos.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        // Formulário para adicionar produto
        var nome by remember { mutableStateOf("") }
        var preco by remember { mutableStateOf("") }
        
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do produto") }
        )
        OutlinedTextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text("Preço") }
        )
        Button(onClick = {
            viewModel.adicionarProduto(nome, preco.toDoubleOrNull() ?: 0.0)
        }) {
            Text("Adicionar")
        }

        // Lista de produtos
        LazyColumn {
            items(produtos) { produto ->
                Text("${produto.nome} - R$ ${produto.preco}")
            }
        }
    }

    // Carrega os produtos ao iniciar
    LaunchedEffect(Unit) {
        viewModel.carregarProdutos()
    }
}
```

### 5. data/models/Produto.kt

Este arquivo define o modelo de dados para os produtos. O MongoDB Realm usa anotações para definir como os dados são armazenados. O exemplo abaixo mostra como criar um modelo de produto com um ID único, nome e preço.

```kotlin
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Produto : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var nome: String = ""
    var preco: Double = 0.0
}
```

### 6. data/MongoDB.kt

Este arquivo contém a configuração do MongoDB Realm. Aqui, você pode definir a configuração do banco de dados e abrir uma instância do Realm. O exemplo abaixo mostra como configurar o Realm com o modelo de produto.

```kotlin	
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
```

### 7. viewmodels/MainViewModel.kt

Este arquivo contém a lógica do ViewModel, que é responsável por gerenciar os dados e a lógica de negócios do aplicativo. O exemplo abaixo mostra como carregar produtos do banco de dados e adicionar novos produtos.

```kotlin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos = _produtos

    fun carregarProdutos() {
        viewModelScope.launch {
            val realm = MongoDB.getInstance()
            _produtos.value = realm.query(Produto::class).find().list
        }
    }

    fun adicionarProduto(nome: String, preco: Double) {
        viewModelScope.launch {
            val realm = MongoDB.getInstance()
            realm.write {
                copyToRealm(Produto().apply {
                    this.nome = nome
                    this.preco = preco
                })
            }
            carregarProdutos()
        }
    }
}
```
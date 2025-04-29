import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.excelsior.viewmodels.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val produtos by viewModel.produtos.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        // Formulário para adicionar produto
        var nome by remember { mutableStateOf("") }
        var preco by remember { mutableStateOf("") }

        OutlinedTextField(
            value = nome,
            onValueChange = {
                val it
                nome = it
            },
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

@Composable
fun Text(s: String) {

}

package com.example.excelsior.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.excelsior.data.models.Produto
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
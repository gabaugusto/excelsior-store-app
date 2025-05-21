package com.example.excelsior.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.excelsior.data.models.Produto
import io.realm.kotlin.Realm
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    fun carregarProdutos() {
        viewModelScope.launch {
            val realm: Realm = MongoDB.getInstance()
            val results: RealmResults<Produto> = realm.query(Produto::class).find()
            _produtos.value = results.toList()
        }
    }

    fun adicionarProduto(nome: String, preco: Double) {
        viewModelScope.launch {
            val realm: Realm = MongoDB.getInstance()
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
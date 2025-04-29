package com.example.excelsior.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Produto : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var nome: String = ""
    var preco: Double = 0.0
}
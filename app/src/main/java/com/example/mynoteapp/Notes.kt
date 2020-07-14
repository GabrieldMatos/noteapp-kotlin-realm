package com.example.mynoteapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Notes (

    @PrimaryKey
    var id: Int?=null,
    var title:String?=null,
    var description:String?=null,
    var createAt:String?=null,
    var frequency:Int?=null,
    var alert:String?=null,
    var date:String?=null

) : RealmObject()
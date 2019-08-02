package dz.salim.android.travelmantics

class Traveldeal (){
    var id: String? = null
    var title: String? = null
    var price: String? = null
    var description: String? = null
    var imageUrl: String? = null

    constructor(title: String, price: String, description: String, imageUrl: String): this(){
        this.title = title
        this.price = price
        this.description = description
        this.imageUrl = imageUrl
    }
}
package es.uam.eps.enriquez.cueto.cards.viewModels

import androidx.lifecycle.MutableLiveData
import es.uam.eps.enriquez.cueto.cards.Card
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.uam.eps.enriquez.cueto.cards.Deck

private const val FECHA = 0
private const val TAG = "CardListViewModel"
private const val DATABASENAME_CARD = "Cards"
private  const val DATABASENAME_DECK ="Decks"
class CardListViewModel : ViewModel() {


    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun getAuth()=auth
    private var currentDeckID: String = ""
    private  val database = FirebaseDatabase.getInstance().getReference(DATABASENAME_CARD).child(auth.uid.toString())
    var decks: MutableLiveData<List<Deck>> = MutableLiveData()
        private set
        get() {

                FirebaseDatabase.getInstance().getReference(DATABASENAME_DECK).child(auth.uid.toString())
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}

                        override fun onDataChange(p0: DataSnapshot) {
                            var listOfDecks: MutableList<Deck> = mutableListOf<Deck>()
                            for (deck in p0.children) {
                                var newDeck = deck.getValue(Deck::class.java)
                                if (newDeck != null)
                                    listOfDecks.add(newDeck)
                            }
                            field.postValue(listOfDecks)
                        }
                    })

            return field
        }

    var cards: MutableLiveData<List<Card>> = MutableLiveData()
        private set
        get() {


                database.child(currentDeckID)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {}

                        override fun onDataChange(p0: DataSnapshot) {
                            var listOfCards: MutableList<Card> = mutableListOf<Card>()
                            for (card in p0.children) {
                                var newCard = card.getValue(Card::class.java)
                                if (newCard != null)
                                    listOfCards.add(newCard)
                            }
                            field.postValue(listOfCards)
                        }
                    })

            return field
        }
    private var maxCards2Study  = 20
    private var cards2Study = 0
    private lateinit var cardsToStudy: List<Card>
    private var currentCardID: String = " "
    private var cardCurrent: Card = Card(question = " ", answer = " ")
    private var answered = false

    fun selectCard() {
        cardCurrent = cards.value!!.filter { it.id == currentCardID }[0]
    }

    fun setmaxCards2Study (s: String?){
        if (s != null) {
            maxCards2Study = s.toInt()
        }
    }
    fun setCurrentCardID(id: String) {
        currentCardID = id
    }

    fun setCurrentDeckID(id: String) {
        currentDeckID = id
    }


    fun getDateCurrentCard(): String = cardCurrent.date
    fun getQuestionCurrentCard(): String = cardCurrent.question
    fun getAnswerCurrentCard(): String = cardCurrent.answer
    fun getIDCurrentCard() = cardCurrent.id


    fun setAnswerCurrentCard(string: String) {
        cardCurrent.answer = string
    }

    fun setQuestionCurrentCard(string: String) {
        cardCurrent.question = string
    }

    fun getAnswered(): Boolean = answered
    fun resAnswered() {
        answered = false
    }

    fun activateAnswered() {
        answered = true
    }

    fun studingInit() {
        cardsToStudy = cards.value!!.filter { it.currentDate >= FECHA }
        cards2Study =0
    }

    fun nextShowCard(): Boolean {

        var next = false

        if (cardsToStudy.isNotEmpty() && (cards2Study < maxCards2Study)) {
            cardCurrent = cardsToStudy[0]
            cards2Study++
            next = true
        }

        return next
    }

   fun  removeCardToStudy(){
       cardsToStudy =  cardsToStudy.drop(1)
   }
    fun setQualityCurrentCard(int: Int) {
        cardCurrent.quality = int
    }

    fun updateCurrentCard() {
        cardCurrent.update()
        database.child(currentDeckID).child(cardCurrent.id).setValue(cardCurrent)

    }

    fun addCard() {
        val card = Card(question = "Pregunta", answer = "Respuesta")
        database.child(currentDeckID).child(card.id).setValue(card)
    }

    fun addDeck() {

        val deck = Deck("Deck")
        FirebaseDatabase.getInstance().getReference(DATABASENAME_DECK).child(auth.uid.toString()).child(deck.id).setValue(deck)
    }
    fun getCurrentDeckID() = currentDeckID

    fun updateListOfCards(id: String = currentCardID ,field:String ,data:String){

        database.child(currentDeckID).child(id).child(field).setValue(data)
    }
    fun deleteCardToList() {
        database.child(currentDeckID).child(currentCardID).removeValue()
    }
}
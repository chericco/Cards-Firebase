package es.uam.eps.enriquez.cueto.cards.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import es.uam.eps.enriquez.cueto.cards.Card
import es.uam.eps.enriquez.cueto.cards.R
import es.uam.eps.enriquez.cueto.cards.SettingsActivity
import es.uam.eps.enriquez.cueto.cards.viewModels.CardListViewModel
import kotlinx.android.synthetic.main.fragment_card_list.*

private const val TAG = "CardListFragment"
private const val DATABASENAME = "Decks"

class CardListFragment : Fragment() {

    companion object {
        fun newInstance() = CardListFragment()
    }


    private lateinit var cardRecyclerView: RecyclerView
    private var adapter: CardAdapter = CardAdapter(emptyList())
    lateinit var card1: Card


    private val cardListViewModel by lazy {
        activity?.run { ViewModelProviders.of(this).get(CardListViewModel::class.java) }
    }

    interface onCardListFragmentInteractionListener {
        fun onCardSelected()
        fun onBackToDeckList()
        fun onStuding()
    }
    var listener: onCardListFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as onCardListFragmentInteractionListener?
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_card_list, container, false)

        cardRecyclerView = view.findViewById(R.id.card_recycler_view) as RecyclerView
        cardRecyclerView.adapter = adapter
        cardRecyclerView.layoutManager = LinearLayoutManager(context)

        cardListViewModel!!.setmaxCards2Study(SettingsActivity.getMaximumNumberOfCards(requireContext()))


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observer =
            Observer<List<Card>> {
                if (it!= null){
                    updateUI(it)
                    Log.d(TAG, "Lista actualizada")
                }
            }

       cardListViewModel!!.cards.observe(viewLifecycleOwner, observer)

        new_card_fab.setOnClickListener {

            cardListViewModel!!.addCard()
            Snackbar.make(view, "Tarjeta añadida", Snackbar.LENGTH_SHORT).show()

        }
    }



    private fun updateUI(cards: List<Card>) {

        adapter = CardAdapter(cards)
        cardRecyclerView.adapter = adapter
    }

    private inner class CardHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                cardListViewModel!!.setCurrentCardID(cardID)
                cardListViewModel!!.selectCard()
                listener?.onCardSelected()

            }
        }

        val questionTextView: TextView = itemView.findViewById(R.id.list_item_question)
        val answerTextView: TextView = itemView.findViewById(R.id.list_item_answer)
        val dateTextView: TextView = itemView.findViewById(R.id.list_item_date)
        lateinit var cardID: String

        fun bind(card: Card) {
            card1 = card
            cardID = card1.id
            questionTextView.text = card1.question
            answerTextView.text = card1.answer
            dateTextView.text = card1.date.toString().substring(0, 13)
        }
    }

    private inner class CardAdapter(val cards: List<Card>) : RecyclerView.Adapter<CardHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {

            val view = layoutInflater.inflate(R.layout.list_item_card, parent, false)
            return CardHolder(view)
        }

        override fun getItemCount() = cards.size

        override fun onBindViewHolder(holder: CardHolder, position: Int) {
            holder.bind(cards[position])
        }
    }



    override fun onResume() {
        super.onResume()


        bottomAppbar.setNavigationOnClickListener {
            listener?.onBackToDeckList()
            cardListViewModel!!.setCurrentDeckID("")
        }

        bottomAppbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.study -> {

                    cardListViewModel!!.studingInit()
                    if (cardListViewModel!!.nextShowCard()){
                        listener?.onStuding()
                    }else{
                        view?.let { Snackbar.make(it, "Tomate un descado, nada que estudiar", Snackbar.LENGTH_SHORT).show() }
                    }
                    true
                }else ->super.onOptionsItemSelected(menuItem)
            }

        }

    }

}
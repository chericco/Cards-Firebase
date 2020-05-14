package es.uam.eps.enriquez.cueto.cards.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import es.uam.eps.enriquez.cueto.cards.Deck
import es.uam.eps.enriquez.cueto.cards.R
import es.uam.eps.enriquez.cueto.cards.viewModels.CardListViewModel
import kotlinx.android.synthetic.main.fragment_card_list.*
import kotlinx.android.synthetic.main.fragment_deck_list.*
import kotlinx.android.synthetic.main.fragment_deck_list.bottomAppbar

/**
 * A simple [Fragment] subclass.
 */
private const val TAG = "DeckListFragment"
private const val DATABASENAME = "Decks"
class DeckListFragment : Fragment() {



    companion object {
        fun newInstance(): DeckListFragment {
            return  DeckListFragment()
        }
    }


    private lateinit var deckRecyclerView: RecyclerView
    private lateinit var adapter: DeckAdapter
    lateinit var auxDeck: Deck



    private  val deckViewModel by lazy {
        activity?.run { ViewModelProviders.of(this).get(CardListViewModel::class.java) }
    }


    interface onDeckListFragmentInteractionListener {
        fun onDeckSelected()
        fun onSettings()
        fun onLogOut()
    }
    var listener: onDeckListFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as onDeckListFragmentInteractionListener?
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
        val view = inflater.inflate(R.layout.fragment_deck_list, container, false)

        deckRecyclerView = view.findViewById(R.id.deck_recycler_view) as RecyclerView
        deckRecyclerView.layoutManager = LinearLayoutManager(activity)



        return view
    }



    private fun updateUI(decks: List<Deck>) {
        adapter = DeckAdapter(decks)
        deckRecyclerView.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observer =
            Observer<List<Deck>> {
                        updateUI(it)
                        Log.d(TAG, "Lista actualizada")
                    }

        deckViewModel!!.decks!!.observe(viewLifecycleOwner,observer)

        new_deck_fab.setOnClickListener {
            deckViewModel!!.addDeck()
            Snackbar.make(view, "Deck añadido", Snackbar.LENGTH_SHORT).show()
        }
    }

    private inner class DeckHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                deckViewModel!!.setCurrentDeckID(deckID)
                listener?.onDeckSelected()
            }
        }

        val nameTextView: TextView = itemView.findViewById(R.id.list_item_name)

        lateinit var deckID: String

        fun bind(deck: Deck) {
            auxDeck = deck
            deckID = auxDeck.id
            nameTextView.text = auxDeck.name
        }
    }

    private inner class DeckAdapter(val decks: List<Deck>) : RecyclerView.Adapter<DeckHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckHolder {

            val view = layoutInflater.inflate(R.layout.list_item_deck, parent, false)
            return DeckHolder(view)
        }

        override fun getItemCount() = decks.size

        override fun onBindViewHolder(holder: DeckHolder, position: Int) {
            holder.bind(decks[position])
        }
    }

    override fun onResume() {
        super.onResume()
        bottomAppbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.settings -> {
                    listener?.onSettings()
                    true
                }
                R.id.logout ->{
                    listener?.onLogOut()
                    true
                }else ->super.onOptionsItemSelected(menuItem)
            }

        }
    }

}

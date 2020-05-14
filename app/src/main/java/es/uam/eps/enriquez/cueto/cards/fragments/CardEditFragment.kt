package es.uam.eps.enriquez.cueto.cards.fragments
import android.content.Context
import es.uam.eps.enriquez.cueto.cards.Card
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

import es.uam.eps.enriquez.cueto.cards.R
import es.uam.eps.enriquez.cueto.cards.viewModels.CardListViewModel
import kotlinx.android.synthetic.main.fragment_card_edit.*


/**
 * A simple [Fragment] subclass.
 */

const val TAG1 = "CardEditFragment"
private  const val  DATABASENAME = "Decks"

class CardEditFragment : Fragment() {

    companion object {


        fun newInstance() = CardEditFragment()

    }

    interface onCardEditFragmentInteractionListener {
        fun onBackToList()
    }
    var listener: onCardEditFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as onCardEditFragmentInteractionListener?
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private lateinit var questionTextEdit: EditText
    private lateinit var answerTextEdit: EditText
    private var mutex = false
    private lateinit var dateTextView: TextView

    private val cardViewModel by lazy {
            activity?.run { ViewModelProviders.of(this).get(CardListViewModel::class.java) }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card_edit, container, false)

        questionTextEdit = view.findViewById<EditText>(R.id.question_edit_text)
        answerTextEdit = view.findViewById<EditText>(R.id.answer_edit_text)
        dateTextView = view.findViewById<TextView>(R.id.date_text_view)

        return view
    }



    override fun onStart() {
        super.onStart()

        val questionTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cardViewModel!!.setQuestionCurrentCard(s.toString())
            }
        }

        val answerTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cardViewModel!!.setAnswerCurrentCard( s.toString())

            }
        }

        question_edit_text.addTextChangedListener(questionTextWatcher)
        answer_edit_text.addTextChangedListener(answerTextWatcher)



                questionTextEdit.setText(cardViewModel!!.getQuestionCurrentCard())
                answerTextEdit.setText(cardViewModel!!.getAnswerCurrentCard())
                uuid_text_view.text = cardViewModel!!.getIDCurrentCard()
                date_text_view.text = cardViewModel!!.getDateCurrentCard().substring(0, 16)





    }

    override fun onResume() {
        super.onResume()

        bottomAppbar.setNavigationOnClickListener {
            listener?.onBackToList()

        }

        del_card_fab.setOnClickListener{
            mutex = true
            cardViewModel!!.deleteCardToList()
            listener?.onBackToList()
        }
    }


    override fun onStop() {
        super.onStop()
        if(!mutex){
            cardViewModel!!.updateListOfCards(field = "question", data = cardViewModel!!.getQuestionCurrentCard())
            cardViewModel!!.updateListOfCards(field = "answer", data = cardViewModel!!.getAnswerCurrentCard())
        }



}


}
package es.uam.eps.enriquez.cueto.cards.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import es.uam.eps.enriquez.cueto.cards.R
import es.uam.eps.enriquez.cueto.cards.viewModels.CardListViewModel
import kotlinx.android.synthetic.main.fragment_card_study.*

/**
 * A simple [Fragment] subclass.
 */
private const val TAG = "CardStudyFragment"
class CardStudyFragment : Fragment() {

    companion object {

        fun newInstance() =  CardStudyFragment ()
    }

    private fun message(msg: String) {
        Log.d(TAG, msg)
    }

    private val cardViewModel by lazy {
        activity?.run { ViewModelProviders.of(this).get(CardListViewModel::class.java) }
    }

    interface onCardStudyFragmentInteractionListener {
        fun onStuding()
        fun onBackToList()
    }
    var listener: onCardStudyFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as onCardStudyFragmentInteractionListener?
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card_study, container, false)

        return  view
    }

  override fun onResume() {
      super.onResume()

      if (cardViewModel!!.getAnswered()) {

          answer_button.visibility = View.INVISIBLE
          difficulty_bar.visibility = View.VISIBLE
          question_Text_View.text = cardViewModel!!.getAnswerCurrentCard()

      }else{
          answer_button.visibility = View.VISIBLE
          difficulty_bar.visibility = View.INVISIBLE
          question_Text_View.text = cardViewModel!!.getQuestionCurrentCard()
      }


      answer_button.setOnClickListener {

          cardViewModel!!.activateAnswered()
          question_Text_View.text = cardViewModel!!.getAnswerCurrentCard()
          answer_button.visibility = View.INVISIBLE
          difficulty_bar.visibility = View.VISIBLE

      }

      hard_button.setOnClickListener{
          cardViewModel!!.setQualityCurrentCard(0)
          cardViewModel!!.updateCurrentCard()
          nextShowCard()
      }

      doubt_button.setOnClickListener{
          cardViewModel!!.setQualityCurrentCard(3)
          cardViewModel!!.updateCurrentCard()
          nextShowCard()
      }

      easy_button.setOnClickListener{
          cardViewModel!!.setQualityCurrentCard(5)
          cardViewModel!!.updateCurrentCard()
          nextShowCard()
      }
  }

fun nextShowCard(){

    cardViewModel!!.removeCardToStudy()

    if (cardViewModel!!.nextShowCard()){

        cardViewModel!!.resAnswered()

        listener?.onStuding()

    }else{
        cardViewModel!!.resAnswered()
        listener?.onBackToList()
    }
  }


}

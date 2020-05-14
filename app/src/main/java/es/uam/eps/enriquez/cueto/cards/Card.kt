package es.uam.eps.enriquez.cueto.cards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.math.*

@Entity
data class Card
    (
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var question: String = "asd",
    var answer: String = "asd",
    var date: String = Date().toString(),
    var quality: Int = 0,
    var repetitions: Int = 0,
    var interval: Int = 1,
    var NextPracticeDate: Int = 1,
    var easiness: Double = 2.5,
    var currentDate: Int = 0
) {



    fun update() {

        easiness = max(1.3, easiness + 0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02) * 10.0)

        repetitions = if (quality < 3) 0;
        else repetitions + 1;

        interval = if (repetitions <= 1) 1;
        else if (repetitions == 2) 6;
        else (easiness * repetitions).roundToInt();

        currentDate += interval


    }

    fun details() {
        println("$question ($answer) eas = $easiness rep = $repetitions int = $interval ")
    }

    fun show_simulation(_quality: Int) {
        quality = _quality
        update()
        details()
    }

}
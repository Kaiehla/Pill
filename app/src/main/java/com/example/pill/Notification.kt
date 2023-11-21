import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pill.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linearLayout = LinearLayout(this)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
        linearLayout.setPadding(20, 20, 20, 20)

        val textView = TextView(this)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.text = "It's time to take your medicine!"
        textView.setTextColor(resources.getColor(android.R.color.black))
        textView.textSize = 20f
        textView.setTypeface(null, Typeface.BOLD)

        val takeMedicineButton = Button(this)
        takeMedicineButton.id = R.id.takeMedicineButton
        takeMedicineButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        takeMedicineButton.gravity = Gravity.CENTER
        takeMedicineButton.setBackgroundResource(R.color.purple_700)
        takeMedicineButton.text = "Take medicine"

        val imageView = ImageView(this)
        imageView.id = R.id.imageView5
        imageView.layoutParams = LinearLayout.LayoutParams(
            375,
            31
        )
        imageView.setImageResource(R.drawable.icon_pill)

        val remindLaterButton = Button(this)
        remindLaterButton.id = R.id.remindLaterButton
        remindLaterButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        remindLaterButton.gravity = Gravity.CENTER
        remindLaterButton.setBackgroundResource(R.color.purple_500)
        remindLaterButton.text = "Remind me later"
        remindLaterButton.setTextColor(resources.getColor(R.color.PalePink))
        remindLaterButton.highlightColor = resources.getColor(R.color.PalePink)
        remindLaterButton.highlightColor= resources.getColor(R.color.PalePink)

        linearLayout.addView(textView)
        linearLayout.addView(takeMedicineButton)
        linearLayout.addView(imageView)
        linearLayout.addView(remindLaterButton)

        setContentView(linearLayout)
    }
}
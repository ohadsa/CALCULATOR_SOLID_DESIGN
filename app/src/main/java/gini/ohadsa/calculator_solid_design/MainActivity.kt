package gini.ohadsa.calculator_solid_design


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import gini.ohadsa.calculator_solid_design.databinding.ActivityMainBinding
import gini.ohadsa.calculator_solid_design.models.ScientificCalculator


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var display: String
        get() = binding.editResult.text.toString()
        set(value) {
            binding.editResult.text = value
        }


    private var calc = ScientificCalculator()
    private lateinit var operations : List<CharSequence>
    private lateinit var digitsAndDot : List<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        operations  = binding.opGroup.referencedIds.map { findViewById<Button>(it).text }
        digitsAndDot = binding.digitGroup.referencedIds.map { findViewById<Button>(it).text }
        val buttons: List<Button> = (binding.flowBtn.referencedIds.map(this::findViewById))
        buttons.forEach { it.setOnClickListener(this::buttonsRouter) }
    }


    private fun buttonsRouter(view: View) {
        var result = ""
        with(view as Button) {
            result = when (view.text) {
                in digitsAndDot -> digitTapped(this)
                in operations -> operandTapped(this)
                else -> throw RuntimeException("Input error")
            }
        }
        display = result
    }


    private fun operandTapped(button: Button): String =
        calc.operationClicked(button.text.toString(), display).toString()

    private fun digitTapped(digit: Button): String =
        calc.digitClicked(digit.text.toString(), display)


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        calc.state = savedInstanceState
        display = savedInstanceState.getString("display",display)?:"0"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(calc.state)
        outState.putString("display" , display)

    }
}




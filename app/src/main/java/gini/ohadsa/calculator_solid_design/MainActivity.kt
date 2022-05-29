package gini.ohadsa.calculator_solid_design


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import gini.ohadsa.calculator_solid_design.databinding.ActivityMainBinding
import gini.ohadsa.calculator_solid_design.models.CalculatorBrain
import gini.ohadsa.calculator_solid_design.models.ScientificCalculator


class MainActivity : AppCompatActivity() {
    //TODO number format
    // activity - find views, set on .. listener, talk to brain get result and displayed it, save the state
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var displayedValue: String
        get() = binding.editResult.text.toString()
        set(value) {
            binding.editResult.text = value
        }

    private var calculator: CalculatorBrain = ScientificCalculator()
    private lateinit var operations: List<String>
    private lateinit var digitsAndDot: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        operations = binding.opGroup.referencedIds.map { "${findViewById<Button>(it).text}"  }
        digitsAndDot = binding.digitGroup.referencedIds.map { "${findViewById<Button>(it).text}" }
        val buttons: List<Button> = (binding.flowBtn.referencedIds.map(this::findViewById))
        buttons.forEach { it.setOnClickListener(this::buttonsRouter) }
    }

    private fun buttonsRouter(view: View) {
        val result :String?
        val op = "${(view as Button).text}"
        result = when (op) {
            in digitsAndDot -> calculator.digitClicked(op, displayedValue)
            in operations -> calculator.operationClicked(op, displayedValue)
            else -> throw RuntimeException("Input error")
        }
        displayedValue = result
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        calculator.state = savedInstanceState
        displayedValue = savedInstanceState.getString("display", displayedValue) ?: "0"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(calculator.state)
        outState.putString("display", displayedValue)
    }
}




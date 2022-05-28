package gini.ohadsa.calculator_solid_design


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import gini.ohadsa.calculator_solid_design.databinding.ActivityMainBinding
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var display: String
        get() = binding.editResult.text.toString()
        set(value) {
            binding.editResult.text = value
        }

    //every UI can choose its own operations
    private var operations = mutableMapOf<String, MathOperation>()
    init {
        //const
        operations["AC"] = MathOperation.ConstC { 0.0 }
        operations["e"] = MathOperation.ConstC { E }
        operations["π"] = MathOperation.ConstC { PI }
        //unary
        operations["-/+"] = MathOperation.Unary { x -> -1 * x }
        operations["√"] = MathOperation.Unary { x -> sqrt(x) }
        operations["%"] = MathOperation.Unary { x -> x / 100 }
        operations["x²"] = MathOperation.Unary { x -> x * x }
        operations["x³"] = MathOperation.Unary { x -> x * x * x }
        operations["x⁻¹"] = MathOperation.Unary { x -> 1 / x }
        operations["sin"] = MathOperation.Unary { x -> sin(x) }
        operations["tan"] = MathOperation.Unary { x -> tan(x) }
        operations["cos"] = MathOperation.Unary { x -> cos(x) }
        operations["sin⁻¹"] = MathOperation.Unary { x -> asin(x) }
        operations["cos⁻¹"] = MathOperation.Unary { x -> acos(x) }
        operations["tan⁻¹"] = MathOperation.Unary { x -> atan(x) }
        operations["ln"] = MathOperation.Unary { x -> ln(x) }
        operations["eˣ"] = MathOperation.Unary { x -> E.pow(x) }
        //binary
        operations["+"] = MathOperation.Binary { x, y -> x + y }
        operations["-"] = MathOperation.Binary { x, y -> x - y }
        operations["x"] = MathOperation.Binary { x, y -> x * y }
        operations["÷"] = MathOperation.Binary { x, y -> x / y }
        operations["xʸ"] = MathOperation.Binary { x, y -> x.pow(y) }
        //equals
        operations["="] = MathOperation.Equals

    }

    private var calc = CalculatorBrain(operations)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val buttons: List<Button> = (binding.flowBtn.referencedIds.map(this::findViewById))
        buttons.forEach { it.setOnClickListener(this::buttonsRouter) }
    }


    private fun buttonsRouter(view: View) {
        val operations = binding.opGroup.referencedIds.map { findViewById<Button>(it).text }
        val digitsAndDot = binding.digitGroup.referencedIds.map { findViewById<Button>(it).text }
        println(operations.toString())
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




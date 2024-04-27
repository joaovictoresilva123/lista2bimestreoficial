package com.example.lista2bim
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var gyroSensor: Sensor? = null

    private var altura: Double = 0.0
    private var peso: Double = 0.0
    private var contarpassos: Int = 0

    private lateinit var altura_digitada: EditText
    private lateinit var peso_digitada: EditText
    private lateinit var contadordepassosmostrar: TextView
    private lateinit var caloriasqueimadas: TextView
    private lateinit var botaocalcularcalorias: Button

    private var tempoultimopasso: Long = 0
    private val intervalopassos = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        altura_digitada = findViewById(R.id.altura_digitada)
        peso_digitada = findViewById(R.id.pesodigitadas)
        contadordepassosmostrar = findViewById(R.id.contadordepassosmostrar)
        caloriasqueimadas = findViewById(R.id.caloriasqueimadas)
        botaocalcularcalorias = findViewById(R.id.botaocalcularcalorias)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        botaocalcularcalorias.setOnClickListener {
            altura = altura_digitada.text.toString().toDouble()
            peso = peso_digitada.text.toString().toDouble()

            caloriasqueimadas.text = calculateCaloriesBurned().toString()
        }
    }

    override fun onResume() {
        super.onResume()
        gyroSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - tempoultimopasso > intervalopassos) {
                contarPassos()
                tempoultimopasso = currentTime
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun contarPassos() {
        contarpassos++
        contadordepassosmostrar.text = contarpassos.toString()
    }

    private fun calculateCaloriesBurned(): Double {
        val valorMET = 3.5
        val caloriasporkg = valorMET * 3.5
        val caloriesporpasso = caloriasporkg * (peso / 60)

        return contarpassos * caloriesporpasso
    }
}

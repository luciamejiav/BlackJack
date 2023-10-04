package net.azarquiel.blackjack

import android.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import net.azarquiel.blackjack.model.Carta
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var titulo = ""
    private lateinit var random: Random
    private lateinit var llcartas: LinearLayout //si lo usamos en muchos sitios declarar arriba
    val mazo = Array(40){i -> Carta() }
    var imazo = 0
    val palos = arrayOf("clubs", "diamonds", "hearts", "spades") //introducimos los palos
    var jugador = 0
    var puntos = Array(2){0}
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titulo =title.toString()

        random = Random(System.currentTimeMillis())

        llcartas = findViewById<LinearLayout>(R.id.llcartas)
        var ivmazo = findViewById<ImageView>(R.id.ivmazo)
        var btnstop = findViewById<Button>(R.id.btnstop)

        ivmazo.setOnClickListener { ivmazoOnClick() } //como tenemos dos botones hacemos dos funciones
        btnstop.setOnClickListener { btnstopOnClick() }

        mazoCarta()
        newGame()
    }

    private fun mazoCarta() {
        var c = 0 //iniciamos una variable contador del 0 al 39
        for (n in 1 .. 10){ //recorremos los 4 palos, como hemos puesto until no llega al 4, es del 0 al 3
            for (p in 0 until 4){ //recorremos las 10 cartas, los números
                mazo[c] = Carta(n, p)
                c++
            }
        }
    }


    private fun newGame() {
        imazo = 0
        jugador = 0
        puntos = Array(2){0}

        mazo.shuffle() //coge un vector y cambio todo de posición, baraja las cartas

        llcartas.removeAllViews() //al principio si hay cartas las quita todas
        sacaCarta()
        sacaCarta()

        for (carta in mazo){  //sirve para ver si estamos haciendo bien la app, como ya lo hemos comprobado lo quitamos del código
            carta.palo
            Log.d("paco", "${carta.numero} de ${palos[ carta.palo] }")
        }
    }

    private fun btnstopOnClick() { //fun es de funcion
        if (jugador == 0){
            nextPlayer()
        } else if (jugador == 1){
            gameOver()
        }
    }

    private fun gameOver() {
        var msg = ""
        if (puntos[0] > 21 && puntos[1] > 21) {
            msg = "empate"
        } else if (puntos[0] > 21){
            msg = "Gana el jugador 2"
        } else if (puntos[1] > 21){
            msg = "Gana el jugador 1"
        } else if (21 - puntos[0] < 21 - puntos[1]){
            msg = "Gana el jugador 1"
        } else if (21 - puntos[0] > 21 - puntos[1]){
            msg = "Gana el jugador 2"
        } else {
            msg = "empate"
        }

        //sacar un dialogo dicendo quien ha ganado
        AlertDialog.Builder(this)
        .setTitle("Game Over - $msg")
        .setMessage("Jugador 1: ${puntos[0]} puntos \n\n Jugador 2: ${puntos[1]} puntos")
        .setPositiveButton("New Game") { dialog, which ->
            newGame()
        }
        .setNegativeButton("Fin") { dialog, which ->
            finish()
        }
        .show()
    }

    private fun nextPlayer() {
        jugador = 1 //solo con poner esto ya saca las cartas del primer jugador
        llcartas.removeAllViews() //borramos las cartas anteriores
        //sacamos 2 cartas nuevas
        sacaCarta()
        sacaCarta()
    }

    private fun ivmazoOnClick() {
        sacaCarta()
    }

    private fun sacaCarta(){
        //sacamos cartas
        val cartaJuego = mazo[imazo]
        //miramos los puntos y se los acumulamos a jugador
        val n = if (cartaJuego.numero > 7) 10 else cartaJuego.numero
        puntos[jugador] += n
        imazo++
        //if (imazo == 40) imazo = 0
        //ponemos los puntos en la parte de arriba
        //title = "$titulo - ${puntos[jugador]} puntos"
        //esta creando una imagen, en kotlin no sale new image
        val ivCarta = ImageView(this)
        //buscamos el id de la imagen a traves del nombre
        val idimage = resources.getIdentifier("${palos[cartaJuego.palo]}${cartaJuego.numero}", "drawable", packageName)
        ivCarta.setImageResource(idimage)
        //ponemos atributos de alto, ancho,
        var lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(8, 0, 8, 0)
        ivCarta.layoutParams = lp
        //enganchamos la imageview al linear para que se pinte en la pantalla
        llcartas.addView(ivCarta)
        //Toast.makeText(this, "${puntos[jugador]} puntos", Toast.LENGTH_SHORT).show() //aparece un cartel con los puntos que llevas



        //ponemos los puntos en la parte de arriba
        title = "$titulo - ${puntos[jugador]} puntos"
        if (puntos[jugador] > 21 && jugador == 0){
            nextPlayer()
        } else if (jugador == 1 && puntos[jugador] > 21){
            gameOver()
        }

    }
}
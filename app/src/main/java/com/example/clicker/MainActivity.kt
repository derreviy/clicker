package com.example.clicker

import android.content.ClipData.Item
import android.icu.util.ULocale.AvailableType
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clicker.ui.theme.ClickerTheme

data class ShopItem(
    val name: String,
    val price: Int,
    val onBuy:  () -> Unit,
    var isAvailable: Boolean = true,
    val isMultiple: Boolean = false
)

sealed interface State {
    data object DefaultState : State
    data object TwoEtapState : State
    data object ThreeEtapState : State
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

        }
    }
}

@Composable
fun InfoBar(
    coins: Int,
    diamonds: Int,
    woods: Int,
    buff: Int
){

    Row {
        Text(
            text = "$coins \uD83D\uDCB6",
            modifier = Modifier
        )
        Spacer(Modifier.padding(8.dp))
        Text(
            text = "$diamonds \uD83D\uDC8E",
            modifier = Modifier
        )
        Spacer(Modifier.padding(8.dp))
        Text(
            text = "$woods \uD83E\uDEB5",
            modifier = Modifier
        )
        Spacer(Modifier.padding(8.dp))
        Text(
            text = "$buff",
            modifier = Modifier
        )
    }
}

@Composable
fun Clicker(onTap: () -> Unit){
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {
                onTap()
            }
            ,
            modifier = Modifier.height(80.dp).width(120.dp),
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text("Click", fontSize = 25.sp)
            }

        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var coins by remember { mutableIntStateOf(0) }
    var buff by remember { mutableIntStateOf(1) }
    var diamonds by remember { mutableIntStateOf(0) }
    var woods by remember { mutableIntStateOf(0) }
    var isShopOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var one by remember { mutableStateOf(false) }
    var state: State by remember { mutableStateOf(State.DefaultState) }
    var shopItems by remember { mutableStateOf(arrayListOf(
        ShopItem(
            name = "x2", price = 1, onBuy = { buff *= 2 }
        ),
        ShopItem(
            name = "x4", price = 2, onBuy = { buff *= 4 }
        ),
        ShopItem(
            name = "res", price = 0, isMultiple = true, onBuy = {
                coins = 0
                isShopOpen = false
                buff = 1
                diamonds = 0
                woods = 0
                Toast.makeText(context, "Все сброшено!", Toast.LENGTH_SHORT).show()
            }
        ),
        ShopItem(
            name = "+1 diamons", isMultiple = true, price = 0, onBuy = {
                diamonds += 1
            }
        )
        )
    )}






    when (isShopOpen) {
        true ->
            Column(modifier.fillMaxSize()) {
                Column(

                ) {
                    TextButton(onClick = {
                        isShopOpen = false
                    }) {
                        Text(text = "< Back")
                    }
                }
                 LazyHorizontalGrid (
                     rows = GridCells.Adaptive(40.dp),
                     modifier = Modifier.fillMaxSize()
                 ){
                     items(shopItems) { item ->
                         if (item.isAvailable)
                         Button(
                             onClick = {
                            if (item.price<= diamonds) {
                                if (!item.isMultiple) {
                                    item.isAvailable = false
                                    shopItems.remove(item)
                                }
                                item.onBuy()
                                diamonds -= item.price
                            }
                     }) {
                         Text(text = "${item.name}\n${item.price} \uD83D\uDC8E")
                     } }


                }
            }

        false -> Box(modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                InfoBar(coins, diamonds, woods, buff)
            }
            Row {
                OutlinedButton(
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
                    onClick = {
                    isShopOpen = !isShopOpen
                    one = false
                }) {
                    Text(text = "\uD83D\uDED2")
                }
            }
            // я знаю что можно проще но я уже зделал
            when (state){
                is State.DefaultState -> {
                    Clicker {
                        coins += buff
                        if (coins >= 1000) {
                            diamonds += 1
                            coins = 0
                            if (buff>= 500)
                                state = State.TwoEtapState
                        }
                    }
                }
                is State.TwoEtapState -> {
                    Clicker {
                        coins += buff
                        if (coins >= 10000) {
                            woods += 1
                            coins = 0
                            if (buff >= 1000)
                                state = State.ThreeEtapState
                        }
                    }
                }
                is State.ThreeEtapState -> {
                    Clicker {
                        coins += buff
                        if (coins >= 100000) {
                            diamonds += 100
                            woods += 1
                            coins = 0
                        }
                    }
                }
            }

        }


    }
}
package com.example.clicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.example.clicker.ui.theme.ClickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "hi",
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
        ){ Text(text = "Click") }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var coins by remember { mutableIntStateOf(0) }
    var buff by remember { mutableIntStateOf(1) }
    var diamonds by remember { mutableIntStateOf(0) }
    var woods by remember { mutableIntStateOf(0) }
    var isShopOpen by remember { mutableStateOf(false) }

    Box(modifier.fillMaxSize()) {
        Clicker {
            coins += buff
            if (coins >= 1000){
                diamonds += 1
                coins = 0
            }
        }
        Column(modifier.fillMaxSize()) {
            Row {
                Button(onClick = {
                    isShopOpen = !isShopOpen
                }) {
                    Text(text = "\uD83D\uDED2")
                }
                Spacer(Modifier.padding(20.dp))
                Button(onClick = {
                    diamonds += 1
                }) {
                    Text(text = "+1 diamond")
                }
            }
            InfoBar(coins, diamonds, woods, buff)
            if (isShopOpen) {
                Row {
                    Button(onClick = {
                        if (diamonds >= 1){
                            diamonds -= 1
                            buff *= 2
                        }else{
                             return@Button
                        }
                    }) {
                        Text(text = "x2")
                    }
                    Spacer(Modifier.padding(8.dp))
                    Button(onClick = {
                        if (diamonds >= 2){
                            diamonds -= 2
                            buff *= 4
                        }else{
                            return@Button
                        }

                    }) {
                        Text(text = "x4")
                    }
                    Spacer(Modifier.padding(8.dp))
                    Button(onClick = {
                        coins = 0
                        isShopOpen = false
                        buff = 1
                        diamonds = 0
                        woods = 0
                    }) {
                        Text(text = "res")
                    }
                }
            }
        }

    }
}


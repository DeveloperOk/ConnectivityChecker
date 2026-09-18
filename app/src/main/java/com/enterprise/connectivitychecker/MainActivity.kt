package com.enterprise.connectivitychecker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enterprise.connectivitychecker.ui.theme.ConnectivityCheckerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val isInternetAvailable = MutableStateFlow(false)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConnectivityCheckerTheme {
                ConnectivityCheckerApp(isInternetAvailable = isInternetAvailable)
            }
        }
    }
}

@Composable
fun ConnectivityCheckerApp(isInternetAvailable: MutableStateFlow<Boolean>) {

    val oneTimeFlag = retain { mutableStateOf(true) }

    val isInternetAvailableState = isInternetAvailable.collectAsStateWithLifecycle()

    val context = LocalContext.current

    if(oneTimeFlag.value){

        LaunchedEffect(true) {

            GlobalScope.launch(Dispatchers.Default) {

                while (true){

                    isInternetAvailable.update {
                        InternetManager.isInternetAvailable(context = context)
                    }

                    delay(1000L)

                }

                oneTimeFlag.value = false

            }

        }

    }



    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(innerPadding)){

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().weight(1F)){

                Text("Connectivity Manager")
                Text("Text is shown when internet is lost")


            }


            if(!isInternetAvailableState.value){

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(5.dp).fillMaxWidth()
                        .background(Color.Red, RoundedCornerShape(15.dp))
                        .padding(5.dp)) {

                    Text("No Internet Connection")

                }

            }

        }
    }

}


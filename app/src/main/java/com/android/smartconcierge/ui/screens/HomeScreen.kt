package com.android.smartconcierge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    visitorName: String,
    visitorCPF: String,
    onActionComplete: () -> Unit
) {
    var isAccepted by remember { mutableStateOf<Boolean?>(null) }

    if (visitorName.isNotEmpty() && visitorCPF.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Visita Solicitada", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Nome: $visitorName", style = MaterialTheme.typography.bodyMedium)
            Text(text = "CPF: $visitorCPF", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        isAccepted = false
                        onActionComplete()
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Rejeitar")
                }
                Button(
                    onClick = {
                        isAccepted = true
                        onActionComplete()
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Aceitar")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (isAccepted) {
                true -> Text(text = "Pedido Aceito!", color = MaterialTheme.colorScheme.primary)
                false -> Text(text = "Pedido Rejeitado.", color = MaterialTheme.colorScheme.error)
                null -> Text(text = "Aguardando ação...", color = MaterialTheme.colorScheme.secondary)
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sem novas solicitações...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}



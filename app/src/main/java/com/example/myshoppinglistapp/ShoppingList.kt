package com.example.myshoppinglistapp


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class  ShoppingItem(val id:Int,
                         var name: String,
                         var quantity: Int,
                         var isEditing : Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp (){
    var sItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false)}
    var itemName by remember { mutableStateOf("")}
    var itemQuantity by remember { mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {showDialog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            Text("Add")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){
               item ->
                if(item.isEditing){ //If we are editing (in other words if we clicked on the edit button, the we want to show the edit screen.
                    SHoppingItemEditor(item = item, onEditComplete = {
                        editedName, editedQuantity -> //editedName and editedQuantity are the parameters of this annonymous lambda method.
                        sItems = sItems.map {
                            it.copy(isEditing = false)}
                        val editedItem = sItems.find{it.id == item.id} //Finding the object that we are currently editing.
                        editedItem?.let{
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                }else{//Else we want to show the edit screen
                    ShoppingListItem(item = item, onEditClick = {
                        /*
                        When we are clicking on the edit button how do we know the edit button we clicked
                        **Please note: and remember sItems is the list of all the objects stored.
                        * */
                        sItems = sItems.map { it.copy(isEditing = it.id==item.id ) }


                    }, onDeleteClick = {
                        sItems = sItems - item
                    })
                }
            }
        }
    }

    if(showDialog){
        //display the alert dialog
       AlertDialog(
           onDismissRequest = { showDialog=false},
           confirmButton = {
                           Row(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(8.dp),
                               horizontalArrangement = Arrangement.SpaceBetween
                           ) {

//Add button of the alert dialog
                               Button(onClick ={

                                   if(itemName.isNotBlank()){
                                       val newItem = ShoppingItem(
                                           id = sItems.size+1,
                                           name = itemName,
                                           quantity = itemQuantity.toInt()
                                       )
                                       sItems = sItems + newItem
                                       showDialog = false
                                       itemName = ""
                                   }

                               }){
                                Text("Add") //Text on the add button
                               }
//Cancel button of the Alert dialog
                               Button(onClick = {

                               }) {
                                Text("Cancel")
                               }

                           }
           },
           title = { Text("Add Shopping item")},
           text = {
               Column {
                   OutlinedTextField(
                       value = itemName,
                       onValueChange = {itemName = it},
                       singleLine = true,
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(8.dp)
                       )

                   OutlinedTextField(
                       value = itemQuantity,
                       onValueChange = {itemQuantity = it},
                       singleLine = true,
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(8.dp)
                   )
               }
           })
    }

}


@Composable
fun SHoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name)}
    var editedQuantity by remember { mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing)}

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
        ){
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

            BasicTextField(
                value = editedQuantity,
                onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }

        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            }
        ) {
            Text("Save")
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
){

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick){
             Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }

            IconButton(onClick = onDeleteClick){
                Icon(imageVector =  Icons.Default.Delete, contentDescription = null)
            }
        }
    }

}
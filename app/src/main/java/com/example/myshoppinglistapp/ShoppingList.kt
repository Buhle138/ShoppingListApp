package com.example.myshoppinglistapp


import android.util.Log
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

            /*BIIIG NOTE:!!!!!!!  ALL OBJECTS START OFF THEIR isEditing as false. Scroll all the way up to find out.*/
            /*ANOTHER BIG NOTE!!!! THIS CODE DOES NOT STOP RUNNING UNTIL YOU CLOSE THE APPLICATION ON THE EMULATOR.
            THIS MEANS THAT IF DURING THE RUNTIME THE OBJECTS isEditing WAS FALSE AND NOW IT'S TRUE THE IF STATEMENT BELOW WILL RUN AGAIN.
            * */
            items(sItems){
               item -> //PLEASE NOTE THAT THIS ITEM IS THE CURRENT ITEM THAT WE ARE CURRENTLY ON
                //THE 'it' REPRESENTS ALL THE ALREADY EXISTING OBJECTS.
                //SO PICTURE IN YOUR HEAD TWO SCENARIOS  ONE WHERE THERE IS OLD OBJECTS which are represented by 'it'. TWO THE OLD OBJECT THAT WE ARE ON WILL BE REPRESENTED BY 'item'.
                if(item.isEditing){ //If we are editing (in other words if we clicked on the edit button, then we want to show the edit screen.

                    /*Plase note the onEditComplete button will only be executed when we click on the save button.*/

                    SHoppingItemEditor(item = item, onEditComplete = {
                        editedName, editedQuantity -> //editedName is the new name entered by the user same as editedQuantity they are from the SHoppingItem() composable.
                        sItems = sItems.map { it.copy(isEditing = false)}/*Once we click on the save button all the isEditing buttons will be false for all the objects.*/

                        val editedItem = sItems.find{it.id == item.id} /*Finding the object that we are currently editing.
                            True will be returned when we find the object that is currently edited. which will have id
                        */
                        editedItem?.let{
                            it.name = editedName //
                            it.quantity = editedQuantity
                        }


                    })
                }else{/*Else we want to show the screen with the added items. 'Note we haven't clicked yet on the edit icon.
                Once we click on the icon if editing an item. Then the onEditClick code will be ran.
                */
                    ShoppingListItem(item = item, onEditClick = {
                        /*
                        When we are clicking on the edit button how do we know the edit button we clicked
                        **Please note: and remember sItems is the list of all the objects stored.
                        * */
                        sItems = sItems.map { it.copy(isEditing = it.id==item.id ) } /*NOTE: THIS IS WHERE THE FALSE BOOLEAN OF THE OBJECT THAT WE CLICKED
                        GOES FROM FALSE TO 'TRUE'.
                        /*NOTE: THE NEW sItems ARE NOW SENT BACK TO THE IF items(sItems) METHOD TO BE EVALUATED AGAIN, OBVIOUSLY NOW AT THIS POINT THE OBJECT THAT WE CLICKED ON
                        has an isEditing property of 'TRUE' WHICH WILL NOW RUN THE CODE UNDER THE IF STATEMENT.
                        */
                        */


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

/*WHEN THE EDIT ICON IS CLICKED ON ANY OF THE CATEGORIES WE USE THIS COMPOSABLE.*/
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
                isEditing = false //isEditing is false because once we click on the save button we are no longer editing.
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            }
        ) {
            Text("Save")
        }
    }
}

/*WHEN THE ADD BUTTON IS CLICKED FROM THE ALERT DIALOG FORM.*/
@Composable
fun ShoppingListItem(item: ShoppingItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit, ){

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
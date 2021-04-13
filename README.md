# Prosjekt1-Inventory

<h1 align="Center">App name: Inventory</h1>

APK link: https://appdistribution.firebase.dev/i/b28a381845b885c0

My app works fluently and has distinct colors. Decided to go with bright colors to make the user remember to actually use the app. The functionality is working well and every feature is included. The progress bar changes dynamically and everything is stored in firebase, both todos, items below todos, how many have been completed and how many items there in each todo. Chose the name inventory because I thought this should be more than just a todo app, but it should be used for absolutely everything one needs to keep in mind not only todos. Recommended movies or games from friends etc, things you have to remember but will forget right after being told.

<h3 align="Left">Features</h3>


* Full functioning app with no crashes or malfunctions.
* Easy on the eyes. 
  * Color panel directed towards kids but can also be enjoyed by adults.

* Centered title with subsection
  * The title changes when the user clickes on a Todo
* Add a todo
* Delete the todo
* Progressbar for each todo 
* Click on the todo
* Add an item connected to the clicked todo
* Delete the item 
* Checkbar to complete the todo
* Progressbar planted in the toolbar 
   * Dynamicaly updating when a todo is checked, added or deleted.
*  Also added a go back button for eas of use

<h3 align="Left">Further functionality</h3>

*  Added darkmode to my app.
   *  Pop up dialog menu with 3 different choices
   *  Integrated with the users standard setting, by choosing system default

<h3 align="Left">Backend</h3>

* Firebase realtime database for cloud storage
* Todo is stored by title name and can expand to view information
  * Stores the number of completed items
  * Stores the size of all items
  * Deleting a todo, deletes all child items  
* Item is connected to the parent todo
  * Each item store a completed bool value and title name
  * Deleting an item does not delete the todo, only the item clicked on:))(Bug I had for a very long time)
  * Stores the value of the checkbox automaticly when changed
  
  
<h3 align="Left">Why I decided to use Firebase RealtimeDatabase</h3>
After reasearching the topic of realtime vs firestore i decided to use realtime database as my backend. The reasoning being that i wanted to try both and because i have experience with firestore I wanted to try realtime. Every article I read pointed me toward the functionality of realtime database and because the project description only mentioned firebase backend and not firestore, I decided to try it out.
<h3 align="Left">Unit testing</h3>
I have used unit testing to create and transform data by checking for the expected format and further checked for correct data in different cases by asserting into my data classes. 


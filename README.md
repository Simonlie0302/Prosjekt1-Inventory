# Prosjekt1-Inventory

<h1 align="Center">App name: Inventory</h1>

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
* Todo is stored by title
  * Stores the number of completed items
  * Stores the size of all items
  * Deleting a todo, deletes all child items  
* Item is connected to the parent todo
  * Each item store a completed bool value and title
  * Deleting an item does not delete the todo:))(Bug I had for a very long time)
  * Stores the value of the checkbox dynamicly when changed


